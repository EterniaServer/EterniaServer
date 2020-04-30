package br.com.eterniaserver.modules.genericmanager;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.storages.sql.Connections;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Reload {

    private final EterniaServer plugin;
    private final Messages messages;

    public Reload(EterniaServer plugin, Messages messages) {
        this.plugin = plugin;
        this.messages = messages;
    }

    public void reload() {
        plugin.connections.Close();

        File config = new File(plugin.getDataFolder(), "config.yml");
        if (!config.exists()) {
            plugin.saveResource("config.yml", false);
        }
        plugin.serverConfig = new YamlConfiguration();
        try {
            plugin.serverConfig.load(config);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        if (plugin.serverConfig.getBoolean("modules.commands")) {
            File commandsConfigFile = new File(plugin.getDataFolder(), "commands.yml");
            if (!commandsConfigFile.exists()) {
                plugin.saveResource("commands.yml", false);
            }
            plugin.cmdConfig = new YamlConfiguration();
            try {
                plugin.cmdConfig.load(commandsConfigFile);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }

        File messagesConfigFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!messagesConfigFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        plugin.msgConfig = new YamlConfiguration();
        try {
            plugin.msgConfig.load(messagesConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        if (plugin.serverConfig.getBoolean("modules.block-reward")) {
            File blocksFile = new File(plugin.getDataFolder(), "blocks.yml");
            if (!blocksFile.exists()) {
                plugin.saveResource("blocks.yml", false);
            }
            plugin.blockConfig = new YamlConfiguration();
            try {
                plugin.blockConfig.load(blocksFile);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }

        if (plugin.blockConfig.getBoolean("modules.chat")) {
            File chatFile = new File(plugin.getDataFolder(), "chat.yml");
            if (!chatFile.exists()) {
                plugin.saveResource("chat.yml", false);
            }
            plugin.chatConfig = new YamlConfiguration();
            try {
                plugin.chatConfig.load(chatFile);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }

            File fileGroups = new File(plugin.getDataFolder(), "groups.yml");
            if (!fileGroups.exists()) {
                plugin.saveResource("groups.yml", false);
            }
            plugin.groupConfig = new YamlConfiguration();
            try {
                plugin.groupConfig.load(fileGroups);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }

            File placeholdersFile = new File(plugin.getDataFolder(), "customplaceholders.yml");
            if (!placeholdersFile.exists()) {
                plugin.saveResource("customplaceholders.yml", false);
            }
            plugin.placeholderConfig = new YamlConfiguration();
            try {
                plugin.placeholderConfig.load(placeholdersFile);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
        if (plugin.serverConfig.getBoolean("modules.kits")) {
            File commandsConfigFile = new File(plugin.getDataFolder(), "kits.yml");
            if (!commandsConfigFile.exists()) {
                plugin.saveResource("kits.yml", false);
            }
            plugin.kitConfig = new YamlConfiguration();
            try {
                plugin.kitConfig.load(commandsConfigFile);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }

        plugin.connections = new Connections(plugin, plugin.serverConfig.getBoolean("sql.mysql"), messages);
    }
}
