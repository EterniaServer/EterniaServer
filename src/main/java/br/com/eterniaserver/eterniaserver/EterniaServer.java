package br.com.eterniaserver.eterniaserver;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.api.interfaces.CashAPI;
import br.com.eterniaserver.eterniaserver.api.interfaces.GUIAPI;
import br.com.eterniaserver.eterniaserver.api.interfaces.LocationManager;
import br.com.eterniaserver.eterniaserver.api.interfaces.UserManager;
import br.com.eterniaserver.eterniaserver.configurations.configs.*;
import br.com.eterniaserver.eterniaserver.configurations.locales.ConstantsCfg;
import br.com.eterniaserver.eterniaserver.configurations.locales.MsgCfg;
import br.com.eterniaserver.eterniaserver.craft.CraftEterniaServer;
import br.com.eterniaserver.eterniaserver.craft.CraftEconomy;
import br.com.eterniaserver.eterniaserver.craft.CraftUser;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.Doubles;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.enums.ItemsKeys;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.handlers.EntityHandler;
import br.com.eterniaserver.eterniaserver.handlers.PlayerHandler;
import br.com.eterniaserver.eterniaserver.handlers.ServerHandler;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.modules.MainConfigurations;
import br.com.eterniaserver.eterniaserver.modules.Manager;
import br.com.eterniaserver.eterniaserver.objects.EntityControl;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import org.bstats.bukkit.Metrics;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;

import java.util.List;


public class EterniaServer extends CraftEterniaServer {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    private final int[] integers = new int[Integers.values().length];
    private final int[] chestShopBuyRoof = new int[Material.values().length];
    private final int[] chestShopSellRoof = new int[Material.values().length];
    private final double[] doubles = new double[Doubles.values().length];
    private final boolean[] booleans = new boolean[Booleans.values().length];

    private final String[] messages = new String[Messages.values().length];
    private final String[] strings = new String[Strings.values().length];
    private final EntityControl[] entities = new EntityControl[EntityType.values().length];
    private final NamespacedKey[] namespaceKeys = new NamespacedKey[ItemsKeys.values().length];

    // APIs
    private UserManager userManager;
    private LocationManager locationManager;
    private static CraftEconomy economyAPI;
    private static CraftUser userAPI;
    private static GUIAPI guiAPI;
    private static CashAPI cashAPI;

    public UserManager userManager() { return userManager; }
    public LocationManager locationManager() { return locationManager; }
    public static CraftEconomy getEconomyAPI() { return economyAPI; }
    public static CraftUser getUserAPI() { return userAPI; }
    public static GUIAPI getGuiAPI() { return guiAPI; }
    public static CashAPI getCashAPI() { return cashAPI; }

    public void setGuiAPI(GUIAPI implementation) { guiAPI = implementation; }
    public void setCashAPI(CashAPI implementation) { cashAPI = implementation; }

    public int[] integers() {
        return integers;
    }

    public double[] doubles() {
        return doubles;
    }

    public boolean[] booleans() {
        return booleans;
    }

    public String[] strings() {
        return strings;
    }

    public String[] messages() {
        return messages;
    }

    public List<List<String>> stringLists() {
        return stringLists;
    }

    public NamespacedKey[] namespacedKeys() { return namespaceKeys; }

    @Override
    public void onEnable() {
        userAPI = new CraftUser(this);
        economyAPI = new CraftEconomy(this);

        loadConfiguration();

        new Metrics(this, 10160);
        new Managers(this);
        new MainConfigurations(this);

        userManager = new CraftUserManager(this);
        locationManager = new CraftLocationManager(this);

        new Manager(this);

        this.getServer().getPluginManager().registerEvents(new EntityHandler(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerHandler(this), this);
        this.getServer().getPluginManager().registerEvents(new ServerHandler(this), this);
    }

    private void loadConfiguration() {
        final ConstantsCfg constantsCfg = new ConstantsCfg(this, strings, namespaceKeys);
        final MsgCfg msgCfg = new MsgCfg(this, messages);
        final ConfigsCfg configsCfg = new ConfigsCfg(this, strings, booleans, integers, doubles);
        final CommandsCfg commandsCfg = new CommandsCfg(this);
        final ChatCfg chatCfg = new ChatCfg(this, strings, integers);
        final EntityCfg entityCfg = new EntityCfg(booleans, integers, entities);
        final KitsCfg kitsCfg = new KitsCfg(this);
        final ScheduleCfg scheduleCfg = new ScheduleCfg(this, integers);
        final EconomyCfg economyCfg = new EconomyCfg(this, booleans, doubles, strings, integers, chestShopBuyRoof, chestShopSellRoof);

        EterniaLib.addReloadableConfiguration("eterniaserver", "constants", constantsCfg);
        EterniaLib.addReloadableConfiguration("eterniaserver", "messages", msgCfg);
        EterniaLib.addReloadableConfiguration("eterniaserver", "configs", configsCfg);
        EterniaLib.addReloadableConfiguration("eterniaserver", "chat", chatCfg);
        EterniaLib.addReloadableConfiguration("eterniaserver", "entities", entityCfg);
        EterniaLib.addReloadableConfiguration("eterniaserver", "kits", kitsCfg);
        EterniaLib.addReloadableConfiguration("eterniaserver", "schedule", scheduleCfg);
        EterniaLib.addReloadableConfiguration("eterniaserver", "economy", economyCfg);

        constantsCfg.executeConfig();
        msgCfg.executeConfig();
        configsCfg.executeConfig();
        commandsCfg.executeConfig();
        chatCfg.executeConfig();
        entityCfg.executeConfig();
        kitsCfg.executeConfig();
        scheduleCfg.executeConfig();
        economyCfg.executeConfig();
        economyAPI.setUp(booleans);
        configsCfg.executeCritical();
    }

    public NamespacedKey getKey(final ItemsKeys entry) {
        return namespaceKeys[entry.ordinal()];
    }

    public EntityControl getControl(EntityType entityType) {
        return entities[entityType.ordinal()];
    }

    public String getString(Strings configName) {
        return strings[configName.ordinal()];
    }

    public boolean getBoolean(Booleans configName) {
        return booleans[configName.ordinal()];
    }

    public int getInteger(Integers configName) {
        return integers[configName.ordinal()];
    }

    public double getDouble(Doubles configName) {
        return doubles[configName.ordinal()];
    }

    public int getChestShopBuyRoof(Material material) {
        return chestShopBuyRoof[material.ordinal()];
    }

    public void saveEconomy(int[] chestShopBuyRoofNew, int[] chestShopSellRoofNew) {
        new EconomySaveCfg(this, chestShopBuyRoof, chestShopSellRoof).saveToFile(chestShopSellRoofNew, chestShopBuyRoofNew);
    }

    public int getChestShopSellRoof(Material material) {
        return chestShopSellRoof[material.ordinal()];
    }

    public void sendMiniMessages(CommandSender sender, Messages messagesId, String... args) {
        sendMiniMessages(sender, messagesId, true, args);

    }

    public void sendMiniMessages(CommandSender sender, Messages messagesId, boolean prefix, String... args) {
        sender.sendMessage(miniMessage.parse(getMessage(messagesId, prefix, args)));
    }

    public Component parseColor(String string) {
        return miniMessage.parse(string);
    }

    public void sendMessage(CommandSender sender, Messages messagesId, String... args) {
        sendMessage(sender, messagesId, true, args);
    }

    public void sendMessage(CommandSender sender, Messages messagesId, boolean prefix, String... args) {
        sender.sendMessage(getMessage(messagesId, prefix, args));
    }

    public Component getMiniMessage(Messages messagesId, boolean prefix, String... args) {
        return miniMessage.parse(getMessage(messagesId, prefix, args));
    }

    public String getMessage(Messages messagesId, boolean prefix, String... args) {
        String message = messages[messagesId.ordinal()];

        for (int i = 0; i < args.length; i++) {
            message = message.replace("{" + i + "}", args[i]);
        }

        if (prefix) {
            return strings[Strings.SERVER_PREFIX.ordinal()] + message;
        }

        return message;
    }

}
