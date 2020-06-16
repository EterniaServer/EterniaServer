package br.com.eterniaserver.eterniaserver.modules;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.events.OnPlayerBedEnter;
import br.com.eterniaserver.eterniaserver.events.OnPlayerBedLeave;
import br.com.eterniaserver.eterniaserver.modules.commands.*;
import br.com.eterniaserver.eterniaserver.modules.economymanager.commands.Economy;
import br.com.eterniaserver.eterniaserver.modules.experiencemanager.commands.Experience;
import br.com.eterniaserver.eterniaserver.modules.tasks.AccelerateWorld;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.AdvancedChatTorch;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.commands.Channels;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.commands.Mute;
import br.com.eterniaserver.eterniaserver.modules.tasks.Checks;

import co.aikar.commands.PaperCommandManager;

import io.papermc.lib.PaperLib;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;

public class Managers {

    public Managers(EterniaServer plugin) {

        final PaperCommandManager manager = plugin.getManager();
        final EFiles messages = plugin.getEFiles();

        if (plugin.serverConfig.getBoolean("modules.bed")) {
            plugin.getServer().getScheduler().runTaskTimer(plugin, new AccelerateWorld(plugin), 0L, plugin.serverConfig.getInt("server.checks") * 40);
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
            manager.registerCommand(new Channels(plugin));
            manager.registerCommand(new Mute(plugin));
            manager.registerCommand(new br.com.eterniaserver.eterniaserver.modules.chatmanager.commands.Others(plugin));
            new AdvancedChatTorch(plugin);
            messages.sendConsole("modules.enable", "%module%", "Chat");
        } else {
            messages.sendConsole("modules.disable", "%module%", "Chat");
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

        if (plugin.serverConfig.getBoolean("modules.economy")) {
            manager.registerCommand(new Economy(plugin));
            manager.registerCommand(new EcoChange(plugin));
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
            manager.registerCommand(new Experience(plugin));
            messages.sendConsole("modules.enable", "%module%", "Teleports");
        } else {
            messages.sendConsole("modules.disable", "%module%", "Teleports");
        }

        if (plugin.serverConfig.getBoolean("modules.generic")) {
            manager.registerCommand(new Gamemode(messages));
            manager.registerCommand(new Inventory(messages));
            manager.registerCommand(new Others(plugin));
            manager.registerCommand(new Replaces(plugin));
            manager.registerCommand(new Simplifications(messages));
            messages.sendConsole("modules.enable", "%module%", "Generic");
        } else {
            messages.sendConsole("modules.disable", "%module%", "Generic");
        }

        if (plugin.serverConfig.getBoolean("modules.playerchecks")) {
            new Checks(plugin, plugin.teleportsManager).runTaskTimer(plugin, 20L, plugin.serverConfig.getInt("server.checks") * 20);
            messages.sendConsole("modules.enable", "%module%", "Player-Checks");
        } else {
            messages.sendConsole("modules.disable", "%module%", "Player-Checks");
        }

        if (plugin.serverConfig.getBoolean("modules.spawners")) {
            manager.registerCommand(new SpawnerGive(plugin));
            messages.sendConsole("modules.enable", "%module%", "Spawners");
        } else {
            messages.sendConsole("modules.disable", "%module%", "Spawners");
        }

    }

}
