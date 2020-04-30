package br.com.eterniaserver.modules;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class CommandsManager {
    public CommandsManager(EterniaServer plugin, Messages messages) {
        if (plugin.serverConfig.getBoolean("modules.commands")) {
            File commandsConfigFile = new File(plugin.getDataFolder(), "commands.yml");
            if (!commandsConfigFile.exists()) {
                plugin.saveResource("commands.yml", false);
            }
            plugin.cmdConfig = new YamlConfiguration();
            try {
                plugin.cmdConfig.load(commandsConfigFile);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
            messages.ConsoleMessage("modules.enable", "%module%", "Custom-Commands");
        } else {
            messages.ConsoleMessage("modules.disable", "%module%", "Custom-Commands");
        }
    }

}
