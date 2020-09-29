package br.com.eterniaserver.eterniaserver.dependencies.eternialib;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.dependencies.papi.PlaceHolders;
import br.com.eterniaserver.eterniaserver.core.APIServer;
import br.com.eterniaserver.eterniaserver.objects.CustomCommands;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Files {

    private final EterniaServer plugin;

    public Files(EterniaServer plugin) {
        this.plugin = plugin;
    }

    public void loadChat() {
        loadFile("chat.yml", EterniaServer.chatConfig);
        loadFile("groups.yml", EterniaServer.groupConfig);
        loadFile("customplaceholders.yml", EterniaServer.placeholderConfig);
    }

    public void loadRewards() {
        loadFile("rewards.yml", EterniaServer.rewardsConfig);
    }

    public void loadKits() {
        loadFile("kits.yml", EterniaServer.kitConfig);
    }

    public void loadCommands() {
        loadFile("commands.yml", EterniaServer.cmdConfig);
        for (String keys : EterniaServer.cmdConfig.getConfigurationSection("commands").getKeys(false)) {
            final String commandKey = "commands." + keys;
            List<String> aliasesString = EterniaServer.cmdConfig.getStringList(commandKey + ".aliases");
            List<String> commandsString = EterniaServer.cmdConfig.getStringList(commandKey + ".command");
            String description = EterniaServer.cmdConfig.getString(commandKey + ".description");
            List<String> messagesString = EterniaServer.cmdConfig.getStringList(commandKey + ".text");
            new CustomCommands(plugin, keys, description, aliasesString, messagesString, commandsString);
        }
    }

    public void loadSchedules() {
        loadFile("schedule.yml", EterniaServer.scheduleConfig);
    }

    public void loadDatabase() {
        new Table();
    }

    public void loadPlaceHolders() {
        new PlaceHolders().register();
    }

    private void loadFile(String fileName, YamlConfiguration yamlConfiguration) {

        final File file = new File(plugin.getDataFolder(), fileName);

        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }

        if (!file.canRead()) {
            plugin.getServer().getConsoleSender().sendMessage(
                    APIServer.getColor("&8[&aE&9S&8] &7A jar do EterniaServer não possui o arquivo necessário&8: &3" + fileName + "&8."));
        } else try {
            yamlConfiguration.load(file);
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }

    }

}
