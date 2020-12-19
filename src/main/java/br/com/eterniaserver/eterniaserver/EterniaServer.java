package br.com.eterniaserver.eterniaserver;

import br.com.eterniaserver.eterniaserver.configurations.configs.BlocksCfg;
import br.com.eterniaserver.eterniaserver.configurations.configs.CashCfg;
import br.com.eterniaserver.eterniaserver.configurations.configs.ChatCfg;
import br.com.eterniaserver.eterniaserver.configurations.configs.EntityCfg;
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
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.ChanceMaps;
import br.com.eterniaserver.eterniaserver.enums.Doubles;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.enums.Lists;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.handlers.BlockHandler;
import br.com.eterniaserver.eterniaserver.handlers.EntityHandler;
import br.com.eterniaserver.eterniaserver.handlers.PlayerHandler;
import br.com.eterniaserver.eterniaserver.handlers.ServerHandler;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.objects.CashItem;
import br.com.eterniaserver.eterniaserver.objects.ChannelObject;
import br.com.eterniaserver.eterniaserver.objects.CommandData;
import br.com.eterniaserver.eterniaserver.objects.CustomKit;
import br.com.eterniaserver.eterniaserver.objects.CustomPlaceholder;
import br.com.eterniaserver.eterniaserver.objects.EntityControl;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class EterniaServer extends JavaPlugin {

    private static Pattern filter;

    private static final String[] strings = new String[Strings.values().length];
    private static final String[] messages = new String[Messages.values().length];
    private static final Boolean[] booleans = new Boolean[Booleans.values().length];
    private static final Integer[] integers = new Integer[Integers.values().length];
    private static final Double[] doubles = new Double[Doubles.values().length];

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

    private static final EntityControl[] entities = new EntityControl[EntityType.values().length];

    private boolean test = false;

    public EterniaServer() {
        super();
    }

    public EterniaServer(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
        this.test = true;
    }

    @Override
    public void onEnable() {

        if (test) return;

        for (int i = 0; i < Lists.values().length; i++) {
            stringLists.add(new ArrayList<>());
        }

        for (int i = 0; i < ChanceMaps.values().length; i++) {
            chanceMaps.add(new HashMap<>());
        }

        constants();
        messages();
        configs();
        commands();
        blocks();
        chat();
        entity();
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

    public void entity() {
        new EntityCfg(booleans, integers, entities);
    }

    public void blocks() {
        new BlocksCfg(chanceMaps);
    }

    public void chat() {
        new ChatCfg(customPlaceholdersObjectsMap, channelsMap, channels, integers, strings);
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

    public static boolean changeState(String string, Boolean state) {
        switch (string) {
            case "entities_module":
                booleans[Booleans.MODULE_ENTITY.ordinal()] = state;
                return true;
            case "entities_editor":
                booleans[Booleans.ENTITY_EDITOR.ordinal()] = state;
                return true;
            case "entities_limiter":
                booleans[Booleans.ENTITY_LIMITER.ordinal()] = state;
                return true;
            case "entities_breeding":
                booleans[Booleans.BREEDING_LIMITER.ordinal()] = state;
                return true;
            default:
                return false;
        }
    }

    public static boolean changeState(String string) {
        switch (string) {
            case "entities_module":
                booleans[Booleans.MODULE_ENTITY.ordinal()] = !booleans[Booleans.MODULE_ENTITY.ordinal()];
                return true;
            case "entities_editor":
                booleans[Booleans.ENTITY_EDITOR.ordinal()] = !booleans[Booleans.ENTITY_EDITOR.ordinal()];
                return true;
            case "entities_limiter":
                booleans[Booleans.ENTITY_LIMITER.ordinal()] = !booleans[Booleans.ENTITY_LIMITER.ordinal()];
                return true;
            case "entities_breeding":
                booleans[Booleans.BREEDING_LIMITER.ordinal()] = !booleans[Booleans.BREEDING_LIMITER.ordinal()];
                return true;
            default:
                return false;
        }
    }

    public static EntityControl getControl(EntityType entityType) {
        return entities[entityType.ordinal()];
    }

    public static String getString(Strings configName) {
        return strings[configName.ordinal()];
    }

    public static boolean getBoolean(Booleans configName) {
        return booleans[configName.ordinal()];
    }

    public static int getInteger(Integers configName) {
        return integers[configName.ordinal()];
    }

    public static double getDouble(Doubles configName) {
        return doubles[configName.ordinal()];
    }

    public static void setFilter(Pattern pattern) {
        filter = pattern;
    }

    public static Pattern getFilter() {
        return filter;
    }

    public static List<String> getStringList(Lists configName) {
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

    public static Map<String, Map<Double, List<String>>> getChanceMap(ChanceMaps configName) {
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
