package br.com.eterniaserver.modules;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class CommandsManager {
    public CommandsManager(EterniaServer plugin) {
        if (EterniaServer.configs.getBoolean("modules.commands")) {
            File commandsConfigFile = new File(plugin.getDataFolder(), "commands.yml");
            if (!commandsConfigFile.exists()) {
                plugin.saveResource("commands.yml", false);
            }
            EterniaServer.commands = new YamlConfiguration();
            try {
                EterniaServer.commands.load(commandsConfigFile);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
            Messages.ConsoleMessage("modules.enable", "%module%", "Custom-Commands");
        } else {
            Messages.ConsoleMessage("modules.disable", "%module%", "Custom-Commands");
        }
    }

}
