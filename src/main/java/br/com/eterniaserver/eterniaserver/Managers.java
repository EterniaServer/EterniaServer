package br.com.eterniaserver.eterniaserver;

import br.com.eterniaserver.acf.ConditionFailedException;
import br.com.eterniaserver.eternialib.CommandManager;
import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.commands.*;
import br.com.eterniaserver.eterniaserver.configurations.locales.CommandsLocaleCfg;
import br.com.eterniaserver.eterniaserver.core.EterniaTick;
import br.com.eterniaserver.eterniaserver.core.PluginClearSchedule;
import br.com.eterniaserver.eterniaserver.core.PluginSchedule;
import br.com.eterniaserver.eterniaserver.core.CheckWorld;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.Entities;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.handlers.BlocksHandler;
import br.com.eterniaserver.eterniaserver.handlers.ChestShopHandler;
import br.com.eterniaserver.eterniaserver.objects.ChannelCommand;
import br.com.eterniaserver.eterniaserver.objects.CustomCommand;
import br.com.eterniaserver.eterniaserver.enums.Colors;
import br.com.eterniaserver.eterniaserver.enums.Commands;
import br.com.eterniaserver.eterniaserver.enums.Messages;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Managers {

    private final EterniaServer plugin;

    private String baltopName;
    private String channelCommand;

    public Managers(final EterniaServer plugin) {

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
        loadShopManager();
        loadHomesManager();
        loadEterniaTick();
        loadClearManager();
        loadKitManager();
        loadRewardsManager();
        loadSpawnersManager();
        loadTeleportsManager();
        loadScheduleTasks();
        loadChestShopComp();

    }

    private void loadChestShopComp() {
        if (plugin.getBoolean(Booleans.CHEST_SHOP_SUPPORT)) {
            plugin.getServer().getPluginManager().registerEvents(new ChestShopHandler(plugin), plugin);
        }
    }

    private void loadCommandsLocale() {
        CommandsLocaleCfg cmdsLocale = new CommandsLocaleCfg(plugin);

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

        this.baltopName = cmdsLocale.getName(Commands.ECO_BALTOP);
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
            if (value == null || !plugin.getChannels().contains(value)) {
                throw new ConditionFailedException("Você precisa informar um canal válido");
            }
        });

    }

    private void loadCompletions() {
        CommandManager.getCommandCompletions().registerStaticCompletion("colors", Stream.of(Colors.values()).map(Enum::name).collect(Collectors.toList()));
        CommandManager.getCommandCompletions().registerStaticCompletion("entidades", Stream.of(Entities.values()).map(Enum::name).collect(Collectors.toList()));
        CommandManager.getCommandCompletions().registerStaticCompletion("channels", plugin.getChannels());
        CommandManager.getCommandCompletions().registerCompletion("list_of_shops", shop -> plugin.getShopList());
    }

    private void loadBedManager() {
        if (sendModuleStatus(plugin.getBoolean(Booleans.MODULE_BED), "Bed")) {
            new CheckWorld(plugin).runTaskTimer(plugin, 0L, (long) plugin.getInteger(Integers.PLUGIN_TICKS) * 40);
        }
    }

    private void loadBlockRewardsManager() {
        sendModuleStatus(plugin.getBoolean(Booleans.MODULE_BLOCK), "Block-Reward");
    }

    private void loadCommandsManager() {
        if (sendModuleStatus(plugin.getBoolean(Booleans.MODULE_COMMANDS), "Commands")) {
            plugin.getCustomCommandMap().forEach((commandName, commandObject) -> new CustomCommand(plugin, commandName, commandObject.getDescription(), commandObject.getAliases(), commandObject.getText(), commandObject.getCommands(), commandObject.getConsole()));
        }
    }

    private void loadCashManager() {
        if (sendModuleStatus(plugin.getBoolean(Booleans.MODULE_CASH), "Cash")) {
            CommandManager.registerCommand(new Cash(plugin));
        }
    }

    private void loadShopManager() {
        if (sendModuleStatus(plugin.getBoolean(Booleans.MODULE_SHOP), "Shop")) {
            CommandManager.registerCommand(new Shop(plugin));
        }
    }

    private void loadChatManager() {
        if (sendModuleStatus(plugin.getBoolean(Booleans.MODULE_CHAT), "Chat")) {
            CommandManager.registerCommand(new Mute(plugin));
            CommandManager.registerCommand(new Chat(plugin));
            plugin.getChannelsMap().forEach((ignored, commandObject) -> new ChannelCommand(plugin, commandObject.getName(), "chat", commandObject.getPerm(), channelCommand));
        }
    }

    private void loadEconomyManager() {
        if (sendModuleStatus(plugin.getBoolean(Booleans.MODULE_ECONOMY), "Economy")) {
            CommandManager.registerCommand(new Economy(plugin, baltopName));
        }
    }

    private void loadElevatorManager() {
        sendModuleStatus(plugin.getBoolean(Booleans.MODULE_ELEVATOR), "Elevator");
    }

    private void loadExperienceManager() {
        if (sendModuleStatus(plugin.getBoolean(Booleans.MODULE_EXPERIENCE), "Experience"))
            CommandManager.registerCommand(new Experience(plugin));
    }

    private void loadGenericManager() {
        if (sendModuleStatus(plugin.getBoolean(Booleans.MODULE_GENERIC), "Generic")) {
            CommandManager.registerCommand(new Inventory(plugin));
            CommandManager.registerCommand(new Gamemode(plugin));
            CommandManager.registerCommand(new Glow(plugin));
            CommandManager.registerCommand(new Item(plugin));
        }
        CommandManager.registerCommand(new Generic(plugin));
    }

    private void loadHomesManager() {
        if (sendModuleStatus(plugin.getBoolean(Booleans.MODULE_HOMES), "Homes")) {
            CommandManager.registerCommand(new Home(plugin));
        }
    }

    private void loadKitManager() {
        if (sendModuleStatus(plugin.getBoolean(Booleans.MODULE_KITS), "Kits")) {
            CommandManager.registerCommand(new Kit(plugin));
        }
    }

    private void loadEterniaTick() {
        new EterniaTick(plugin).runTaskTimer(plugin, 20L, (long) plugin.getInteger(Integers.PLUGIN_TICKS) * 20L);
    }

    private void loadClearManager() {
        if (sendModuleStatus(plugin.getBoolean(Booleans.MODULE_ENTITY), "Mob Control")) {
            new PluginClearSchedule(plugin).runTaskTimerAsynchronously(plugin, 20L, (long) plugin.getInteger(Integers.CLEAR_TIMER) * 20);
        }
    }

    private void loadRewardsManager() {
        if (sendModuleStatus(plugin.getBoolean(Booleans.MODULE_REWARDS), "Rewards")) {
            CommandManager.registerCommand(new Reward(plugin));
        }
    }

    private void loadSpawnersManager() {
        if (sendModuleStatus(plugin.getBoolean(Booleans.MODULE_SPAWNERS), "Spawners")) {
            CommandManager.registerCommand(new Spawner(plugin));
        }
    }

    private void loadTeleportsManager() {
        if (sendModuleStatus(plugin.getBoolean(Booleans.MODULE_TELEPORTS), "Teleports")) {
            CommandManager.registerCommand(new Warp(plugin));
            CommandManager.registerCommand(new Teleport(plugin));
        }
    }

    private void loadScheduleTasks() {
        if (sendModuleStatus(plugin.getBoolean(Booleans.MODULE_SCHEDULE), "Schedule")) {
            long time = ChronoUnit.SECONDS.between(LocalTime.now(), LocalTime.of(plugin.getInteger(Integers.SCHEDULE_HOUR), plugin.getInteger(Integers.SCHEDULE_MINUTE), plugin.getInteger(Integers.SCHEDULE_SECONDS)));
            if (time < 0) {
                time += TimeUnit.HOURS.toSeconds(plugin.getInteger(Integers.SCHEDULE_DELAY));
            }
            new PluginSchedule(plugin).runTaskTimer(plugin, time * 20L, TimeUnit.HOURS.toSeconds(plugin.getInteger(Integers.SCHEDULE_DELAY)) * 20L);
        }
    }

    private boolean sendModuleStatus(final boolean enable, final String module) {
        if (enable) {
            EterniaLib.report(plugin.getMessage(Messages.SERVER_MODULE_ENABLED, true, module));
            return true;
        }
        EterniaLib.report(plugin.getMessage(Messages.SERVER_MODULE_DISABLED, true, module));
        return false;
    }

}
