package br.com.eterniaserver.modules.chatmanager.chats;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Strings;

import br.com.eterniaserver.configs.Vars;
import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Local {

    private final EterniaServer plugin;
    private final Messages messages;
    private final Strings strings;
    private final Vars vars;

    public Local(EterniaServer plugin, Messages messages, Strings strings, Vars vars) {
        this.plugin = plugin;
        this.messages = messages;
        this.strings = strings;
        this.vars = vars;
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
                        if (p.hasPermission("eternia.spy") && p != player && !(distance <= Math.pow(radius, 2) && vars.spy.getOrDefault(p, false))) {
                            p.sendMessage(strings.getColor("&8[&7SPY&8-&eL&8] &8" + player.getName() + ": " + message));
                        }
                        if (distance <= Math.pow(radius, 2)) {
                            pes += 1;
                            p.sendMessage(format);
                        }
                    } else {
                        if (p.hasPermission("eternia.spy") && p != player  && vars.spy.getOrDefault(p, false)) {
                            p.sendMessage(strings.getColor("&8[&7SPY&8-&eL&8] &8" + player.getName() + ": " + message));
                        }
                    }
                }
            }
        }
        if (pes <= 1) {
            messages.PlayerMessage("chat.noone", player);
        }
    }

}
