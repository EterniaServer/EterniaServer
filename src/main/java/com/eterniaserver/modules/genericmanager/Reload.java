package com.eterniaserver.modules.genericmanager;

import com.eterniaserver.EterniaServer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Reload {
    public Reload() {
        EterniaServer plugin = EterniaServer.getMain();
        plugin.reloadConfig();
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
        File blocksFile = new File(plugin.getDataFolder(), "blocks.yml");
        if (!blocksFile.exists()) {
            plugin.saveResource("blocks.yml", false);
        }
        EterniaServer.blocks = new YamlConfiguration();
        try {
            EterniaServer.blocks.load(blocksFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
