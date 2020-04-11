package br.com.eterniaserver.modules;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.methods.ConsoleMessage;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class BlockRewardManager {

    public BlockRewardManager(EterniaServer plugin) {
        if (EterniaServer.configs.getBoolean("modules.block-reward")) {
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
            new ConsoleMessage("modules.enable", "Block-Reward");
        } else {
            new ConsoleMessage("modules.disable", "Block-Reward");
        }
    }

}
