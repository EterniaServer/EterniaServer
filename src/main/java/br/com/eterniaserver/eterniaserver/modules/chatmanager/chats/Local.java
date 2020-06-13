package br.com.eterniaserver.eterniaserver.modules.chatmanager.chats;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Local {

    private final EterniaServer plugin;
    private final EFiles strings;

    public Local(EterniaServer plugin) {
        this.plugin = plugin;
        this.strings = plugin.getEFiles();
    }

    public void SendMessage(String message, Player player, int radius) {
        int pes = 0;
        for (Player p : Bukkit.getOnlinePlayers()) {
            String format = plugin.chatConfig.getString("local.format");
            format = PlaceholderAPI.setPlaceholders(player, format);
            if (format != null) {
                format = strings.getColor(format.replace("%message%", message));
                if (radius <= 0) {
                    pes += 1;
                    p.sendMessage(format);
                } else {
                    if (player.getWorld() == p.getWorld()) {
                        double distance = p.getLocation().distanceSquared(player.getLocation());
                        if (p.hasPermission("eternia.spy") && p != player && !(distance <= Math.pow(radius, 2) && plugin.getVars().spy.getOrDefault(p, false))) {
                            p.sendMessage(strings.getColor("&8[&7SPY&8-&eL&8] &8" + player.getName() + ": " + message));
                        }
                        if (distance <= Math.pow(radius, 2)) {
                            pes += 1;
                            p.sendMessage(format);
                        }
                    } else {
                        if (p.hasPermission("eternia.spy") && p != player  && plugin.getVars().spy.getOrDefault(p, false)) {
                            p.sendMessage(strings.getColor("&8[&7SPY&8-&eL&8] &8" + player.getName() + ": " + message));
                        }
                    }
                }
            }
        }
        if (pes <= 1) {
            strings.sendMessage("chat.noone", player);
        }
    }

}
