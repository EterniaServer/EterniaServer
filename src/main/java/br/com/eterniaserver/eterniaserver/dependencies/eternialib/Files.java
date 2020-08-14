package br.com.eterniaserver.eterniaserver.dependencies.eternialib;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Configs;
import br.com.eterniaserver.eterniaserver.configs.Strings;
import br.com.eterniaserver.eterniaserver.generics.CustomCommands;

import org.bukkit.configuration.InvalidConfigurationException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Files {

    private final EterniaServer plugin;

    public Files(EterniaServer plugin) {
        this.plugin = plugin;
    }

    public void loadConfigs() {
        final String config = "config.yml";

        final File file = new File(plugin.getDataFolder(), config);
        if (!file.exists()) plugin.saveResource(config, false);

        try {
            EterniaServer.serverConfig.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            errorInJar(config);
        }

    }

    public void loadMessages() {
        final String messages = "messages.yml";

        final File file = new File(plugin.getDataFolder(), messages);
        if (!file.exists()) plugin.saveResource(messages, false);

        try {
            EterniaServer.msgConfig.load(file);
            Strings.reloadMessages(EterniaServer.msgConfig);
        } catch (IOException | InvalidConfigurationException e) {
            errorInJar(messages);
        }

    }

    public void loadChat() {

        final String chat = "chat.yml";
        final String groups = "groups.yml";
        final String placeholder = "customplaceholders.yml";

        final File chatFile = new File(plugin.getDataFolder(), chat);
        final File groupsFile = new File(plugin.getDataFolder(), groups);
        final File placeholderFile = new File(plugin.getDataFolder(), placeholder);

        if (!chatFile.exists()) plugin.saveResource(chat, false);
        if (!groupsFile.exists()) plugin.saveResource(groups, false);
        if (!placeholderFile.exists()) plugin.saveResource(placeholder, false);

        try {
            EterniaServer.chatConfig.load(chatFile);
            EterniaServer.groupConfig.load(groupsFile);
            EterniaServer.placeholderConfig.load(placeholderFile);
        } catch (IOException | InvalidConfigurationException e) {
            errorInJar(chat);
            errorInJar(groups);
            errorInJar(placeholder);
        }

    }

    public void loadRewards() {

        final String rewards = "rewards.yml";

        final File file = new File(plugin.getDataFolder(), rewards);
        if (!file.exists()) plugin.saveResource(rewards, false);

        try {
            EterniaServer.rewardsConfig.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            errorInJar(rewards);
        }

    }

    public void loadKits() {

        final String kits = "kits.yml";

        final File file = new File(plugin.getDataFolder(), kits);
        if (!file.exists()) plugin.saveResource(kits, false);

        try {
            EterniaServer.kitConfig.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            errorInJar(kits);
        }

    }

    public void loadCommands() {

        final String commands = "commands.yml";

        final File file = new File(plugin.getDataFolder(), commands);
        if (!file.exists()) plugin.saveResource(commands, false);

        try {
            EterniaServer.cmdConfig.load(file);
            for (String keys : EterniaServer.cmdConfig.getConfigurationSection("commands").getKeys(false)) {
                final String commandKey = "commands." + keys;
                List<String> aliasesString = EterniaServer.cmdConfig.getStringList(commandKey + ".aliases");
                List<String> commandsString = EterniaServer.cmdConfig.getStringList(commandKey + ".command");
                String description = EterniaServer.cmdConfig.getString(commandKey + ".description");
                List<String> messagesString = EterniaServer.cmdConfig.getStringList(commandKey + ".text");
                new CustomCommands(plugin, keys, description, aliasesString, messagesString, commandsString);
            }
        } catch (IOException | InvalidConfigurationException e) {
            errorInJar(commands);
        }

    }

    public void loadBlocksRewards() {

        final String blocks = "blocks.yml";

        final File file = new File(plugin.getDataFolder(), blocks);
        if (!file.exists()) plugin.saveResource(blocks, false);

        try {
            EterniaServer.blockConfig.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            errorInJar(blocks);
        }

    }

    public void loadCashGui() {

        final String cashGui = "cashgui.yml";

        final File file = new File(plugin.getDataFolder(), cashGui);
        if (!file.exists()) plugin.saveResource(cashGui, false);

        try {
            EterniaServer.cashConfig.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            errorInJar(cashGui);
        }

    }

    public void loadConfigurations() {
        Configs.reloadConfigs();
    }

    public void loadDatabase() {
        new Table();
    }

    private void sendConsoleMessage(final String msg) {
        plugin.getServer().getConsoleSender().sendMessage(Strings.getColor(msg));
    }

    private void errorInJar(final String arq) {
        sendConsoleMessage("&8[&aE&9S&8] &7A jar do EterniaServer não possui o arquivo necessário&8: &3" + arq + "&8.");
    }

}
