package br.com.eterniaserver.eterniaserver.dependencies.eternialib;

import br.com.eterniaserver.eternialib.configs.FileCreator;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import co.aikar.commands.PaperCommandManager;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.Locale;

public class Files {

    private final EterniaServer plugin;
    private final PaperCommandManager manager;

    public Files(EterniaServer plugin, PaperCommandManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    public void loadConfigs() {

        plugin.serverConfig = new YamlConfiguration();

        try {
            plugin.serverConfig.load(FileCreator.fileLoad(plugin, "config.yml"));

            FileCreator.fileLoad(plugin, "acf_messages.yml");
            manager.getLocales().loadYamlLanguageFile("acf_messages.yml", Locale.ENGLISH);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

    public void loadMessages() {

        plugin.msgConfig = new YamlConfiguration();

        try {
            plugin.msgConfig.load(FileCreator.fileLoad(plugin, "messages.yml"));
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

    public void loadChat() {

        plugin.chatConfig = new YamlConfiguration();
        plugin.groupConfig = new YamlConfiguration();
        plugin.placeholderConfig = new YamlConfiguration();

        try {
            plugin.chatConfig.load(FileCreator.fileLoad(plugin, "chat.yml"));
            plugin.groupConfig.load(FileCreator.fileLoad(plugin, "groups.yml"));
            plugin.placeholderConfig.load(FileCreator.fileLoad(plugin, "customplaceholders.yml"));
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

    public void loadDatabase() {

        new Table(plugin);

    }

}
