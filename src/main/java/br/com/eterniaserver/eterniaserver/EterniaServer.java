package br.com.eterniaserver.eterniaserver;

import br.com.eterniaserver.eternialib.chat.MessageMap;
import br.com.eterniaserver.eterniaserver.api.interfaces.CashAPI;
import br.com.eterniaserver.eterniaserver.api.interfaces.ChatAPI;
import br.com.eterniaserver.eterniaserver.api.interfaces.ExtraEconomyAPI;
import br.com.eterniaserver.eterniaserver.api.interfaces.GUIAPI;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.ChanceMaps;
import br.com.eterniaserver.eterniaserver.enums.Doubles;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.enums.ItemsKeys;
import br.com.eterniaserver.eterniaserver.enums.Lists;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.modules.Manager;
import br.com.eterniaserver.eterniaserver.modules.entity.Utils.EntityControl;

import lombok.Getter;
import lombok.Setter;

import me.clip.placeholderapi.PlaceholderAPI;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import net.milkbowl.vault.economy.Economy;

import br.com.eterniaserver.bstats.bukkit.Metrics;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EterniaServer extends JavaPlugin {

    private final int[] integers = new int[Integers.values().length];
    private final double[] doubles = new double[Doubles.values().length];
    private final boolean[] booleans = new boolean[Booleans.values().length];

    private final MessageMap<Messages, String> messages = new MessageMap<>(Messages.class, Messages.SERVER_PREFIX);
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

    public MessageMap<Messages, String> messages() {
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

    public String setPlaceholders(Player player, String message) {
        String playerName = player.getName();
        String displayName = PlainTextComponentSerializer.plainText().serialize(player.displayName());
        message = message.replace("%player_name%", playerName);
        message = message.replace("%player_displayname%", displayName);
        return PlaceholderAPI.setPlaceholders(player, message);
    }

}
