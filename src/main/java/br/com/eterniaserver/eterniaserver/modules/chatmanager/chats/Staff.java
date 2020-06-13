package br.com.eterniaserver.eterniaserver.modules.chatmanager.chats;

import br.com.eterniaserver.eterniaserver.EterniaServer;

import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Staff {

    private final EterniaServer plugin;

    public Staff(EterniaServer plugin) {
        this.plugin = plugin;
    }

    public void SendMessage(String message, Player player) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("eternia.chat.staff")) {
                String format = plugin.chatConfig.getString("staff.format");
                format = PlaceholderAPI.setPlaceholders(player, format);
                if (format != null) {
                    format = plugin.getEFiles().getColor(format.replace("%message%", message));
                    p.sendMessage(format);
                }
            }
        }
    }

}
