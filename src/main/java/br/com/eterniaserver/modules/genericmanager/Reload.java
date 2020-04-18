package br.com.eterniaserver.modules.genericmanager;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.storages.sql.Connections;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Reload {
    public Reload(EterniaServer plugin) {
        EterniaServer.connection.Close();

        File config = new File(plugin.getDataFolder(), "config.yml");
        if (!config.exists()) {
            plugin.saveResource("config.yml", false);
        }
        EterniaServer.configs = new YamlConfiguration();
        try {
            EterniaServer.configs.load(config);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        if (EterniaServer.configs.getBoolean("modules.commands")) {
            File commandsConfigFile = new File(plugin.getDataFolder(), "commands.yml");
            if (!commandsConfigFile.exists()) {
                plugin.saveResource("commands.yml", false);
            }
            EterniaServer.commands = new YamlConfiguration();
            try {
                EterniaServer.commands.load(commandsConfigFile);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }

        File messagesConfigFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!messagesConfigFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        EterniaServer.messages = new YamlConfiguration();
        try {
            EterniaServer.messages.load(messagesConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

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
        }

        EterniaServer.connection = new Connections(plugin, EterniaServer.configs.getBoolean("sql.mysql"));
    }
}
