package br.com.eterniaserver.eterniaserver.modules.chatmanager.chats;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Strings;

import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Staff {

    private final EterniaServer plugin;
    private final Strings strings;

    public Staff(EterniaServer plugin,Strings strings) {
        this.plugin = plugin;
        this.strings = strings;
    }

    public void SendMessage(String message, Player player) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("eternia.chat.staff")) {
                String format = plugin.chatConfig.getString("staff.format");
                format = PlaceholderAPI.setPlaceholders(player, format);
                if (format != null) {
                    format = strings.getColor(format.replace("%message%", message));
                    p.sendMessage(format);
                }
            }
        }
    }

}
