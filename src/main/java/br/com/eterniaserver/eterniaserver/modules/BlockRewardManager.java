package br.com.eterniaserver.eterniaserver.modules;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class BlockRewardManager {

    public BlockRewardManager(EterniaServer plugin, Messages messages) {
        if (plugin.serverConfig.getBoolean("modules.block-reward")) {
            File blocksFile = new File(plugin.getDataFolder(), "blocks.yml");
            if (!blocksFile.exists()) plugin.saveResource("blocks.yml", false);
            
            plugin.blockConfig = new YamlConfiguration();
            try {
                plugin.blockConfig.load(blocksFile);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
            messages.ConsoleMessage("modules.enable", "%module%", "Block-Reward");
        } else {
            messages.ConsoleMessage("modules.disable", "%module%", "Block-Reward");
        }
    }

}
