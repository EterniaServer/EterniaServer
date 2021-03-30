package br.com.eterniaserver.eterniaserver.craft;

import br.com.eterniaserver.eternialib.SQL;
import br.com.eterniaserver.eternialib.core.queries.Update;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configurations.dependencies.VaultInterface;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.Lists;
import br.com.eterniaserver.eterniaserver.enums.Strings;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CraftEconomy {

    private final EterniaServer plugin;

    private Economy economy;
    private NumberFormat numberFormat;
    private UUID baltop;
    private long baltopTime = 0;

    private final Map<UUID, Double> economyMap = new HashMap<>();
    private final Map<UUID, Double> economyOrdered = new LinkedHashMap<>();

    public CraftEconomy(final EterniaServer plugin) {
        this.plugin = plugin;
    }

    public void setUp(final boolean[] booleans) {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            booleans[Booleans.MODULE_ECONOMY.ordinal()] = false;
            return;
        }

        if (booleans[Booleans.MODULE_ECONOMY.ordinal()]) {
            Bukkit.getServer().getServicesManager().register(Economy.class, new VaultInterface(), plugin, ServicePriority.Highest);
            return;
        }

        final RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            booleans[Booleans.MODULE_ECONOMY.ordinal()] = false;
            return;
        }

        this.economy = rsp.getProvider();
    }

    public void putInMoney(UUID uuid, double money) {
        economyMap.put(uuid, money);
    }

    /**
     * Get the number format of plugin
     * @return the NumberFormat
     */
    public NumberFormat getNumberFormat() {
        if (numberFormat == null) {
            numberFormat = NumberFormat.getInstance(new Locale(plugin.getString(Strings.MONEY_LANGUAGE), plugin.getString(Strings.MONEY_COUNTRY)));
        }
        return numberFormat;
    }

    /**
     * Get the amount formated
     * @param amount to format
     * @return the formatted string value
     */
    public String format(double amount) {
        if (plugin.getBoolean(Booleans.MODULE_ECONOMY)) {
            return getNumberFormat().format(amount);
        }
        return economy.format(amount);
    }

    /**
     * Get the singular name of current
     * @return the name
     */
    public String singularName() {
        if (plugin.getBoolean(Booleans.MODULE_ECONOMY)) {
            return plugin.getString(Strings.MONEY_SINGULAR);
        }
        return economy.currencyNameSingular();
    }

    /**
     * Get the plural name of current
     * @return the name
     */
    public String pluralName() {
        if (plugin.getBoolean(Booleans.MODULE_ECONOMY)) {
            return plugin.getString(Strings.MONEY_PLURAL);
        }
        return economy.currencyNamePlural();
    }

    /**
     * Check if player has account
     * @param uuid uuid of player
     */
    public boolean hasAccount(UUID uuid) {
        if (plugin.getBoolean(Booleans.MODULE_ECONOMY)) {
            return EterniaServer.getUserAPI().hasProfile(uuid);
        }
        return economy.hasAccount(Bukkit.getOfflinePlayer(uuid));
    }

    /**
     * Create a account for the player
     * @param uuid uuid of player
     */
    public void createAccount(UUID uuid) {
        if (plugin.getBoolean(Booleans.MODULE_ECONOMY)) {
            EterniaServer.getUserAPI().createProfile(uuid, Bukkit.getOfflinePlayer(uuid).getName());
            return;
        }
        economy.createPlayerAccount(Bukkit.getOfflinePlayer(uuid));
    }

    /**
     * Get the balance of the player
     * @param uuid of player
     * @return the balance
     */
    public double getMoney(UUID uuid) {
        if (plugin.getBoolean(Booleans.MODULE_ECONOMY)) {
            if (!EterniaServer.getUserAPI().hasProfile(uuid)) {
                EterniaServer.getUserAPI().createProfile(uuid, Bukkit.getOfflinePlayer(uuid).getName());
            }
            return economyMap.get(uuid);
        }
        return economy.getBalance(Bukkit.getOfflinePlayer(uuid));
    }

    /**
     * Check if the player has money enough
     * @param uuid of player
     * @param amount the amount of money needed
     * @return if has or not
     */
    public boolean hasMoney(UUID uuid, double amount) {
        if (plugin.getBoolean(Booleans.MODULE_ECONOMY)) {
            return getMoney(uuid) >= amount;
        }
        return economy.has(Bukkit.getOfflinePlayer(uuid), amount);
    }

    /**
     * Defines the balance of the player
     * @param uuid of player
     * @param amount the amount of money to set
     */
    public void setMoney(UUID uuid, double amount) {
        if (plugin.getBoolean(Booleans.MODULE_ECONOMY)) {
            if (!EterniaServer.getUserAPI().hasProfile(uuid)) {
                EterniaServer.getUserAPI().createProfile(uuid, Bukkit.getOfflinePlayer(uuid).getName());
            }
            economyMap.put(uuid, amount);
            Update update = new Update(plugin.getString(Strings.TABLE_PLAYER));
            update.set.set("balance", amount);
            update.where.set("uuid", uuid.toString());
            SQL.executeAsync(update);
            return;
        }
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        economy.withdrawPlayer(offlinePlayer, economy.getBalance(offlinePlayer));
        economy.depositPlayer(offlinePlayer, amount);
    }

    /**
     * Give money to player account
     * @param uuid of player
     * @param amount the amount of money to give
     */
    public void addMoney(UUID uuid, double amount) {
        if (plugin.getBoolean(Booleans.MODULE_ECONOMY)) {
            setMoney(uuid, getMoney(uuid) + amount);
            return;
        }
        economy.depositPlayer(Bukkit.getOfflinePlayer(uuid), amount);
    }

    /**
     * Give money to player account
     * @param uuid of player
     * @param amount the amount of money to give
     */
    public void removeMoney(UUID uuid, double amount) {
        if (plugin.getBoolean(Booleans.MODULE_ECONOMY)) {
            setMoney(uuid, getMoney(uuid) - amount);
            return;
        }
        economy.withdrawPlayer(Bukkit.getOfflinePlayer(uuid), amount);
    }

    /**
     * Check if the player is the top money
     * @param uuid of player
     * @return if the player was top money or not
     */
    public boolean isBalanceTop(UUID uuid) {
        if (baltop == null) {
            updateBalanceTop();
        }
        return baltop.equals(uuid);
    }

    /**
     * Update the baltop list and return And
     * return if the list was updated or not
     * @return if the list was updated or not
     */
    public CompletableFuture<Boolean> updateBalanceTop() {
        return CompletableFuture.supplyAsync(() -> {
            List<Map.Entry<UUID, Double>> list = new ArrayList<>(economyMap.entrySet());

            list.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

            economyOrdered.clear();
            baltopTime = System.currentTimeMillis();
            baltop = list.get(0).getKey();
            for (Map.Entry<UUID, Double> entry : list) {
                if (!plugin.getStringList(Lists.BLACKLISTED_BALANCE_TOP).contains(Bukkit.getOfflinePlayer(entry.getKey()).getName())) {
                    economyOrdered.put(entry.getKey(), entry.getValue());
                }
            }
            return true;
        });
    }

    /**
     * Get the baltop list
     * @return the baltop list
     */
    public Map<UUID, Double> getBalanceTop() {
        return economyOrdered;
    }

    /**
     * Get the time from the last update
     * @return the time in long
     */
    public long getBalanceTopTime() {
        return baltopTime;
    }

    /**
     * Return the default message to a
     * not supported function.
     * @return the string of message
     */
    public String notSupported() {
        return plugin.getString(Strings.NOT_SUPPORTED);
    }

}
