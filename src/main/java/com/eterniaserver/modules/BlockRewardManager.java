package com.eterniaserver.modules;

import com.eterniaserver.EterniaServer;
import com.eterniaserver.configs.CVar;
import com.eterniaserver.configs.MVar;
import com.eterniaserver.configs.Vars;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class BlockRewardManager {
    public BlockRewardManager(EterniaServer plugin) {
        if (CVar.getBool("modules.block-reward")) {
            Vars.blockreward = true;
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
            MVar.consoleReplaceMessage("modules.enable", "Block-Reward");
        } else {
            MVar.consoleReplaceMessage("modules.disable", "Block-Reward");
        }
    }
}
