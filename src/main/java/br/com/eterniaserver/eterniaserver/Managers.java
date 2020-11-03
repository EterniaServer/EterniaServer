package br.com.eterniaserver.eterniaserver;

import br.com.eterniaserver.acf.ConditionFailedException;
import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.commands.*;
import br.com.eterniaserver.eterniaserver.core.PluginClearSchedule;
import br.com.eterniaserver.eterniaserver.core.PluginTick;
import br.com.eterniaserver.eterniaserver.core.PluginSchedule;
import br.com.eterniaserver.eterniaserver.core.Vars;
import br.com.eterniaserver.eterniaserver.core.CheckWorld;
import br.com.eterniaserver.eterniaserver.enums.ConfigBooleans;
import br.com.eterniaserver.eterniaserver.enums.ConfigIntegers;
import br.com.eterniaserver.eterniaserver.objects.CustomCommand;
import br.com.eterniaserver.eterniaserver.enums.Colors;
import br.com.eterniaserver.eterniaserver.enums.Commands;
import br.com.eterniaserver.eterniaserver.enums.Messages;

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

        loadCommandsLocale();
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

    private void loadCommandsLocale() {
        for (Commands command : Commands.values()) {
            EterniaLib.getManager().getCommandReplacements().addReplacements(
                    command.name().toLowerCase(), EterniaServer.cmdsLocale.getName(command),
                    command.name().toLowerCase() + "_description", EterniaServer.cmdsLocale.getDescription(command),
                    command.name().toLowerCase() + "_perm", EterniaServer.cmdsLocale.getPerm(command),
                    command.name().toLowerCase() + "_syntax", EterniaServer.cmdsLocale.getSyntax(command),
                    command.name().toLowerCase() + "_aliases", EterniaServer.cmdsLocale.getAliases(command)
            );
        }
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
        EterniaLib.getManager().getCommandCompletions().registerStaticCompletion("entidades", Vars.entityList);
    }

    private void loadBedManager() {
        if (sendModuleStatus(EterniaServer.getBoolean(ConfigBooleans.MODULE_BED), "Bed")) {
            plugin.getServer().getScheduler().runTaskTimer(plugin, new CheckWorld(plugin), 0L, (long) EterniaServer.getInteger(ConfigIntegers.PLUGIN_TICKS) * 40);
        }
    }

    private void loadBlockRewardsManager() {
        sendModuleStatus(EterniaServer.getBoolean(ConfigBooleans.MODULE_BLOCK), "Block-Reward");
    }

    private void loadCommandsManager() {
        if (sendModuleStatus(EterniaServer.getBoolean(ConfigBooleans.MODULE_COMMANDS), "Commands")) {
            EterniaServer.commands.customCommandMap.forEach((commandName, commandObject) -> new CustomCommand(plugin, commandName, commandObject.getDescription(), commandObject.getAliases(), commandObject.getText(), commandObject.getCommands(), commandObject.getConsole()));
        }
    }

    private void loadCashManager() {
        if (sendModuleStatus(EterniaServer.getBoolean(ConfigBooleans.MODULE_CASH), "Cash")) {
            EterniaLib.getManager().registerCommand(new Cash());
        }
    }

    private void loadChatManager() {
        if (sendModuleStatus(EterniaServer.getBoolean(ConfigBooleans.MODULE_CHAT), "Chat")) {
            EterniaLib.getManager().registerCommand(new Channel());
            EterniaLib.getManager().registerCommand(new Mute());
            EterniaLib.getManager().registerCommand(new Chat(plugin));
            EterniaLib.getManager().registerCommand(new Nick());
        }
    }

    private void loadEconomyManager() {
        if (sendModuleStatus(EterniaServer.getBoolean(ConfigBooleans.MODULE_ECONOMY), "Economy")) {
            EterniaLib.getManager().registerCommand(new Economy());
        }
    }

    private void loadElevatorManager() {
        sendModuleStatus(EterniaServer.getBoolean(ConfigBooleans.MODULE_ELEVATOR), "Elevator");
    }

    private void loadExperienceManager() {
        if (sendModuleStatus(EterniaServer.getBoolean(ConfigBooleans.MODULE_EXPERIENCE), "Experience"))
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
        if (sendModuleStatus(EterniaServer.getBoolean(ConfigBooleans.MODULE_HOMES), "Homes")) {
            EterniaLib.getManager().registerCommand(new Home());
        }
    }

    private void loadKitManager() {
        if (sendModuleStatus(EterniaServer.getBoolean(ConfigBooleans.MODULE_KITS), "Kits")) {
            EterniaLib.getManager().registerCommand(new Kit());
        }
    }

    private void loadPlayerChecks() {
        sendModuleStatus(true, "PlayerChecks");
        if (EterniaServer.getBoolean(ConfigBooleans.ASYNC_CHECK)) {
            new PluginTick(plugin).runTaskTimerAsynchronously(plugin, 20L, (long) EterniaServer.getInteger(ConfigIntegers.PLUGIN_TICKS) * 20);
            return;
        }
        new PluginTick(plugin).runTaskTimer(plugin, 20L, (long) EterniaServer.getInteger(ConfigIntegers.PLUGIN_TICKS) * 20);
    }

    private void loadClearManager() {
        if (sendModuleStatus(EterniaServer.getBoolean(ConfigBooleans.MODULE_CLEAR), "Mob Control")) {
            new PluginClearSchedule().runTaskTimer(plugin, 20L, 600L);
        }
    }

    private void loadRewardsManager() {
        if (sendModuleStatus(EterniaServer.getBoolean(ConfigBooleans.MODULE_REWARDS), "Rewards")) {
            EterniaLib.getManager().registerCommand(new Reward());
        }
    }

    private void loadSpawnersManager() {
        if (sendModuleStatus(EterniaServer.getBoolean(ConfigBooleans.MODULE_SPAWNERS), "Spawners")) {
            EterniaLib.getManager().registerCommand(new Spawner());
        }
    }

    private void loadTeleportsManager() {
        if (sendModuleStatus(EterniaServer.getBoolean(ConfigBooleans.MODULE_TELEPORTS), "Teleports")) {
            EterniaLib.getManager().registerCommand(new Warp());
            EterniaLib.getManager().registerCommand(new Teleport());
        }
    }

    private void loadScheduleTasks() {
        if (sendModuleStatus(EterniaServer.getBoolean(ConfigBooleans.MODULE_SCHEDULE), "Schedule")) {
            long start = ChronoUnit.MILLIS.between(LocalTime.now(), LocalTime.of(
                    EterniaServer.schedule.scheduleHour,
                    EterniaServer.schedule.scheduleMinute,
                    EterniaServer.schedule.scheduleSecond));
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleWithFixedDelay(new PluginSchedule(plugin), start, TimeUnit.HOURS.toMillis(EterniaServer.schedule.scheduleDelay), TimeUnit.MILLISECONDS);
        }
    }

    private boolean sendModuleStatus(final boolean enable, final String module) {
        if (enable) {
            Bukkit.getConsoleSender().sendMessage(EterniaServer.msg.getMessage(Messages.SERVER_MODULE_ENABLED, true, module));
            return true;
        }
        Bukkit.getConsoleSender().sendMessage(EterniaServer.msg.getMessage(Messages.SERVER_MODULE_DISABLED, true, module));
        return false;
    }

}
