package br.com.eterniaserver.eterniaserver;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.generics.*;

import co.aikar.commands.PaperCommandManager;
import com.google.common.collect.ImmutableList;

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

        manager.getCommandCompletions().registerCompletion("colors", c -> ImmutableList.of("dark", "darkblue",
                "darkgreen", "darkaqua", "darkred", "darkpurple", "gold", "lightgray", "darkgray", "blue", "green",
                "aqua", "red", "purple", "yellow", "white"));

        manager.getCommandCompletions().registerCompletion("entidades", c -> plugin.entityList);

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
            manager.registerCommand(new Cash(plugin));
        }
    }

    private void loadChatManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.chat"), "Chat")) {
            plugin.getFiles().loadChat();
            manager.registerCommand(new Channels(plugin));
            manager.registerCommand(new Mute(plugin));
            manager.registerCommand(new ChatCommands(plugin));
            new AdvancedChatTorch(plugin);
        }
    }

    private void loadEconomyManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.economy"), "Economy")) {
            manager.registerCommand(new Economy(plugin));
            manager.registerCommand(new EcoChange(plugin));
        }
    }

    private void loadElevatorManager() {
        sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.elevator"), "Elevator");
    }

    private void loadExperienceManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.experience"), "Experience")) {
            manager.registerCommand(new Experience(plugin));
        }
    }

    private void loadGenericManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.generic"), "Generic")) {
            manager.registerCommand(new Gamemode(messages));
            manager.registerCommand(new Inventory(messages));
            manager.registerCommand(new Others(plugin));
            manager.registerCommand(new Replaces(plugin));
            manager.registerCommand(new Simplifications(messages));
            manager.registerCommand(new Glow(plugin));
        }
    }

    private void loadHomesManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.home"), "Homes")) {
            manager.registerCommand(new HomeSystem(plugin));
        }
    }

    private void loadKitManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.kits"), "Kits")) {
            plugin.getFiles().loadKits();
            manager.registerCommand(new KitSystem(plugin));
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
            manager.registerCommand(new RewardsSystem(plugin));
        }
    }

    private void loadSpawnersManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.spawners"), "Spawners")) {
            manager.registerCommand(new SpawnerGive(plugin));
        }
    }

    private void loadTeleportsManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.teleports"), "Teleports")) {
            manager.registerCommand(new WarpSystem(plugin));
            manager.registerCommand(new TeleportSystem(plugin));
        }
    }

    private boolean sendModuleStatus(final boolean enable, final String module) {
        if (enable) messages.sendConsole("modules.enable", Constants.MODULE, module);
        else messages.sendConsole("modules.disable", Constants.MODULE, module);
        return enable;
    }

}
