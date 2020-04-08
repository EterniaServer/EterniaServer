package br.com.eterniaserver.configs.methods;

import br.com.eterniaserver.configs.MVar;

import org.bukkit.entity.Player;

public class PlayerMessage {

    public PlayerMessage(String message, Player player) {
        message = MVar.getMessage("server.prefix") + MVar.getMessage(message);
        player.sendMessage(message);
    }

    public PlayerMessage(String message, Object valor, Player player) {
        message = MVar.getMessage("server.prefix") + MVar.getMessage(message);
        player.sendMessage(message.replace("%s", String.valueOf(valor)));
    }

    public PlayerMessage(String message, Object valor, Object valor2, Player player) {
        message = MVar.getMessage("server.prefix") + MVar.getMessage(message);
        message = message.replace("%s", String.valueOf(valor));
        player.sendMessage(message.replace("%b", String.valueOf(valor2)));
    }

    public PlayerMessage(String message, Object valor, Object valor2, Object valor3, Player player) {
        message = MVar.getMessage("server.prefix") + MVar.getMessage(message);
        message = message.replace("%s", String.valueOf(valor));
        message = message.replace("%b", String.valueOf(valor2));
        player.sendMessage(message.replace("%v", String.valueOf(valor3)));
    }

}
