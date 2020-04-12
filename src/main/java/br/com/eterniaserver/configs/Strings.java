package br.com.eterniaserver.configs;

import br.com.eterniaserver.EterniaServer;

public class Strings {

    public static java.lang.String getMessage(java.lang.String valor) {
        return getColor(getString(valor));
    }

    public static java.lang.String getString(java.lang.String valor) {
        return EterniaServer.messages.getString(valor);
    }

    public static java.lang.String getColor(java.lang.String valor) {
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', valor);
    }

}
