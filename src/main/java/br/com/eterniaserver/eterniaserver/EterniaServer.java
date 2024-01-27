package br.com.eterniaserver.eterniaserver;

import br.com.eterniaserver.eterniaserver.api.interfaces.CashAPI;
import br.com.eterniaserver.eterniaserver.api.interfaces.ChatAPI;
import br.com.eterniaserver.eterniaserver.api.interfaces.ExtraEconomyAPI;
import br.com.eterniaserver.eterniaserver.api.interfaces.GUIAPI;
import br.com.eterniaserver.eterniaserver.enums.*;
import br.com.eterniaserver.eterniaserver.modules.Manager;

import br.com.eterniaserver.eterniaserver.modules.entity.Utils.EntityControl;

import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EterniaServer extends JavaPlugin {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    private final int[] integers = new int[Integers.values().length];
    private final double[] doubles = new double[Doubles.values().length];
    private final boolean[] booleans = new boolean[Booleans.values().length];

    private final String[] messages = new String[Messages.values().length];
    private final EntityControl[] entities = new EntityControl[EntityType.values().length];
    private final String[] strings = new String[Strings.values().length];
    private final NamespacedKey[] namespaceKeys = new NamespacedKey[ItemsKeys.values().length];

    private final List<String> bankListName = new ArrayList<>();
    private final List<List<String>> stringLists = new ArrayList<>();
    private final List<Material> elevatorMaterials = new ArrayList<>();
    private final List<Map<String, Map<Double, List<String>>>> chanceMaps = new ArrayList<>();

    // APIs
    @Getter
    @Setter
    private static GUIAPI guiAPI;
    @Getter
    @Setter
    private static CashAPI cashAPI;
    @Getter
    @Setter
    private static ChatAPI chatAPI;
    @Getter
    @Setter
    private static Economy economyAPI;
    @Getter
    @Setter
    private static ExtraEconomyAPI extraEconomyAPI;

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

    public EntityControl[] entities() {
        return entities;
    }

    public List<List<String>> stringLists() {
        return stringLists;
    }

    public List<String> bankListName() {
        return bankListName;
    }

    public List<Material> elevatorMaterials() {
        return elevatorMaterials;
    }

    public List<Map<String, Map<Double, List<String>>>> chanceMaps() {
        return chanceMaps;
    }

    public NamespacedKey[] namespacedKeys() { return namespaceKeys; }

    @Override
    public void onEnable() {
        for (int i = 0; i < Lists.values().length; i++) {
            stringLists.add(new ArrayList<>());
        }
        for (int i = 0; i < ChanceMaps.values().length; i++) {
            chanceMaps.add(new HashMap<>());
        }

        new Metrics(this, 10160);
        new Manager(this);

        if (getBoolean(Booleans.HAS_ECONOMY_PLUGIN)) {
            RegisteredServiceProvider<Economy> rspEconomy = getServer().getServicesManager().getRegistration(Economy.class);

            if (rspEconomy != null) {
                EterniaServer.setEconomyAPI(rspEconomy.getProvider());
            }
        }
    }

    public NamespacedKey getKey(final ItemsKeys entry) {
        return namespaceKeys[entry.ordinal()];
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

    public EntityControl getControl(EntityType entityType) {
        return entities[entityType.ordinal()];
    }

    public List<String> getStringList(Lists configName) {
        return stringLists.get(configName.ordinal());
    }

    public Map<String, Map<Double, List<String>>> getChanceMap(ChanceMaps configName) {
        return chanceMaps.get(configName.ordinal());
    }

    public void sendMiniMessages(CommandSender sender, Messages messagesId, String... args) {
        sendMiniMessages(sender, messagesId, true, args);

    }

    public void sendMiniMessages(CommandSender sender, Messages messagesId, boolean prefix, String... args) {
        sender.sendMessage(miniMessage.deserialize(getMessage(messagesId, prefix, args)));
    }

    public String setPlaceholders(Player player, String message) {
        String playerName = player.getName();
        String displayName = PlainTextComponentSerializer.plainText().serialize(player.displayName());
        message = message.replace("%player_name%", playerName);
        message = message.replace("%player_displayname%", displayName);
        return PlaceholderAPI.setPlaceholders(player, message);
    }

    public Component parseColor(String string) {
        return parseColor(string, false);
    }

    public Component parseColor(String string, boolean prefix) {
        if (prefix) {
            string = strings[Strings.SERVER_PREFIX.ordinal()] + string;
        }

        return miniMessage.deserialize(string);
    }

    public Component getMiniMessage(Messages messagesId, boolean prefix, String... args) {
        return miniMessage.deserialize(getMessage(messagesId, prefix, args));
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
