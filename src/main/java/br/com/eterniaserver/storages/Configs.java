package br.com.eterniaserver.storages;

import br.com.eterniaserver.EterniaServer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Configs {

    public Configs(EterniaServer plugin) {
        File messagesConfigFile = new File(plugin.getDataFolder(), "config.yml");
        if (!messagesConfigFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        EterniaServer.configs = new YamlConfiguration();
        try {
            EterniaServer.configs.load(messagesConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

}