package br.com.eterniaserver.eterniaserver.configurations.configs;

import br.com.eterniaserver.eternialib.core.enums.ConfigurationCategory;
import br.com.eterniaserver.eternialib.core.interfaces.ReloadableConfiguration;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.objects.CommandData;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandsCfg implements ReloadableConfiguration {

    private final EterniaServer plugin;

    public CommandsCfg(final EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public ConfigurationCategory category() {
        return ConfigurationCategory.GENERIC;
    }

    @Override
    public void executeConfig() {
        plugin.customCommandMap.clear();

        FileConfiguration commandsConfig = YamlConfiguration.loadConfiguration(new File(Constants.COMMANDS_FILE_PATH));
        FileConfiguration outCommands = new YamlConfiguration();

        plugin.customCommandMap.put("discord", new CommandData("Informa o link do discord", new ArrayList<>(), new ArrayList<>(), List.of("&8[&aE&9S&8] &7Entre em nosso discord&8: &3https://discord.gg/Qs3RxMq&8."), false));
        plugin.customCommandMap.put("facebook", new CommandData("Informa o link do facebook", new ArrayList<>(), new ArrayList<>(), List.of("&8[&aE&9S&8] &7Entre em nosso facebook&8: &3https://facebook.com/eterniaserver&8."), false));

        Map<String, CommandData> tempCustomCommandMap = new HashMap<>();
        ConfigurationSection configurationSection = commandsConfig.getConfigurationSection("commands");
        if (configurationSection != null) {
            for (String key : configurationSection.getKeys(false)) {
                tempCustomCommandMap.put(key, new CommandData(commandsConfig.getString("commands." + key + ".description"),
                        commandsConfig.getStringList("commands." + key + ".aliases"),
                        commandsConfig.getStringList("commands." + key + ".command"),
                        commandsConfig.getStringList("commands." + key + ".text"),
                        commandsConfig.getBoolean("commands." + key + ".console")));
            }
        }

        if (tempCustomCommandMap.isEmpty()) {
            tempCustomCommandMap = new HashMap<>(plugin.customCommandMap);
        }

        plugin.customCommandMap.clear();
        tempCustomCommandMap.forEach(plugin.customCommandMap::put);

        tempCustomCommandMap.forEach((k, v) -> {
            outCommands.set("commands." + k + ".description", v.getDescription());
            outCommands.set("commands." + k + ".aliases", v.getAliases());
            outCommands.set("commands." + k + ".command", v.getCommands());
            outCommands.set("commands." + k + ".text", v.getText());
            outCommands.set("commands." + k + ".console", v.getConsole());
        });

        outCommands.options().header("Caso precise de ajuda acesse https://github.com/EterniaServer/EterniaServer/wiki");

        try {
            outCommands.save(Constants.COMMANDS_FILE_PATH);
        } catch (IOException exception) {
            plugin.logError("Impossível de criar arquivos em " + Constants.DATA_LAYER_FOLDER_PATH, 3);
        }
    }

    @Override
    public void executeCritical() {

    }
}
