package br.com.eterniaserver.eterniaserver.configurations;

import br.com.eterniaserver.eterniaserver.core.APIServer;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.objects.CustomCommand;
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

    private static final String COMMANDS_FILE_PATH = Constants.DATA_LAYER_FOLDER_PATH + File.separator + "commands.yml";

    public final Map<String, CustomCommand> customCommandMap = new HashMap<>();

    public CommandsCfg() {

        FileConfiguration commandsConfig = YamlConfiguration.loadConfiguration(new File(COMMANDS_FILE_PATH));
        FileConfiguration outCommands = new YamlConfiguration();

        this.customCommandMap.put("discord", new CustomCommand("Informa o link do discord", new ArrayList<>(), new ArrayList<>(), List.of("&8[&aE&9S&8] &7Entre em nosso discord&8: &3https://discord.gg/Qs3RxMq&8.")));
        this.customCommandMap.put("facebook", new CustomCommand("Informa o link do facebook", new ArrayList<>(), new ArrayList<>(), List.of("&8[&aE&9S&8] &7Entre em nosso facebook&8: &3https://facebook.com/eterniaserver&8.")));

        Map<String, CustomCommand> tempCustomCommandMap = new HashMap<>();
        ConfigurationSection configurationSection = commandsConfig.getConfigurationSection("commands");
        if (configurationSection != null) {
            for (String key : configurationSection.getKeys(false)) {
                tempCustomCommandMap.put(key, new CustomCommand(commandsConfig.getString("commands." + key + ".description"),
                        commandsConfig.getStringList("commands." + key + ".aliases"),
                        commandsConfig.getStringList("commands." + key + ".command"),
                        commandsConfig.getStringList("commands." + key + ".text")));
            }
        }

        if (tempCustomCommandMap.isEmpty()) {
            tempCustomCommandMap = new HashMap<>(this.customCommandMap);
        }

        this.customCommandMap.clear();
        tempCustomCommandMap.forEach(this.customCommandMap::put);

        tempCustomCommandMap.forEach((k, v) -> {
            outCommands.set("commands." + k + ".description", v.getDescription());
            outCommands.set("commands." + k + ".aliases", v.getAliases());
            outCommands.set("commands." + k + ".command", v.getCommands());
            outCommands.set("commands." + k + ".text", v.getText());
        });

        outCommands.options().header("Caso precise de ajuda acesse https://github.com/EterniaServer/EterniaServer/wiki");

        try {
            outCommands.save(COMMANDS_FILE_PATH);
        } catch (IOException exception) {
            APIServer.logError("Impossível de criar arquivos em " + Constants.DATA_LAYER_FOLDER_PATH, 3);
        }

    }

}
