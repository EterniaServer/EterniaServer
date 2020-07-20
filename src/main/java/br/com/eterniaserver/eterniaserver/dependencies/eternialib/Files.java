package br.com.eterniaserver.eterniaserver.dependencies.eternialib;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import org.bukkit.configuration.InvalidConfigurationException;
import java.io.IOException;
import java.util.Locale;

public class Files {

    private final EterniaServer plugin;

    public Files(EterniaServer plugin) {
        this.plugin = plugin;
    }

    public void loadConfigs() {

        try {
            EterniaServer.serverConfig.load(EFiles.fileLoad(plugin, "config.yml"));

            EFiles.fileLoad(plugin, "acf_messages.yml");
            plugin.getManager().getLocales().loadYamlLanguageFile("acf_messages.yml", Locale.ENGLISH);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

    public void loadMessages() {

        try {
            EterniaServer.msgConfig.load(EFiles.fileLoad(plugin, "messages.yml"));
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

    public void loadChat() {

        try {
            EterniaServer.chatConfig.load(EFiles.fileLoad(plugin, "chat.yml"));
            EterniaServer.groupConfig.load(EFiles.fileLoad(plugin, "groups.yml"));
            EterniaServer.placeholderConfig.load(EFiles.fileLoad(plugin, "customplaceholders.yml"));
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

    public void loadRewards() {

        try {
            EterniaServer.rewardsConfig.load(EFiles.fileLoad(plugin, "rewards.yml"));
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

    public void loadKits() {

        try {
            EterniaServer.kitConfig.load(EFiles.fileLoad(plugin, "kits.yml"));
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

    public void loadCommands() {

        try {
            EterniaServer.cmdConfig.load(EFiles.fileLoad(plugin, "commands.yml"));
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

    public void loadBlocksRewards() {

        try {
            EterniaServer.blockConfig.load(EFiles.fileLoad(plugin, "blocks.yml"));
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

    public void loadCashGui() {

        try {
            EterniaServer.cashConfig.load(EFiles.fileLoad(plugin, "cashgui.yml"));
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

    public void loadDatabase() {

        new Table();

    }

}
