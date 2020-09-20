package br.com.eterniaserver.eterniaserver;

import br.com.eterniaserver.acf.ConditionFailedException;
import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.commands.Gamemode;
import br.com.eterniaserver.eterniaserver.commands.Glow;
import br.com.eterniaserver.eterniaserver.commands.Item;
import br.com.eterniaserver.eterniaserver.generics.PluginClear;
import br.com.eterniaserver.eterniaserver.generics.PluginTimer;
import br.com.eterniaserver.eterniaserver.generics.UtilAccelerateWorld;
import br.com.eterniaserver.eterniaserver.generics.UtilAdvancedChatTorch;
import br.com.eterniaserver.eterniaserver.commands.Cash;
import br.com.eterniaserver.eterniaserver.commands.Channel;
import br.com.eterniaserver.eterniaserver.commands.Chat;
import br.com.eterniaserver.eterniaserver.commands.Economy;
import br.com.eterniaserver.eterniaserver.commands.Experience;
import br.com.eterniaserver.eterniaserver.commands.Generic;
import br.com.eterniaserver.eterniaserver.commands.Home;
import br.com.eterniaserver.eterniaserver.commands.Inventory;
import br.com.eterniaserver.eterniaserver.commands.Mute;
import br.com.eterniaserver.eterniaserver.commands.Reward;
import br.com.eterniaserver.eterniaserver.commands.Spawner;
import br.com.eterniaserver.eterniaserver.commands.Teleport;
import br.com.eterniaserver.eterniaserver.commands.Warp;
import br.com.eterniaserver.eterniaserver.generics.PluginTicks;
import br.com.eterniaserver.eterniaserver.commands.Kit;
import br.com.eterniaserver.eterniaserver.generics.PluginVars;
import br.com.eterniaserver.eterniaserver.generics.PluginConstants;
import br.com.eterniaserver.eterniaserver.generics.PluginMSGs;

import org.bukkit.Bukkit;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
        loadClearManager();
        loadKitManager();
        loadRewardsManager();
        loadSpawnersManager();
        loadTeleportsManager();
        loadScheduleTasks();

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
        EterniaLib.getManager().getCommandCompletions().registerStaticCompletion("colors", PluginVars.colorsString);
        EterniaLib.getManager().getCommandCompletions().registerStaticCompletion("entidades", PluginVars.entityList);
    }

    private void loadBedManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.bed"), "Bed")) {
            plugin.getServer().getScheduler().runTaskTimer(plugin, new UtilAccelerateWorld(plugin), 0L, (long) EterniaServer.serverConfig.getInt(PluginConstants.SERVER_CHECKS) * 40);
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
            EterniaLib.getManager().registerCommand(new Channel());
            EterniaLib.getManager().registerCommand(new Mute());
            EterniaLib.getManager().registerCommand(new Chat(plugin));
            new UtilAdvancedChatTorch();
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
        sendModuleStatus(true, "Generic");
        EterniaLib.getManager().registerCommand(new Inventory());
        EterniaLib.getManager().registerCommand(new Generic(plugin));
        EterniaLib.getManager().registerCommand(new Gamemode());
        EterniaLib.getManager().registerCommand(new Glow());
        EterniaLib.getManager().registerCommand(new Item());
    }

    private void loadHomesManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.home"), "Homes")) {
            EterniaLib.getManager().registerCommand(new Home());
        }
    }

    private void loadKitManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.kits"), "Kits")) {
            plugin.getFiles().loadKits();
            EterniaLib.getManager().registerCommand(new Kit());
        }
    }

    private void loadPlayerChecks() {
        sendModuleStatus(true, "PlayerChecks");
        if (EterniaServer.serverConfig.getBoolean("server.async-check")) {
            new PluginTicks(plugin).runTaskTimerAsynchronously(plugin, 20L, (long) EterniaServer.serverConfig.getInt(PluginConstants.SERVER_CHECKS) * 20);
            return;
        }
        new PluginTicks(plugin).runTaskTimer(plugin, 20L, (long) EterniaServer.serverConfig.getInt(PluginConstants.SERVER_CHECKS) * 20);
    }

    private void loadClearManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.clear"), "Mob Control")) {
            new PluginClear().runTaskTimer(plugin, 20L, 600L);
        }
    }

    private void loadRewardsManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.rewards"), "Rewards")) {
            plugin.getFiles().loadRewards();
            EterniaLib.getManager().registerCommand(new Reward());
        }
    }

    private void loadSpawnersManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.spawners"), "Spawners")) {
            EterniaLib.getManager().registerCommand(new Spawner());
        }
    }

    private void loadTeleportsManager() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.teleports"), "Teleports")) {
            EterniaLib.getManager().registerCommand(new Warp());
            EterniaLib.getManager().registerCommand(new Teleport());
        }
    }

    private void loadScheduleTasks() {
        if (sendModuleStatus(EterniaServer.serverConfig.getBoolean("modules.schedule"), "Schedule")) {
            plugin.getFiles().loadSchedules();
            long start = ChronoUnit.MILLIS.between(LocalTime.now(), LocalTime.of(
                    EterniaServer.scheduleConfig.getInt("schedule.hour"),
                    EterniaServer.scheduleConfig.getInt("schedule.minute"),
                    EterniaServer.scheduleConfig.getInt("schedule.second")));
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleWithFixedDelay(new PluginTimer(plugin), start, TimeUnit.HOURS.toMillis(
                    EterniaServer.scheduleConfig.getInt("schedule.delay")
            ), TimeUnit.MILLISECONDS);
        }
    }

    private boolean sendModuleStatus(final boolean enable, final String module) {
        if (enable) {
            Bukkit.getConsoleSender().sendMessage(PluginMSGs.MSG_MODULE_ENABLE.replace(PluginConstants.MODULE, module));
            return true;
        }
        Bukkit.getConsoleSender().sendMessage(PluginMSGs.MSG_MODULE_DISABLE.replace(PluginConstants.MODULE, module));
        return false;
    }

}
