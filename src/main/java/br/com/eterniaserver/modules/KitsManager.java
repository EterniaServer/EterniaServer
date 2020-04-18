package br.com.eterniaserver.modules;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.modules.kitsmanager.commands.*;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class KitsManager {
    public KitsManager(EterniaServer plugin) {
        if (EterniaServer.configs.getBoolean("modules.kits")) {
            File commandsConfigFile = new File(plugin.getDataFolder(), "kits.yml");
            if (!commandsConfigFile.exists()) {
                plugin.saveResource("kits.yml", false);
            }
            EterniaServer.kits = new YamlConfiguration();
            try {
                EterniaServer.kits.load(commandsConfigFile);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
            Objects.requireNonNull(plugin.getCommand("kit")).setExecutor(new Kit(plugin));
            Objects.requireNonNull(plugin.getCommand("kits")).setExecutor(new Kits());
            Messages.ConsoleMessage("modules.enable", "Kits");
        } else {
            Messages.ConsoleMessage("modules.disable", "Kits");
        }
    }
}
