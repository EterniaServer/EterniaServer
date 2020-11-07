package br.com.eterniaserver.eterniaserver;

import br.com.eterniaserver.eterniaserver.configurations.configs.BlocksCfg;
import br.com.eterniaserver.eterniaserver.configurations.configs.CashCfg;
import br.com.eterniaserver.eterniaserver.configurations.configs.ChatCfg;
import br.com.eterniaserver.eterniaserver.configurations.configs.CommandsCfg;
import br.com.eterniaserver.eterniaserver.configurations.configs.ConfigsCfg;
import br.com.eterniaserver.eterniaserver.configurations.locales.ConstantsCfg;
import br.com.eterniaserver.eterniaserver.configurations.configs.KitsCfg;
import br.com.eterniaserver.eterniaserver.configurations.locales.MsgCfg;
import br.com.eterniaserver.eterniaserver.configurations.configs.RewardsCfg;
import br.com.eterniaserver.eterniaserver.configurations.configs.ScheduleCfg;
import br.com.eterniaserver.eterniaserver.configurations.configs.TableCfg;
import br.com.eterniaserver.eterniaserver.configurations.dependencies.Placeholders;
import br.com.eterniaserver.eterniaserver.configurations.dependencies.VaultHook;
import br.com.eterniaserver.eterniaserver.enums.ConfigBooleans;
import br.com.eterniaserver.eterniaserver.enums.ConfigChanceMaps;
import br.com.eterniaserver.eterniaserver.enums.ConfigDoubles;
import br.com.eterniaserver.eterniaserver.enums.ConfigIntegers;
import br.com.eterniaserver.eterniaserver.enums.ConfigLists;
import br.com.eterniaserver.eterniaserver.enums.ConfigStrings;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.events.BlockHandler;
import br.com.eterniaserver.eterniaserver.events.EntityHandler;
import br.com.eterniaserver.eterniaserver.events.PlayerHandler;
import br.com.eterniaserver.eterniaserver.events.ServerHandler;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EterniaServer extends JavaPlugin {

    public static final BlocksCfg block = new BlocksCfg();
    public static final CashCfg cash = new CashCfg();
    public static final KitsCfg kits = new KitsCfg();
    public static final ChatCfg chat = new ChatCfg();
    public static final ScheduleCfg schedule =  new ScheduleCfg();
    public static final CommandsCfg commands = new CommandsCfg();

    private static final String[] strings = new String[ConfigStrings.values().length];
    private static final String[] messages = new String[Messages.values().length];
    private static final Boolean[] booleans = new Boolean[ConfigBooleans.values().length];
    private static final Integer[] integers = new Integer[ConfigIntegers.values().length];
    private static final Double[] doubles = new Double[ConfigDoubles.values().length];

    private static final List<List<String>> stringLists = new ArrayList<>();
    private static final List<Material> elevatorMaterials = new ArrayList<>();
    private static final List<Map<String, Map<Double, List<String>>>> chanceMaps = new ArrayList<>();

    @Override
    public void onEnable() {

        for (int i = 0; i < ConfigLists.values().length; i++) {
            stringLists.add(new ArrayList<>());
        }

        for (int i = 0; i < ConfigChanceMaps.values().length; i++) {
            chanceMaps.add(new HashMap<>());
        }

        new ConstantsCfg(strings);
        new MsgCfg(messages);
        new ConfigsCfg(strings, booleans, integers, doubles, stringLists, elevatorMaterials);
        new RewardsCfg(chanceMaps);

        new TableCfg();
        new Placeholders().register();
        new Managers(this);
        new VaultHook(this);

        this.getServer().getPluginManager().registerEvents(new BlockHandler(), this);
        this.getServer().getPluginManager().registerEvents(new EntityHandler(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerHandler(this), this);
        this.getServer().getPluginManager().registerEvents(new ServerHandler(), this);

    }

    public static String getString(ConfigStrings configName) {
        return strings[configName.ordinal()];
    }

    public static boolean getBoolean(ConfigBooleans configName) {
        return booleans[configName.ordinal()];
    }

    public static int getInteger(ConfigIntegers configName) {
        return integers[configName.ordinal()];
    }

    public static double getDouble(ConfigDoubles configName) {
        return doubles[configName.ordinal()];
    }

    public static List<String> getStringList(ConfigLists configName) {
        return stringLists.get(configName.ordinal());
    }

    public static List<Material> getElevatorMaterials() {
        return elevatorMaterials;
    }

    public static Map<String, Map<Double, List<String>>> getChanceMap(ConfigChanceMaps configName) {
        return chanceMaps.get(configName.ordinal());
    }

    public static String getMessage(Messages messagesId, boolean prefix, String... args) {
        return Constants.getMessage(messagesId, prefix, messages, args);
    }

    public static void sendMessage(CommandSender sender, Messages messagesId, String... args) {
        Constants.sendMessage(sender, messagesId, true, messages, args);
    }

    public static void sendMessage(CommandSender sender, Messages messagesId, boolean prefix, String... args) {
        Constants.sendMessage(sender, messagesId, prefix, args);
    }

}
