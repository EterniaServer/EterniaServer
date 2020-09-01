package br.com.eterniaserver.eterniaserver;

import br.com.eterniaserver.acf.ConditionFailedException;
import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.generics.BaseCmdItem;
import br.com.eterniaserver.eterniaserver.generics.UtilAccelerateWorld;
import br.com.eterniaserver.eterniaserver.generics.UtilAdvancedChatTorch;
import br.com.eterniaserver.eterniaserver.generics.BaseCmdCash;
import br.com.eterniaserver.eterniaserver.generics.BaseCmdChannels;
import br.com.eterniaserver.eterniaserver.generics.BaseCmdChat;
import br.com.eterniaserver.eterniaserver.generics.BaseCmdEconomy;
import br.com.eterniaserver.eterniaserver.generics.BaseCmdExperience;
import br.com.eterniaserver.eterniaserver.generics.BaseCmdGeneric;
import br.com.eterniaserver.eterniaserver.generics.BaseCmdHome;
import br.com.eterniaserver.eterniaserver.generics.BaseCmdInventory;
import br.com.eterniaserver.eterniaserver.generics.BaseCmdMute;
import br.com.eterniaserver.eterniaserver.generics.BaseCmdRewards;
import br.com.eterniaserver.eterniaserver.generics.BaseCmdSpawner;
import br.com.eterniaserver.eterniaserver.generics.BaseCmdTeleport;
import br.com.eterniaserver.eterniaserver.generics.BaseCmdWarp;
import br.com.eterniaserver.eterniaserver.generics.PluginTick;
import br.com.eterniaserver.eterniaserver.generics.BaseCmdKit;
import br.com.eterniaserver.eterniaserver.generics.Vars;
import br.com.eterniaserver.eterniaserver.strings.Constants;
import br.com.eterniaserver.eterniaserver.strings.MSG;

import org.bukkit.Bukkit;

public class Managers {

    private final EterniaServer plugin;

    public Managers(EterniaServer plugin) {

        EterniaLib.getManager().enableUnstableAPI("help");
        
        this.plugin = plugin;

        loadConditions();
        loadCompletions();
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

    private void loadConditions() {

        EterniaLib.getManager().getCommandConditions().addCondition(Integer.class, "limits", (c, exec, value) -> {
            if (value == null) {
                return;
            }
            if (c.getConfigValue("min", 0) > value) {
                throw new ConditionFailedException("O valor mínimo precisa ser &3" + c.getConfigValue("min", 0));
            }
            if (c.getConfigValue("max", 3) < value) {
                throw new ConditionFailedException("O valor máximo precisa ser &3 " + c.getConfigValue("max", 3));
            }
        });

        EterniaLib.getManager().getCommandConditions().addCondition(Double.class, "limits", (c, exec, value) -> {
            if (value == null) {
                return;
            }
            if (c.getConfigValue("min", 0) > value) {
                throw new ConditionFailedException("O valor mínimo precisa ser &3" + c.getConfigValue("min", 0));
            }
            if (c.getConfigValue("max", 3) < value) {
                throw new ConditionFailedException("O valor máximo precisa ser &3 " + c.getConfigValue("max", 3));
            }
        });

    }

    private void loadCompletions() {
        EterniaLib.getManager().getCommandCompletions().registerStaticCompletion("colors", Vars.colorsString);
        EterniaLib.getManager().getCommandCompletions().registerStaticCompletion("entidades", Vars.entityList);
    }

    private void loadBedManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.bed"), "Bed")) {
            plugin.getServer().getScheduler().runTaskTimer(plugin, new UtilAccelerateWorld(plugin), 0L, (long) EterniaServer.serverConfig.getInt(Constants.SERVER_CHECKS) * 40);
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
            new UtilAdvancedChatTorch();
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
        EterniaLib.getManager().registerCommand(new BaseCmdInventory());
        EterniaLib.getManager().registerCommand(new BaseCmdGeneric(plugin));
        EterniaLib.getManager().registerCommand(new BaseCmdItem());
    }

    private void loadHomesManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.home"), "Homes")) {
            EterniaLib.getManager().registerCommand(new BaseCmdHome());
        }
    }

    private void loadKitManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.kits"), "Kits")) {
            plugin.getFiles().loadKits();
            EterniaLib.getManager().registerCommand(new BaseCmdKit());
        }
    }

    private void loadPlayerChecks() {
        sendModuleStatus(true, "PlayerChecks");
        if (EterniaServer.serverConfig.getBoolean("server.async-check")) {
            new PluginTick(plugin).runTaskTimerAsynchronously(plugin, 20L, (long) EterniaServer.serverConfig.getInt(Constants.SERVER_CHECKS) * 20);
            return;
        }
        new PluginTick(plugin).runTaskTimer(plugin, 20L, (long) EterniaServer.serverConfig.getInt(Constants.SERVER_CHECKS) * 20);
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
            Bukkit.getConsoleSender().sendMessage(MSG.MSG_MODULE_ENABLE.replace(Constants.MODULE, module));
            return true;
        }
        Bukkit.getConsoleSender().sendMessage(MSG.MSG_MODULE_DISABLE.replace(Constants.MODULE, module));
        return false;
    }

}
