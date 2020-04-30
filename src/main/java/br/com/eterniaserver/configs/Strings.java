package br.com.eterniaserver.configs;

import br.com.eterniaserver.EterniaServer;

public class Strings {

    private final EterniaServer plugin;

    public Strings(EterniaServer plugin) {
        this.plugin = plugin;
    }

    public String getMessage(String valor) {
        return getColor(getString(valor));
    }

    public String getString(String valor) {
        return plugin.msgConfig.getString(valor);
    }

    public String getColor(String valor) {
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', valor);
    }

}
