package br.com.eterniaserver.eterniaserver.storages;

import br.com.eterniaserver.eterniaserver.EterniaServer;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Configs {

    public Configs(EterniaServer plugin) {
        File messagesConfigFile = new File(plugin.getDataFolder(), "config.yml");
        if (!messagesConfigFile.exists()) plugin.saveResource("config.yml", false);
        
        plugin.serverConfig = new YamlConfiguration();
        try {
            plugin.serverConfig.load(messagesConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

}
