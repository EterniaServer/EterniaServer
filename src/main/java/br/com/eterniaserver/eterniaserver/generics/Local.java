package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Local {

    private final EFiles strings;

    public Local(EterniaServer plugin) {
        this.strings = plugin.getEFiles();
    }

    public void sendChatMessage(String message, Player player, int radius) {
        int pes = 0;
        final String format = getFormat(message, player);
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (radius <= 0) {
                pes += 1;
                p.sendMessage(format);
            } else if (player.getWorld() == p.getWorld() && p.getLocation().distanceSquared(player.getLocation()) <= Math.pow(radius, 2)) {
                pes += 1;
                p.sendMessage(format);
            } else if (p.hasPermission("eternia.spy") && Vars.spy.getOrDefault(p.getName(), false)) {
                p.sendMessage(strings.getColor("&8[&7SPY&8-&eL&8] &8" + player.getDisplayName() + ": " + message));
            }
        }
        if (pes <= 1) {
            strings.sendMessage("chat.noone", player);
        }
    }

    private String getFormat(String message, final Player player) {
        String format = PlaceholderAPI.setPlaceholders(player, EterniaServer.chatConfig.getString("local.format"));
        return strings.getColor(format.replace(Constants.MESSAGE, message));
    }

}
