package br.com.eterniaserver.eterniaserver;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.generics.OnPlayerBedEnter;
import br.com.eterniaserver.eterniaserver.generics.OnPlayerBedLeave;
import br.com.eterniaserver.eterniaserver.generics.ChatCommands;
import br.com.eterniaserver.eterniaserver.generics.Economy;
import br.com.eterniaserver.eterniaserver.generics.Experience;
import br.com.eterniaserver.eterniaserver.generics.*;
import br.com.eterniaserver.eterniaserver.generics.KitSystem;
import br.com.eterniaserver.eterniaserver.generics.RewardsSystem;
import br.com.eterniaserver.eterniaserver.generics.AccelerateWorld;
import br.com.eterniaserver.eterniaserver.generics.AdvancedChatTorch;
import br.com.eterniaserver.eterniaserver.generics.Channels;
import br.com.eterniaserver.eterniaserver.generics.Mute;
import br.com.eterniaserver.eterniaserver.generics.Checks;

import co.aikar.commands.PaperCommandManager;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Managers {

    private final EterniaServer plugin;
    private final EFiles messages;
    private final PaperCommandManager manager;

    public Managers(EterniaServer plugin) {

        this.messages = plugin.getEFiles();
        this.manager = plugin.getManager();
        this.plugin = plugin;

        if (plugin.serverConfig.getBoolean("modules.bed")) {
            plugin.getServer().getScheduler().runTaskTimer(plugin, new AccelerateWorld(plugin), 0L, (long) plugin.serverConfig.getInt("server.checks") * 40);
            plugin.getServer().getPluginManager().registerEvents(new OnPlayerBedEnter(plugin), plugin);
            plugin.getServer().getPluginManager().registerEvents(new OnPlayerBedLeave(plugin), plugin);
            messages.sendConsole("modules.enable", "%module%", "Bed");
        } else {
            messages.sendConsole("modules.disable", "%module%", "Bed");
        }

        if (plugin.serverConfig.getBoolean("modules.block-reward")) {
            plugin.blockConfig = new YamlConfiguration();
            try {
                plugin.blockConfig.load(EFiles.fileLoad(plugin, "blocks.yml"));
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
            messages.sendConsole("modules.enable", "%module%", "Block-Reward");
        } else {
            messages.sendConsole("modules.disable", "%module%", "Block-Reward");
        }

        if (plugin.serverConfig.getBoolean("modules.chat")) {
            plugin.files.loadChat();
        }

        if (plugin.serverConfig.getBoolean("modules.commands")) {
            plugin.cmdConfig = new YamlConfiguration();
            try {
                plugin.cmdConfig.load(EFiles.fileLoad(plugin, "commands.yml"));
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
            messages.sendConsole("modules.enable", "%module%", "Custom-Commands");
        } else {
            messages.sendConsole("modules.disable", "%module%", "Custom-Commands");
        }

        if (plugin.serverConfig.getBoolean("modules.kits")) {
            File commandsConfigFile = new File(plugin.getDataFolder(), "kits.yml");
            if (!commandsConfigFile.exists()) {
                plugin.saveResource("kits.yml", false);
            }
            plugin.kitConfig = new YamlConfiguration();
            try {
                plugin.kitConfig.load(commandsConfigFile);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }

        if (plugin.serverConfig.getBoolean("modules.rewards")) {
            File rwConfig = new File(plugin.getDataFolder(), "rewards.yml");
            if (!rwConfig.exists()) plugin.saveResource("rewards.yml", false);
            plugin.rewardsConfig = new YamlConfiguration();
            try {
                plugin.rewardsConfig.load(rwConfig);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }

        loadChatManager();
        loadEconomyManager();
        loadElevatorManager();
        loadExperienceManager();
        loadGenericManager();
        loadHomesManager();
        loadKitManager();
        loadPlayerChecks();
        loadRewardsManager();
        loadSpawnersManager();
        loadTeleportsManager();

    }

    private void loadChatManager() {
        if (sendModuleStatus(plugin.serverConfig.getBoolean("modules.chat"), "Chat")) {
            manager.registerCommand(new Channels(plugin));
            manager.registerCommand(new Mute(plugin));
            manager.registerCommand(new ChatCommands(plugin));
            new AdvancedChatTorch(plugin);
        }
    }

    private void loadEconomyManager() {
        if (sendModuleStatus(plugin.serverConfig.getBoolean("modules.economy"), "Economy")) {
            manager.registerCommand(new Economy(plugin));
            manager.registerCommand(new EcoChange(plugin));
        }
    }

    private void loadElevatorManager() {
        sendModuleStatus(plugin.serverConfig.getBoolean("modules.elevator"), "Elevator");
    }

    private void loadExperienceManager() {
        if (sendModuleStatus(plugin.serverConfig.getBoolean("modules.experience"), "Experience")) {
            manager.registerCommand(new Experience(plugin));
        }
    }

    private void loadGenericManager() {
        if (sendModuleStatus(plugin.serverConfig.getBoolean("modules.generic"), "Generic")) {
            manager.registerCommand(new Gamemode(messages));
            manager.registerCommand(new Inventory(messages));
            manager.registerCommand(new Others(plugin));
            manager.registerCommand(new Replaces(plugin));
            manager.registerCommand(new Simplifications(messages));
        }
    }

    private void loadHomesManager() {
        if (sendModuleStatus(plugin.serverConfig.getBoolean("modules.home"), "Homes")) {
            manager.registerCommand(new HomeSystem(plugin));
        }
    }

    private void loadKitManager() {
        if (sendModuleStatus(plugin.serverConfig.getBoolean("modules.kits"), "Kits")) {
            manager.registerCommand(new KitSystem(plugin));
        }
    }

    private void loadPlayerChecks() {
        if (sendModuleStatus(plugin.serverConfig.getBoolean("modules.playerchecks"), "PlayerChecks")) {
            new Checks(plugin).runTaskTimer(plugin, 20L, (long) plugin.serverConfig.getInt("server.checks") * 20);
        }
    }

    private void loadRewardsManager() {
        if (sendModuleStatus(plugin.serverConfig.getBoolean("modules.rewards"), "Rewards")) {
            manager.registerCommand(new RewardsSystem(plugin));
        }
    }

    private void loadSpawnersManager() {
        if (sendModuleStatus(plugin.serverConfig.getBoolean("modules.spawners"), "Spawners")) {
            manager.registerCommand(new SpawnerGive(plugin));
        }
    }

    private void loadTeleportsManager() {
        if (sendModuleStatus(plugin.serverConfig.getBoolean("modules.teleports"), "Teleports")) {
            manager.registerCommand(new WarpSystem(plugin));
            manager.registerCommand(new TeleportSystem(plugin));
        }
    }

    private boolean sendModuleStatus(final boolean enable, final String module) {
        if (enable) messages.sendConsole("modules.enable", "%module%", module);
        else messages.sendConsole("modules.disable", "%module%", module);
        return enable;
    }

}
