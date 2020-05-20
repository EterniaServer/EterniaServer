package br.com.eterniaserver.eterniaserver.storages;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.storages.database.Connections;
import br.com.eterniaserver.eterniaserver.storages.database.Table;

import co.aikar.commands.PaperCommandManager;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class Files {

    private final EterniaServer plugin;
    private final Messages messages;
    private final PaperCommandManager manager;

    public Files(EterniaServer plugin, Messages messages, PaperCommandManager manager) {
        this.plugin = plugin;
        this.messages = messages;
        this.manager = manager;
    }

    public void loadConfigs() {

        File messagesConfigFile = new File(plugin.getDataFolder(), "config.yml");
        File acfmsg = new File(plugin.getDataFolder(), "acf_messages.yml");

        if (!messagesConfigFile.exists()) plugin.saveResource("config.yml", false);
        if (!acfmsg.exists()) plugin.saveResource("acf_messages.yml", false);

        plugin.serverConfig = new YamlConfiguration();
        try {
            manager.getLocales().loadYamlLanguageFile("acf_messages.yml", Locale.ENGLISH);
            plugin.serverConfig.load(messagesConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

    public void loadMessages() {

        File messagesConfigFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!messagesConfigFile.exists()) plugin.saveResource("messages.yml", false);

        plugin.msgConfig = new YamlConfiguration();
        try {
            plugin.msgConfig.load(messagesConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

    public void loadChat() {
        File chatFile = new File(plugin.getDataFolder(), "chat.yml");
        File fileGroups = new File(plugin.getDataFolder(), "groups.yml");
        File placeholdersFile = new File(plugin.getDataFolder(), "customplaceholders.yml");

        if (!chatFile.exists()) plugin.saveResource("chat.yml", false);
        if (!fileGroups.exists()) plugin.saveResource("groups.yml", false);
        if (!placeholdersFile.exists()) plugin.saveResource("customplaceholders.yml", false);


        plugin.chatConfig = new YamlConfiguration();
        plugin.groupConfig = new YamlConfiguration();
        plugin.placeholderConfig = new YamlConfiguration();
        try {
            plugin.chatConfig.load(chatFile);
            plugin.groupConfig.load(fileGroups);
            plugin.placeholderConfig.load(placeholdersFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void loadDatabase() {

        plugin.connections = new Connections(plugin, plugin.serverConfig.getBoolean("sql.mysql"), messages);
        new Table(plugin);

    }

}
