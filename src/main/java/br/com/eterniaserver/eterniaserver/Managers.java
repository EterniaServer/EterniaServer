package br.com.eterniaserver.eterniaserver;

import br.com.eterniaserver.acf.ConditionFailedException;
import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.commands.*;
import br.com.eterniaserver.eterniaserver.enums.Colors;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.core.*;

import org.bukkit.Bukkit;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Managers {

    private final EterniaServer plugin;

    public Managers(EterniaServer plugin) {

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
        EterniaLib.getManager().getCommandCompletions().registerStaticCompletion("colors", Stream.of(Colors.values()).map(Enum::name).collect(Collectors.toList()));
        EterniaLib.getManager().getCommandCompletions().registerStaticCompletion("entidades", PluginVars.entityList);
    }

    private void loadBedManager() {
        if (sendModuleStatus(EterniaServer.configs.moduleBed, "Bed")) {
            plugin.getServer().getScheduler().runTaskTimer(plugin, new UtilAccelerateWorld(plugin), 0L, (long) EterniaServer.configs.pluginTicks * 40);
        }
    }

    private void loadBlockRewardsManager() {
        sendModuleStatus(EterniaServer.configs.moduleBlock, "Block-Reward");
    }

    private void loadCommandsManager() {
        sendModuleStatus(EterniaServer.configs.moduleCommands, "Commands");
    }

    private void loadCashManager() {
        if (sendModuleStatus(EterniaServer.configs.moduleCash, "Cash")) {
            EterniaLib.getManager().registerCommand(new Cash());
        }
    }

    private void loadChatManager() {
        if (sendModuleStatus(EterniaServer.configs.moduleChat, "Chat")) {
            EterniaLib.getManager().registerCommand(new Channel());
            EterniaLib.getManager().registerCommand(new Mute());
            EterniaLib.getManager().registerCommand(new Chat(plugin));
            EterniaLib.getManager().registerCommand(new Nick());
        }
    }

    private void loadEconomyManager() {
        if (sendModuleStatus(EterniaServer.configs.moduleEconomy, "Economy")) {
            EterniaLib.getManager().registerCommand(new Economy());
        }
    }

    private void loadElevatorManager() {
        sendModuleStatus(EterniaServer.configs.moduleElevator, "Elevator");
    }

    private void loadExperienceManager() {
        if (sendModuleStatus(EterniaServer.configs.moduleExperience, "Experience"))
            EterniaLib.getManager().registerCommand(new Experience());
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
        if (sendModuleStatus(EterniaServer.configs.moduleHomes, "Homes")) {
            EterniaLib.getManager().registerCommand(new Home());
        }
    }

    private void loadKitManager() {
        if (sendModuleStatus(EterniaServer.configs.moduleKits, "Kits")) {
            EterniaLib.getManager().registerCommand(new Kit());
        }
    }

    private void loadPlayerChecks() {
        sendModuleStatus(true, "PlayerChecks");
        if (EterniaServer.configs.asyncCheck) {
            new PluginTicks(plugin).runTaskTimerAsynchronously(plugin, 20L, (long) EterniaServer.configs.pluginTicks * 20);
            return;
        }
        new PluginTicks(plugin).runTaskTimer(plugin, 20L, (long) EterniaServer.configs.pluginTicks * 20);
    }

    private void loadClearManager() {
        if (sendModuleStatus(EterniaServer.configs.moduleClear, "Mob Control")) {
            new PluginClear().runTaskTimer(plugin, 20L, 600L);
        }
    }

    private void loadRewardsManager() {
        if (sendModuleStatus(EterniaServer.configs.moduleRewards, "Rewards")) {
            EterniaLib.getManager().registerCommand(new Reward());
        }
    }

    private void loadSpawnersManager() {
        if (sendModuleStatus(EterniaServer.configs.moduleSpawners, "Spawners")) {
            EterniaLib.getManager().registerCommand(new Spawner());
        }
    }

    private void loadTeleportsManager() {
        if (sendModuleStatus(EterniaServer.configs.moduleTeleports, "Teleports")) {
            EterniaLib.getManager().registerCommand(new Warp());
            EterniaLib.getManager().registerCommand(new Teleport());
        }
    }

    private void loadScheduleTasks() {
        if (sendModuleStatus(EterniaServer.configs.moduleSchedule, "Schedule")) {
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
            Bukkit.getConsoleSender().sendMessage(EterniaServer.configs.getMessage(Messages.SERVER_MODULE_ENABLED, true, module));
            return true;
        }
        Bukkit.getConsoleSender().sendMessage(EterniaServer.configs.getMessage(Messages.SERVER_MODULE_DISABLED, true, module));
        return false;
    }

}
