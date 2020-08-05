package br.com.eterniaserver.eterniaserver.dependencies.eternialib;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.Strings;
import br.com.eterniaserver.eterniaserver.generics.CustomCommands;
import br.com.eterniaserver.eterniaserver.objects.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.utils.StringHelper;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Files {

    private final EterniaServer plugin;
    private final StringHelper stringHelper;

    public Files(EterniaServer plugin) {
        this.plugin = plugin;
        this.stringHelper = new StringHelper();
    }

    public void loadConfigs() {

        final String config = "config.yml";

        try {
            EterniaServer.serverConfig.load(EFiles.fileLoad(plugin, config));
        } catch (IOException | InvalidConfigurationException e) {
            errorInJar(config);
        }

    }

    public void loadMessages() {

        final String messages = "messages.yml";

        try {
            EterniaServer.msgConfig.load(EFiles.fileLoad(plugin, messages));
        } catch (IOException | InvalidConfigurationException e) {
            errorInJar(messages);
        }

    }

    public void loadChat() {

        try {
            EterniaServer.chatConfig.load(EFiles.fileLoad(plugin, "chat.yml"));
            EterniaServer.groupConfig.load(EFiles.fileLoad(plugin, "groups.yml"));
            EterniaServer.placeholderConfig.load(EFiles.fileLoad(plugin, "customplaceholders.yml"));
        } catch (IOException | InvalidConfigurationException e) {
            errorInJar("(chat|groups|customplaceholders).yml");
        }

    }

    public void loadRewards() {

        final String rewards = "rewards.yml";

        try {
            EterniaServer.rewardsConfig.load(EFiles.fileLoad(plugin, rewards));
        } catch (IOException | InvalidConfigurationException e) {
            errorInJar(rewards);
        }

    }

    public void loadKits() {

        final String kits = "kits.yml";

        try {
            EterniaServer.kitConfig.load(EFiles.fileLoad(plugin, kits));
        } catch (IOException | InvalidConfigurationException e) {
            errorInJar(kits);
        }

    }

    public void loadCommands() {

        final String commands = "commands.yml";

        try {
            EterniaServer.cmdConfig.load(EFiles.fileLoad(plugin, commands));
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

        try {
            EterniaServer.blockConfig.load(EFiles.fileLoad(plugin, blocks));
        } catch (IOException | InvalidConfigurationException e) {
            errorInJar(blocks);
        }

    }

    public void loadCashGui() {

        final String cashGui = "cashgui.yml";

        try {
            EterniaServer.cashConfig.load(EFiles.fileLoad(plugin, cashGui));
        } catch (IOException | InvalidConfigurationException e) {
            errorInJar(cashGui);
        }

    }

    public void loadDatabase() {
        new Table();
    }

    private void sendConsoleMessage(final String msg) {
        plugin.getServer().getConsoleSender().sendMessage(stringHelper.cc(msg));
    }

    private void errorInJar(final String arq) {
        sendConsoleMessage("&8[&aE&9S&8] &7A jar do EterniaServer não possui o arquivo necessário&8: &3" + arq + "&8.");
    }

}
