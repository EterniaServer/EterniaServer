package br.com.eterniaserver.modules.chatmanager.chats;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Strings;

import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Staff {

    public static void SendMessage(String message, Player player) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("eternia.chat.staff")) {
                String format = EterniaServer.chat.getString("staff.format");
                format = PlaceholderAPI.setPlaceholders(player, format);
                if (format != null) {
                    format = Strings.getColor(format.replace("%message%", message));
                    p.sendMessage(format);
                }
            }
        }
    }

}
