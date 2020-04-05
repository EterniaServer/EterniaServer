package com.eterniaserver.configs.methods;

import com.eterniaserver.configs.MVar;
import org.bukkit.Bukkit;

public class ConsoleMessage {

    public ConsoleMessage(String message) {
        message = MVar.getMessage("server.prefix") + MVar.getMessage(message);
        Bukkit.getConsoleSender().sendMessage(message);
    }

    public ConsoleMessage(String message, Object valor) {
        message = MVar.getMessage("server.prefix") + MVar.getMessage(message);
        Bukkit.getConsoleSender().sendMessage(message.replace("%s", String.valueOf(valor)));
    }

    public ConsoleMessage(String message, Object valor, Object valor2) {
        message = MVar.getMessage("server.prefix") + MVar.getMessage(message);
        message = message.replace("%s", String.valueOf(valor));
        Bukkit.getConsoleSender().sendMessage(message.replace("%b", String.valueOf(valor2)));
    }

    public ConsoleMessage(String message, Object valor, Object valor2, Object valor3) {
        message = MVar.getMessage("server.prefix") + MVar.getMessage(message);
        message = message.replace("%s", String.valueOf(valor));
        message = message.replace("%b", String.valueOf(valor2));
        Bukkit.getConsoleSender().sendMessage(message.replace("%v", String.valueOf(valor3)));
    }

}
