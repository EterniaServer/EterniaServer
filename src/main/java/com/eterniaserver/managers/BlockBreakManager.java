package com.eterniaserver.managers;

import com.eterniaserver.EterniaServer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;

public class BlockBreakManager {
    public BlockBreakManager(EterniaServer plugin) {
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