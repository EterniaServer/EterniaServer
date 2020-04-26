package br.com.eterniaserver.modules.chatmanager.chats;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Strings;

import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Local {

    public static void SendMessage(String message, Player player, int radius) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            String format = EterniaServer.chat.getString("local.format");
            format = PlaceholderAPI.setPlaceholders(player, format);
            int pes = 0;
            if (format != null) {
                format = Strings.getColor(format.replace("%message%", message));
                if (radius <= 0) {
                    pes += 1;
                    p.sendMessage(format);
                } else {
                    double distance = p.getLocation().distanceSquared(player.getLocation());
                    if (p.hasPermission("eternia.spy") && p != player && !(distance <= Math.pow(radius, 2)) ) {
                        p.sendMessage(Strings.getColor("&8[&7SPY&8-&eL&8] &7" + player.getName() + ": " + message));
                    }
                    if (distance <= Math.pow(radius, 2)) {
                        pes += 1;
                        p.sendMessage(format);
                    }
                }
                if (pes <= 1) {
                    Messages.PlayerMessage("chat.noone", player);
                }
            }
        }
    }

}
