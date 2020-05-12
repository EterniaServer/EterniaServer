package br.com.eterniaserver.eterniaserver.configs;

import br.com.eterniaserver.eterniaserver.EterniaServer;

public class Strings {

    private final EterniaServer plugin;

    public Strings(EterniaServer plugin) {
        this.plugin = plugin;
    }

    public String getMessage(String valor) {
        return getColor(getString(valor));
    }

    public String getString(String valor) {
        return plugin.msgConfig.getString(valor, "&7Erro&8: &7String &3" + valor + " &7não encontrada&8.");
    }

    public String getColor(String valor) {
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', valor);
    }

}
