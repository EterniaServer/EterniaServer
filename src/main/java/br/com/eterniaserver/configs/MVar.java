package br.com.eterniaserver.configs;

import br.com.eterniaserver.EterniaServer;

public class MVar {

    public static String getMessage(String valor) {
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', getString(valor));
    }

    public static String getString(String valor) {
        return EterniaServer.messages.getString(valor);
    }

    public static String getColor(String valor) {
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', valor);
    }

}
