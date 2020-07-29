package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Staff {

    private final EterniaServer plugin;

    public Staff(EterniaServer plugin) {
        this.plugin = plugin;
    }

    public void sendChatMessage(String message, Player player) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("eternia.chat.staff")) {
                String format = EterniaServer.chatConfig.getString("staff.format");
                format = PlaceholderAPI.setPlaceholders((OfflinePlayer) player, format);
                format = plugin.getEFiles().getColor(format.replace(Constants.MESSAGE, message));
                p.sendMessage(format);
            }
        }
    }

}
