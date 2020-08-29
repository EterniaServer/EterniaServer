package br.com.eterniaserver.eterniaserver;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.strings.Constants;
import br.com.eterniaserver.eterniaserver.strings.Strings;
import br.com.eterniaserver.eterniaserver.generics.*;

import org.bukkit.Bukkit;

public class Managers {

    private final EterniaServer plugin;

    public Managers(EterniaServer plugin) {

        this.plugin = plugin;

        loadGenericManager();
        loadBedManager();
        loadBlockRewardsManager();
        loadCashManager();
        loadCommandsManager();
        loadChatManager();
        loadEconomyManager();
        loadElevatorManager();
        loadExperienceManager();
        loadHomesManager();
        loadPlayerChecks();
        loadKitManager();
        loadRewardsManager();
        loadSpawnersManager();
        loadTeleportsManager();

    }

    private void loadBedManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.bed"), "Bed")) {
            plugin.getServer().getScheduler().runTaskTimer(plugin, new AccelerateWorld(plugin), 0L, (long) EterniaServer.serverConfig.getInt(Constants.SERVER_CHECKS) * 40);
            plugin.getServer().getPluginManager().registerEvents(new EventPlayerBedEnter(), plugin);
            plugin.getServer().getPluginManager().registerEvents(new EventPlayerBedLeave(), plugin);
        }
    }

    private void loadBlockRewardsManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.block-reward"), "Block-Reward")) {
            plugin.getFiles().loadBlocksRewards();
        }
    }

    private void loadCommandsManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.commands"), "Commands")) {
            plugin.getFiles().loadCommands();
        }
    }

    private void loadCashManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.cash"), "Cash")) {
            plugin.getFiles().loadCashGui();
            EterniaLib.getManager().registerCommand(new BaseCmdCash());
        }
    }

    private void loadChatManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.chat"), "Chat")) {
            plugin.getFiles().loadChat();
            EterniaLib.getManager().registerCommand(new BaseCmdChannels());
            EterniaLib.getManager().registerCommand(new BaseCmdMute());
            EterniaLib.getManager().registerCommand(new BaseCmdChat(plugin));
            new AdvancedChatTorch();
        }
    }

    private void loadEconomyManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.economy"), "Economy")) {
            EterniaLib.getManager().registerCommand(new BaseCmdEconomy());
        }
    }

    private void loadElevatorManager() {
        sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.elevator"), "Elevator");
    }

    private void loadExperienceManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.experience"), "Experience")) {
            EterniaLib.getManager().registerCommand(new BaseCmdExperience());
        }
    }

    private void loadGenericManager() {
        sendModuleStatus(true, "Generic");
        EterniaLib.getManager().registerCommand(new BaseCmdGamemode());
        EterniaLib.getManager().registerCommand(new BaseCmdInventory());
        EterniaLib.getManager().registerCommand(new BaseCmdItem());
        EterniaLib.getManager().registerCommand(new BaseCmdGeneric(plugin));
    }

    private void loadHomesManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.home"), "Homes")) {
            EterniaLib.getManager().registerCommand(new BaseCmdHome());
        }
    }

    private void loadKitManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.kits"), "Kits")) {
            plugin.getFiles().loadKits();
            EterniaLib.getManager().registerCommand(new KitSystem());
        }
    }

    private void loadPlayerChecks() {
        sendModuleStatus(true, "PlayerChecks");
        if (EterniaServer.serverConfig.getBoolean("server.async-check")) {
            new Checks(plugin).runTaskTimerAsynchronously(plugin, 20L, (long) EterniaServer.serverConfig.getInt(Constants.SERVER_CHECKS) * 20);
            return;
        }
        new Checks(plugin).runTaskTimer(plugin, 20L, (long) EterniaServer.serverConfig.getInt(Constants.SERVER_CHECKS) * 20);
    }

    private void loadRewardsManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.rewards"), "Rewards")) {
            plugin.getFiles().loadRewards();
            EterniaLib.getManager().registerCommand(new BaseCmdRewards());
        }
    }

    private void loadSpawnersManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.spawners"), "Spawners")) {
            EterniaLib.getManager().registerCommand(new BaseCmdSpawner());
        }
    }

    private void loadTeleportsManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.teleports"), "Teleports")) {
            EterniaLib.getManager().registerCommand(new BaseCmdWarp());
            EterniaLib.getManager().registerCommand(new BaseCmdTeleport());
        }
    }

    private boolean sendModuleStatus(final boolean enable, final String module) {
        if (enable) {
            Bukkit.getConsoleSender().sendMessage(Strings.MSG_MODULE_ENABLE.replace(Constants.MODULE, module));
        }
        else {
            Bukkit.getConsoleSender().sendMessage(Strings.MSG_MODULE_DISABLE.replace(Constants.MODULE, module));
        }
        return enable;
    }

}
