package br.com.eterniaserver.eterniaserver;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.configs.Constants;
import br.com.eterniaserver.eterniaserver.configs.Strings;
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
            plugin.getServer().getScheduler().runTaskTimer(plugin, new AccelerateWorld(plugin), 0L, (long) EterniaServer.serverConfig.getInt("server.checks") * 40);
            plugin.getServer().getPluginManager().registerEvents(new OnPlayerBedEnter(), plugin);
            plugin.getServer().getPluginManager().registerEvents(new OnPlayerBedLeave(), plugin);
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
            EterniaLib.getManager().registerCommand(new Cash());
        }
    }

    private void loadChatManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.chat"), "Chat")) {
            plugin.getFiles().loadChat();
            EterniaLib.getManager().registerCommand(new Channels());
            EterniaLib.getManager().registerCommand(new Mute());
            EterniaLib.getManager().registerCommand(new ChatCommands(plugin));
            new AdvancedChatTorch();
        }
    }

    private void loadEconomyManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.economy"), "Economy")) {
            EterniaLib.getManager().registerCommand(new Economy());
        }
    }

    private void loadElevatorManager() {
        sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.elevator"), "Elevator");
    }

    private void loadExperienceManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.experience"), "Experience")) {
            EterniaLib.getManager().registerCommand(new Experience());
        }
    }

    private void loadGenericManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.generic"), "Generic")) {
            EterniaLib.getManager().registerCommand(new Gamemode());
            EterniaLib.getManager().registerCommand(new Inventory());
            EterniaLib.getManager().registerCommand(new Others(plugin));
            EterniaLib.getManager().registerCommand(new Replaces());
            EterniaLib.getManager().registerCommand(new Simplifications());
            EterniaLib.getManager().registerCommand(new Glow());
        }
    }

    private void loadHomesManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.home"), "Homes")) {
            EterniaLib.getManager().registerCommand(new HomeSystem());
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
        new Checks().runTaskTimer(plugin, 20L, (long) EterniaServer.serverConfig.getInt("server.checks") * 20);
    }

    private void loadRewardsManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.rewards"), "Rewards")) {
            plugin.getFiles().loadRewards();
            EterniaLib.getManager().registerCommand(new RewardsSystem());
        }
    }

    private void loadSpawnersManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.spawners"), "Spawners")) {
            EterniaLib.getManager().registerCommand(new SpawnerGive());
        }
    }

    private void loadTeleportsManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.teleports"), "Teleports")) {
            EterniaLib.getManager().registerCommand(new WarpSystem());
            EterniaLib.getManager().registerCommand(new TeleportSystem());
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
