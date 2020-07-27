package br.com.eterniaserver.eterniaserver;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.generics.*;

public class Managers {

    private final EterniaServer plugin;
    private final EFiles messages;

    public Managers(EterniaServer plugin) {

        this.messages = plugin.getEFiles();
        this.plugin = plugin;

        loadBedManager();
        loadBlockRewardsManager();
        loadCashManager();
        loadCommandsManager();
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

    private void loadBedManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.bed"), "Bed")) {
            plugin.getServer().getScheduler().runTaskTimer(plugin, new AccelerateWorld(plugin), 0L, (long) EterniaServer.serverConfig.getInt("server.checks") * 40);
            plugin.getServer().getPluginManager().registerEvents(new OnPlayerBedEnter(plugin), plugin);
            plugin.getServer().getPluginManager().registerEvents(new OnPlayerBedLeave(plugin), plugin);
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
            EterniaLib.getManager().registerCommand(new Cash(plugin));
        }
    }

    private void loadChatManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.chat"), "Chat")) {
            plugin.getFiles().loadChat();
            EterniaLib.getManager().registerCommand(new Channels(plugin));
            EterniaLib.getManager().registerCommand(new Mute(plugin));
            EterniaLib.getManager().registerCommand(new ChatCommands(plugin));
            new AdvancedChatTorch(plugin);
        }
    }

    private void loadEconomyManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.economy"), "Economy")) {
            EterniaLib.getManager().registerCommand(new Economy(plugin));
            EterniaLib.getManager().registerCommand(new EcoChange(plugin));
        }
    }

    private void loadElevatorManager() {
        sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.elevator"), "Elevator");
    }

    private void loadExperienceManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.experience"), "Experience")) {
            EterniaLib.getManager().registerCommand(new Experience(plugin));
        }
    }

    private void loadGenericManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.generic"), "Generic")) {
            EterniaLib.getManager().registerCommand(new Gamemode(messages));
            EterniaLib.getManager().registerCommand(new Inventory(messages));
            EterniaLib.getManager().registerCommand(new Others(plugin));
            EterniaLib.getManager().registerCommand(new Replaces(plugin));
            EterniaLib.getManager().registerCommand(new Simplifications(messages));
            EterniaLib.getManager().registerCommand(new Glow(plugin));
        }
    }

    private void loadHomesManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.home"), "Homes")) {
            EterniaLib.getManager().registerCommand(new HomeSystem(plugin));
        }
    }

    private void loadKitManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.kits"), "Kits")) {
            plugin.getFiles().loadKits();
            EterniaLib.getManager().registerCommand(new KitSystem(plugin));
        }
    }

    private void loadPlayerChecks() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.playerchecks"), "PlayerChecks")) {
            new Checks(plugin).runTaskTimer(plugin, 20L, (long) EterniaServer.serverConfig.getInt("server.checks") * 20);
        }
    }

    private void loadRewardsManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.rewards"), "Rewards")) {
            plugin.getFiles().loadRewards();
            EterniaLib.getManager().registerCommand(new RewardsSystem(plugin));
        }
    }

    private void loadSpawnersManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.spawners"), "Spawners")) {
            EterniaLib.getManager().registerCommand(new SpawnerGive(plugin));
        }
    }

    private void loadTeleportsManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.teleports"), "Teleports")) {
            EterniaLib.getManager().registerCommand(new WarpSystem(plugin));
            EterniaLib.getManager().registerCommand(new TeleportSystem(plugin));
        }
    }

    private boolean sendModuleStatus(final boolean enable, final String module) {
        if (enable) messages.sendConsole("modules.enable", Constants.MODULE, module);
        else messages.sendConsole("modules.disable", Constants.MODULE, module);
        return enable;
    }

}
