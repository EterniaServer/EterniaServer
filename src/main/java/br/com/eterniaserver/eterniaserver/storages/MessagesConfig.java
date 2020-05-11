package br.com.eterniaserver.eterniaserver.storages;

import br.com.eterniaserver.eterniaserver.EterniaServer;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MessagesConfig {

    public MessagesConfig(EterniaServer plugin) {
        File messagesConfigFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!messagesConfigFile.exists()) plugin.saveResource("messages.yml", false);
        
        plugin.msgConfig = new YamlConfiguration();
        try {
            plugin.msgConfig.load(messagesConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

}
