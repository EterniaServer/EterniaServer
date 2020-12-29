package br.com.eterniaserver.eterniaserver;

import br.com.eterniaserver.acf.ConditionFailedException;
import br.com.eterniaserver.eternialib.CommandManager;
import br.com.eterniaserver.eterniaserver.commands.*;
import br.com.eterniaserver.eterniaserver.configurations.locales.CommandsLocaleCfg;
import br.com.eterniaserver.eterniaserver.core.PluginClearSchedule;
import br.com.eterniaserver.eterniaserver.core.PluginTick;
import br.com.eterniaserver.eterniaserver.core.PluginSchedule;
import br.com.eterniaserver.eterniaserver.core.CheckWorld;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.Entities;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.objects.ChannelCommand;
import br.com.eterniaserver.eterniaserver.objects.CustomCommand;
import br.com.eterniaserver.eterniaserver.enums.Colors;
import br.com.eterniaserver.eterniaserver.enums.Commands;
import br.com.eterniaserver.eterniaserver.enums.Messages;

import org.bukkit.Bukkit;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Managers {

    private final EterniaServer plugin;

    private String channelCommand;

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
        CommandsLocaleCfg cmdsLocale = new CommandsLocaleCfg();

        this.channelCommand = cmdsLocale.getName(Commands.CHANNEL).split("\\|")[0];

        for (Commands command : Commands.values()) {
            CommandManager.getCommandReplacements().addReplacements(
                    command.name().toLowerCase(), cmdsLocale.getName(command),
                    command.name().toLowerCase() + "_description", cmdsLocale.getDescription(command),
                    command.name().toLowerCase() + "_perm", cmdsLocale.getPerm(command),
                    command.name().toLowerCase() + "_syntax", cmdsLocale.getSyntax(command),
                    command.name().toLowerCase() + "_aliases", cmdsLocale.getAliases(command)
            );
        }
    }

    private void loadConditions() {

        CommandManager.getCommandConditions().addCondition(Integer.class, "limits", (c, exec, value) -> {
            if (value == null || c.getConfigValue("min", 0) > value) {
                throw new ConditionFailedException("O valor mínimo precisa ser &3" + c.getConfigValue("min", 0));
            }
            if (c.getConfigValue("max", 3) < value) {
                throw new ConditionFailedException("O valor máximo precisa ser &3 " + c.getConfigValue("max", 2147483647));
            }
        });

        CommandManager.getCommandConditions().addCondition(Double.class, "limits", (c, exec, value) -> {
            if (value == null || c.getConfigValue("min", 0) > value) {
                throw new ConditionFailedException("O valor mínimo precisa ser &3" + c.getConfigValue("min", 0));
            }
            if (c.getConfigValue("max", 3) < value) {
                throw new ConditionFailedException("O valor máximo precisa ser &3" + c.getConfigValue("max", 1));
            }
        });

        CommandManager.getCommandConditions().addCondition(String.class, "channel", (c, exec, value) -> {
            if (value == null || !EterniaServer.getChannels().contains(value)) {
                throw new ConditionFailedException("Você precisa informar um canal válido");
            }
        });

    }

    private void loadCompletions() {
        CommandManager.getCommandCompletions().registerStaticCompletion("colors", Stream.of(Colors.values()).map(Enum::name).collect(Collectors.toList()));
        CommandManager.getCommandCompletions().registerStaticCompletion("entidades", Stream.of(Entities.values()).map(Enum::name).collect(Collectors.toList()));
        CommandManager.getCommandCompletions().registerStaticCompletion("channels", EterniaServer.getChannels());
    }

    private void loadBedManager() {
        if (sendModuleStatus(EterniaServer.getBoolean(Booleans.MODULE_BED), "Bed")) {
            new CheckWorld(plugin).runTaskTimer(plugin, 0L, (long) EterniaServer.getInteger(Integers.PLUGIN_TICKS) * 40);
        }
    }

    private void loadBlockRewardsManager() {
        sendModuleStatus(EterniaServer.getBoolean(Booleans.MODULE_BLOCK), "Block-Reward");
    }

    private void loadCommandsManager() {
        if (sendModuleStatus(EterniaServer.getBoolean(Booleans.MODULE_COMMANDS), "Commands")) {
            EterniaServer.getCustomCommandMap().forEach((commandName, commandObject) -> new CustomCommand(plugin, commandName, commandObject.getDescription(), commandObject.getAliases(), commandObject.getText(), commandObject.getCommands(), commandObject.getConsole()));
        }
    }

    private void loadCashManager() {
        if (sendModuleStatus(EterniaServer.getBoolean(Booleans.MODULE_CASH), "Cash")) {
            CommandManager.registerCommand(new Cash());
        }
    }

    private void loadChatManager() {
        if (sendModuleStatus(EterniaServer.getBoolean(Booleans.MODULE_CHAT), "Chat")) {
            CommandManager.registerCommand(new Mute());
            CommandManager.registerCommand(new Chat(plugin));
            EterniaServer.getChannelsMap().forEach((ignored, commandObject) -> new ChannelCommand(commandObject.getName(), "chat", commandObject.getPerm(), channelCommand));
        }
    }

    private void loadEconomyManager() {
        if (sendModuleStatus(EterniaServer.getBoolean(Booleans.MODULE_ECONOMY), "Economy")) {
            CommandManager.registerCommand(new Economy());
        }
    }

    private void loadElevatorManager() {
        sendModuleStatus(EterniaServer.getBoolean(Booleans.MODULE_ELEVATOR), "Elevator");
    }

    private void loadExperienceManager() {
        if (sendModuleStatus(EterniaServer.getBoolean(Booleans.MODULE_EXPERIENCE), "Experience"))
            CommandManager.registerCommand(new Experience());
    }

    private void loadGenericManager() {
        sendModuleStatus(true, "Generic");
        CommandManager.registerCommand(new Intern(plugin));
        CommandManager.registerCommand(new Inventory());
        CommandManager.registerCommand(new Generic(plugin));
        CommandManager.registerCommand(new Gamemode());
        CommandManager.registerCommand(new Glow(plugin));
        CommandManager.registerCommand(new Item());
    }

    private void loadHomesManager() {
        if (sendModuleStatus(EterniaServer.getBoolean(Booleans.MODULE_HOMES), "Homes")) {
            CommandManager.registerCommand(new Home());
        }
    }

    private void loadKitManager() {
        if (sendModuleStatus(EterniaServer.getBoolean(Booleans.MODULE_KITS), "Kits")) {
            CommandManager.registerCommand(new Kit());
        }
    }

    private void loadPlayerChecks() {
        sendModuleStatus(true, "PlayerChecks");
        if (EterniaServer.getBoolean(Booleans.ASYNC_CHECK)) {
            new PluginTick(plugin).runTaskTimerAsynchronously(plugin, 20L, (long) EterniaServer.getInteger(Integers.PLUGIN_TICKS) * 20);
            return;
        }
        new PluginTick(plugin).runTaskTimer(plugin, 20L, (long) EterniaServer.getInteger(Integers.PLUGIN_TICKS) * 20);
    }

    private void loadClearManager() {
        if (sendModuleStatus(EterniaServer.getBoolean(Booleans.MODULE_ENTITY), "Mob Control")) {
            new PluginClearSchedule(plugin).runTaskTimerAsynchronously(plugin, 20L, (long) EterniaServer.getInteger(Integers.CLEAR_TIMER) * 20);
        }
    }

    private void loadRewardsManager() {
        if (sendModuleStatus(EterniaServer.getBoolean(Booleans.MODULE_REWARDS), "Rewards")) {
            CommandManager.registerCommand(new Reward());
        }
    }

    private void loadSpawnersManager() {
        if (sendModuleStatus(EterniaServer.getBoolean(Booleans.MODULE_SPAWNERS), "Spawners")) {
            CommandManager.registerCommand(new Spawner());
        }
    }

    private void loadTeleportsManager() {
        if (sendModuleStatus(EterniaServer.getBoolean(Booleans.MODULE_TELEPORTS), "Teleports")) {
            CommandManager.registerCommand(new Warp());
            CommandManager.registerCommand(new Teleport());
        }
    }

    private void loadScheduleTasks() {
        if (sendModuleStatus(EterniaServer.getBoolean(Booleans.MODULE_SCHEDULE), "Schedule")) {
            long start = ChronoUnit.MILLIS.between(LocalTime.now(), LocalTime.of(
                    EterniaServer.getInteger(Integers.SCHEDULE_HOUR),
                    EterniaServer.getInteger(Integers.SCHEDULE_MINUTE),
                    EterniaServer.getInteger(Integers.SCHEDULE_SECONDS)));
            new PluginSchedule(plugin).runTaskTimer(plugin, start, TimeUnit.HOURS.toMillis(EterniaServer.getInteger(Integers.SCHEDULE_DELAY)));
        }
    }

    private boolean sendModuleStatus(final boolean enable, final String module) {
        if (enable) {
            Bukkit.getConsoleSender().sendMessage(EterniaServer.getMessage(Messages.SERVER_MODULE_ENABLED, true, module));
            return true;
        }
        Bukkit.getConsoleSender().sendMessage(EterniaServer.getMessage(Messages.SERVER_MODULE_DISABLED, true, module));
        return false;
    }

}
