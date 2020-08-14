package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.configs.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import br.com.eterniaserver.eterniaserver.configs.Strings;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Staff {

    public void sendChatMessage(String message, Player player) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("eternia.chat.staff")) {
                String format = EterniaServer.chatConfig.getString("staff.format");
                format = InternMethods.setPlaceholders(player, format);
                format = Strings.getColor(format.replace(Constants.MESSAGE, message));
                p.sendMessage(format);
            }
        }
    }

}
