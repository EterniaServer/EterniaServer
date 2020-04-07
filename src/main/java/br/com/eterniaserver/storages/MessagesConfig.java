package br.com.eterniaserver.storages;

import br.com.eterniaserver.EterniaServer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;

public class MessagesConfig {
    public MessagesConfig(EterniaServer plugin) {
        File messagesConfigFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!messagesConfigFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        EterniaServer.messagesConfig = new YamlConfiguration();
        try {
            EterniaServer.messagesConfig.load(messagesConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}