package br.com.eterniaserver.eterniaserver.modules;

import br.com.eterniaserver.eterniaserver.API.Exp;
import br.com.eterniaserver.eterniaserver.API.Money;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Strings;
import br.com.eterniaserver.eterniaserver.configs.Vars;
import br.com.eterniaserver.eterniaserver.dependencies.papi.PlaceHolders;
import br.com.eterniaserver.eterniaserver.events.OnBedEnter;
import br.com.eterniaserver.eterniaserver.events.OnBedLeave;
import br.com.eterniaserver.eterniaserver.modules.commands.*;
import br.com.eterniaserver.eterniaserver.modules.tasks.AccelerateWorld;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.AdvancedChatTorch;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.commands.Channels;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.commands.Mute;
import br.com.eterniaserver.eterniaserver.modules.tasks.Checks;
import br.com.eterniaserver.eterniaserver.modules.teleportsmanager.TeleportsManager;
import br.com.eterniaserver.eterniaserver.player.PlayerFlyState;
import br.com.eterniaserver.eterniaserver.player.PlayerManager;
import br.com.eterniaserver.eterniaserver.storages.Files;

import co.aikar.commands.PaperCommandManager;

import io.papermc.lib.PaperLib;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Managers {

    public Managers(EterniaServer plugin, Messages messages, PaperCommandManager manager,
                    Strings strings, Vars vars, TeleportsManager teleportsManager,
                    Files files, PlaceHolders placeHolders, PlayerFlyState playerFlyState,
                    br.com.eterniaserver.eterniaserver.configs.Checks checks, Exp exp,
                    Money money, PlayerManager playerManager) {

        if (plugin.serverConfig.getBoolean("modules.bed")) {
            if (plugin.serverConfig.getBoolean("server.async-check")) {
                plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new AccelerateWorld(plugin, messages, vars), 0L, plugin.serverConfig.getInt("server.checks") * 40);
            } else {
                plugin.getServer().getScheduler().runTaskTimer(plugin, new AccelerateWorld(plugin, messages, vars), 0L, plugin.serverConfig.getInt("server.checks") * 40);
            }
            plugin.getServer().getPluginManager().registerEvents(new OnBedEnter(messages, checks, vars), plugin);
            plugin.getServer().getPluginManager().registerEvents(new OnBedLeave(checks, vars), plugin);
            messages.sendConsole("modules.enable", "%module%", "Bed");
        } else {
            messages.sendConsole("modules.disable", "%module%", "Bed");
        }

        if (plugin.serverConfig.getBoolean("modules.block-reward")) {
            File blocksFile = new File(plugin.getDataFolder(), "blocks.yml");
            if (!blocksFile.exists()) plugin.saveResource("blocks.yml", false);

            plugin.blockConfig = new YamlConfiguration();
            try {
                plugin.blockConfig.load(blocksFile);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
            messages.sendConsole("modules.enable", "%module%", "Block-Reward");
        } else {
            messages.sendConsole("modules.disable", "%module%", "Block-Reward");
        }

        if (plugin.serverConfig.getBoolean("modules.chat")) {
            files.loadChat();
            manager.registerCommand(new Channels(vars, messages));
            manager.registerCommand(new Mute(plugin, messages, vars, playerManager));
            manager.registerCommand(new br.com.eterniaserver.eterniaserver.modules.chatmanager.commands.Others(messages, strings, vars));
            new AdvancedChatTorch(plugin, messages, vars);
            messages.sendConsole("modules.enable", "%module%", "Chat");
        } else {
            messages.sendConsole("modules.disable", "%module%", "Chat");
        }

        if (plugin.serverConfig.getBoolean("modules.commands")) {
            File commandsConfigFile = new File(plugin.getDataFolder(), "commands.yml");
            if (!commandsConfigFile.exists()) plugin.saveResource("commands.yml", false);

            plugin.cmdConfig = new YamlConfiguration();
            try {
                plugin.cmdConfig.load(commandsConfigFile);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
            messages.sendConsole("modules.enable", "%module%", "Custom-Commands");
        } else {
            messages.sendConsole("modules.disable", "%module%", "Custom-Commands");
        }

        if (plugin.serverConfig.getBoolean("modules.economy")) {
            manager.registerCommand(new Economy(plugin, messages, money));
            manager.registerCommand(new EcoChange(messages, money));
            messages.sendConsole("modules.enable", "%module%", "Economy");
        } else {
            messages.sendConsole("modules.disable", "%module%", "Economy");
        }

        if (plugin.serverConfig.getBoolean("modules.elevator")) {
            messages.sendConsole("modules.enable", "%module%", "Elevator");
        } else {
            messages.sendConsole("modules.disable", "%module%", "Elevator");
        }

        if (plugin.serverConfig.getBoolean("modules.experience")) {
            manager.registerCommand(new Experience(checks, messages, exp));
            messages.sendConsole("modules.enable", "%module%", "Teleports");
        } else {
            messages.sendConsole("modules.disable", "%module%", "Teleports");
        }

        if (plugin.serverConfig.getBoolean("modules.generic")) {
            manager.registerCommand(new Gamemode(messages));
            manager.registerCommand(new Inventory(messages));
            manager.registerCommand(new Others(messages, files, placeHolders, strings, vars, playerFlyState));
            manager.registerCommand(new Replaces(plugin, messages, strings, vars));
            manager.registerCommand(new Simplifications(messages));
            messages.sendConsole("modules.enable", "%module%", "Generic");
        } else {
            messages.sendConsole("modules.disable", "%module%", "Generic");
        }

        if (plugin.serverConfig.getBoolean("modules.playerchecks")) {
            if (PaperLib.isPaper()) {
                if (plugin.serverConfig.getBoolean("server.async-check")) {
                    new Checks(plugin, messages, strings, vars, teleportsManager).runTaskTimerAsynchronously(plugin, 20L, plugin.serverConfig.getInt("server.checks") * 20);
                } else {
                    new Checks(plugin, messages, strings, vars, teleportsManager).runTaskTimer(plugin, 20L, plugin.serverConfig.getInt("server.checks") * 20);
                }
            } else {
                new Checks(plugin, messages, strings, vars, teleportsManager).runTaskTimer(plugin, 20L, plugin.serverConfig.getInt("server.checks") * 20);
            }
            messages.sendConsole("modules.enable", "%module%", "Player-Checks");
        } else {
            messages.sendConsole("modules.disable", "%module%", "Player-Checks");
        }

        if (plugin.serverConfig.getBoolean("modules.spawners")) {
            manager.registerCommand(new SpawnerGive(plugin, messages));
            messages.sendConsole("modules.enable", "%module%", "Spawners");
        } else {
            messages.sendConsole("modules.disable", "%module%", "Spawners");
        }

    }

}
