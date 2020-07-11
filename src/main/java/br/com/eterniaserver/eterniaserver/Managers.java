package br.com.eterniaserver.eterniaserver;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.generics.*;

import co.aikar.commands.PaperCommandManager;

public class Managers {

    private final EterniaServer plugin;
    private final EFiles messages;
    private final PaperCommandManager manager;

    public Managers(EterniaServer plugin) {

        this.messages = plugin.getEFiles();
        this.manager = plugin.getManager();
        this.plugin = plugin;

        loadBedManager();
        loadBlockRewardsManager();
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
        if (sendModuleStatus(plugin.serverConfig.getBoolean("modules.bed"), "Bed")) {
            plugin.getServer().getScheduler().runTaskTimer(plugin, new AccelerateWorld(plugin), 0L, (long) plugin.serverConfig.getInt("server.checks") * 40);
            plugin.getServer().getPluginManager().registerEvents(new OnPlayerBedEnter(plugin), plugin);
            plugin.getServer().getPluginManager().registerEvents(new OnPlayerBedLeave(plugin), plugin);
        }
    }

    private void loadBlockRewardsManager() {
        if (sendModuleStatus(plugin.serverConfig.getBoolean("modules.block-reward"), "Block-Reward")) {
            plugin.files.loadBlocksRewards();
        }
    }

    private void loadCommandsManager() {
        if (sendModuleStatus(plugin.serverConfig.getBoolean("modules.commands"), "Commands")) {
            plugin.files.loadCommands();
        }
    }

    private void loadChatManager() {
        if (sendModuleStatus(plugin.serverConfig.getBoolean("modules.chat"), "Chat")) {
            plugin.files.loadChat();
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
            plugin.files.loadKits();
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
            plugin.files.loadRewards();
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
