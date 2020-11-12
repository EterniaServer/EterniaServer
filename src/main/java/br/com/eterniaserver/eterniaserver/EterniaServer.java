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
import br.com.eterniaserver.eterniaserver.objects.CashItem;
import br.com.eterniaserver.eterniaserver.objects.ChannelObject;
import br.com.eterniaserver.eterniaserver.objects.CommandData;
import br.com.eterniaserver.eterniaserver.objects.CustomKit;
import br.com.eterniaserver.eterniaserver.objects.CustomPlaceholder;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class EterniaServer extends JavaPlugin {

    private static Pattern filter;

    private static final String[] strings = new String[ConfigStrings.values().length];
    private static final String[] messages = new String[Messages.values().length];
    private static final Boolean[] booleans = new Boolean[ConfigBooleans.values().length];
    private static final Integer[] integers = new Integer[ConfigIntegers.values().length];
    private static final Double[] doubles = new Double[ConfigDoubles.values().length];

    private static final List<List<String>> stringLists = new ArrayList<>();
    private static final List<Material> elevatorMaterials = new ArrayList<>();
    private static final List<ItemStack> menuGui = new ArrayList<>();
    private static final List<Map<String, Map<Double, List<String>>>> chanceMaps = new ArrayList<>();

    private static final Map<String, CustomPlaceholder> customPlaceholdersObjectsMap = new HashMap<>();
    private static final Map<String, CommandData> customCommandMap = new HashMap<>();
    private static final Map<String, CustomKit> kitList = new HashMap<>();
    private static final Map<String, Map<Integer, List<String>>> scheduleMap = new HashMap<>();
    private static final Map<Integer, String> guis = new HashMap<>();
    private static final Map<String, Integer> guisInvert = new HashMap<>();
    private static final Map<Integer, List<CashItem>> othersGui = new HashMap<>();
    private static final Map<Integer, ChannelObject> channelsMap = new HashMap<>();
    private static final List<String> channels = new ArrayList<>();

    @Override
    public void onEnable() {

        for (int i = 0; i < ConfigLists.values().length; i++) {
            stringLists.add(new ArrayList<>());
        }

        for (int i = 0; i < ConfigChanceMaps.values().length; i++) {
            chanceMaps.add(new HashMap<>());
        }

        constants();
        messages();
        configs();
        commands();
        blocks();
        chat();
        kits();
        cash();
        rewards();
        schedule();

        new TableCfg();
        new Placeholders().register();
        new Managers(this);
        new VaultHook(this);

        this.getServer().getPluginManager().registerEvents(new BlockHandler(), this);
        this.getServer().getPluginManager().registerEvents(new EntityHandler(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerHandler(this), this);
        this.getServer().getPluginManager().registerEvents(new ServerHandler(), this);

    }

    public void constants() {
        new ConstantsCfg(strings);
    }

    public void messages() {
        new MsgCfg(messages);
    }

    public void configs() {
        new ConfigsCfg(strings, booleans, integers, doubles, stringLists, elevatorMaterials);
    }

    public void commands() {
        new CommandsCfg(customCommandMap);
    }

    public void blocks() {
        new BlocksCfg(chanceMaps);
    }

    public void chat() {
        new ChatCfg(customPlaceholdersObjectsMap, channelsMap, channels, integers);
    }

    public void kits() {
        new KitsCfg(kitList);
    }

    public void cash() {
        new CashCfg(menuGui, guis, guisInvert, othersGui);
    }

    public void rewards() {
        new RewardsCfg(chanceMaps);
    }

    public void schedule() {
        new ScheduleCfg(scheduleMap, integers);
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

    public static void setFilter(Pattern pattern) {
        filter = pattern;
    }

    public static Pattern getFilter() {
        return filter;
    }

    public static List<String> getStringList(ConfigLists configName) {
        return stringLists.get(configName.ordinal());
    }

    public static List<Material> getElevatorMaterials() {
        return elevatorMaterials;
    }

    public static List<ItemStack> getMenuGui() {
        return menuGui;
    }

    public static Map<String, CustomPlaceholder> getCustomPlaceholders() {
        return customPlaceholdersObjectsMap;
    }

    public static Map<String, CommandData> getCustomCommandMap() {
        return customCommandMap;
    }

    public static Map<String, CustomKit> getKitList() {
        return kitList;
    }

    public static Map<Integer, String> getGuis() {
        return guis;
    }

    public static Map<String, Integer> getGuisInvert() {
        return guisInvert;
    }

    public static Map<Integer, List<CashItem>> getOthersGui() {
        return othersGui;
    }

    public static Map<String, Map<Integer, List<String>>> getScheduleMap() {
        return scheduleMap;
    }

    public static Map<Integer, ChannelObject> getChannelsMap() {
        return channelsMap;
    }

    public static Map<String, Map<Double, List<String>>> getChanceMap(ConfigChanceMaps configName) {
        return chanceMaps.get(configName.ordinal());
    }

    public static List<String> getChannels() {
        return channels;
    }

    public static ChannelObject channelObject(int value) {
        return channelsMap.get(value);
    }

    public static String getMessage(Messages messagesId, boolean prefix, String... args) {
        return Constants.getMessage(messagesId, prefix, messages, args);
    }

    public static void sendMessage(CommandSender sender, Messages messagesId, String... args) {
        Constants.sendMessage(sender, messagesId, true, args);
    }

    public static void sendMessage(CommandSender sender, Messages messagesId, boolean prefix, String... args) {
        Constants.sendMessage(sender, messagesId, prefix, args);
    }

}
