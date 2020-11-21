package br.com.eterniaserver.eterniaserver.configurations.configs;

import br.com.eterniaserver.eterniaserver.core.APIServer;
import br.com.eterniaserver.eterniaserver.Constants;
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

public class CommandsCfg {

    public CommandsCfg(Map<String, CommandData> customCommandMap) {

        customCommandMap.clear();

        FileConfiguration commandsConfig = YamlConfiguration.loadConfiguration(new File(Constants.COMMANDS_FILE_PATH));
        FileConfiguration outCommands = new YamlConfiguration();

        customCommandMap.put("discord", new CommandData("Informa o link do discord", new ArrayList<>(), new ArrayList<>(), List.of("&8[&aE&9S&8] &7Entre em nosso discord&8: &3https://discord.gg/Qs3RxMq&8."), false));
        customCommandMap.put("facebook", new CommandData("Informa o link do facebook", new ArrayList<>(), new ArrayList<>(), List.of("&8[&aE&9S&8] &7Entre em nosso facebook&8: &3https://facebook.com/eterniaserver&8."), false));

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
            tempCustomCommandMap = new HashMap<>(customCommandMap);
        }

        customCommandMap.clear();
        tempCustomCommandMap.forEach(customCommandMap::put);

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
            APIServer.logError("Impossível de criar arquivos em " + Constants.DATA_LAYER_FOLDER_PATH, 3);
        }

    }

}
