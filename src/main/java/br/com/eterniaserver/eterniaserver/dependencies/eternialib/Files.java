package br.com.eterniaserver.eterniaserver.dependencies.eternialib;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.Locale;

public class Files {

    private final EterniaServer plugin;

    public Files(EterniaServer plugin) {
        this.plugin = plugin;
    }

    public void loadConfigs() {

        plugin.serverConfig = new YamlConfiguration();

        try {
            plugin.serverConfig.load(EFiles.fileLoad(plugin, "config.yml"));

            EFiles.fileLoad(plugin, "acf_messages.yml");
            plugin.getManager().getLocales().loadYamlLanguageFile("acf_messages.yml", Locale.ENGLISH);
        } catch (IOException | InvalidConfigurationException e) {
            // todo
        }

    }

    public void loadMessages() {

        plugin.msgConfig = new YamlConfiguration();

        try {
            plugin.msgConfig.load(EFiles.fileLoad(plugin, "messages.yml"));
        } catch (IOException | InvalidConfigurationException e) {
            // todo
        }

    }

    public void loadChat() {

        plugin.chatConfig = new YamlConfiguration();
        plugin.groupConfig = new YamlConfiguration();
        plugin.placeholderConfig = new YamlConfiguration();

        try {
            plugin.chatConfig.load(EFiles.fileLoad(plugin, "chat.yml"));
            plugin.groupConfig.load(EFiles.fileLoad(plugin, "groups.yml"));
            plugin.placeholderConfig.load(EFiles.fileLoad(plugin, "customplaceholders.yml"));
        } catch (IOException | InvalidConfigurationException e) {
            // todo
        }

    }

    public void loadDatabase() {

        new Table(plugin);

    }

}
