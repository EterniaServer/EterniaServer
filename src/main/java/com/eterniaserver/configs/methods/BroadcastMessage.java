package com.eterniaserver.configs.methods;

import com.eterniaserver.configs.MVar;
import org.bukkit.Bukkit;

public class BroadcastMessage {

    public BroadcastMessage(String message) {
        message = MVar.getMessage("server.prefix") + MVar.getMessage(message);
        Bukkit.broadcastMessage(message);
    }

    public BroadcastMessage(String message, Object valor) {
        message = MVar.getMessage("server.prefix") + MVar.getMessage(message);
        Bukkit.broadcastMessage(message.replace("%s", String.valueOf(valor)));
    }

    public BroadcastMessage(String message, Object valor, Object valor2) {
        message = MVar.getMessage("server.prefix") + MVar.getMessage(message);
        message = message.replace("%s", String.valueOf(valor));
        Bukkit.broadcastMessage(message.replace("%b", String.valueOf(valor2)));
    }

    public BroadcastMessage(String message, Object valor, Object valor2, Object valor3) {
        message = MVar.getMessage("server.prefix") + MVar.getMessage(message);
        message = message.replace("%s", String.valueOf(valor));
        message = message.replace("%b", String.valueOf(valor2));
        Bukkit.broadcastMessage(message.replace("%v", String.valueOf(valor3)));
    }

}
