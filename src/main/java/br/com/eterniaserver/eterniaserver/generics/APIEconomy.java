package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.Configs;

import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class APIEconomy {

    public static Economy econ;

    private APIEconomy() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Get the amount formated
     * @param amount to format
     * @return the formated string value
     */
    public static String format(double amount) {
        return PluginVars.df2.format(amount);
    }

    /**
     * Get the singular name of current
     * @return the name
     */
    public static String singularName() {
        if (Configs.instance.moduleEconomy) {
            return Configs.instance.singularName;
        } else {
            return econ.currencyNameSingular();
        }
    }

    /**
     * Get the plural name of current
     * @return the name
     */
    public static String pluralName() {
        if (Configs.instance.moduleEconomy) {
            return Configs.instance.pluralName;
        } else {
            return econ.currencyNamePlural();
        }
    }

    /**
     * Check if player has account
     * @param uuid uuid of player
     */
    public static boolean hasAccount(UUID uuid) {
        if (Configs.instance.moduleEconomy) {
            return PluginVars.playerProfile.containsKey(uuid);
        } else {
            return econ.hasAccount(Bukkit.getOfflinePlayer(uuid));
        }
    }

    /**
     * Create a account for the player
     * @param uuid uuid of player
     */
    public static void createAccount(UUID uuid) {
        if (Configs.instance.moduleEconomy) {
            final long time = System.currentTimeMillis();
            final String playerName = UUIDFetcher.getNameOf(uuid);
            EQueries.executeQuery(PluginConstants.getQueryInsert(Configs.instance.tablePlayer, "(uuid, player_name, time, last, hours, balance)",
                    "('" + uuid.toString() + "', '" + playerName + "', '" + time + "', '" + time + "', '" + 0 + "', '" + Configs.instance.startMoney + "')"));
            final PlayerProfile playerProfile = new PlayerProfile(
                    playerName,
                    time,
                    time,
                    0
            );
            playerProfile.balance = Configs.instance.startMoney;
            PluginVars.playerProfile.put(uuid, playerProfile);
        } else {
            econ.createPlayerAccount(Bukkit.getOfflinePlayer(uuid));
        }
    }

    /**
     * Get the balance of the player
     * @param uuid of player
     * @return the balance
     */
    public static double getMoney(UUID uuid) {
        if (Configs.instance.moduleEconomy) {
            if (PluginVars.playerProfile.containsKey(uuid)) {
                return PluginVars.playerProfile.get(uuid).balance;
            } else {
                final long time = System.currentTimeMillis();
                final String playerName = UUIDFetcher.getNameOf(uuid);
                EQueries.executeQuery(PluginConstants.getQueryInsert(Configs.instance.tablePlayer, "(uuid, player_name, time, last, hours, balance)",
                        "('" + uuid.toString() + "', '" + playerName + "', '" + time + "', '" + time + "', '" + 0 + "', '" + Configs.instance.startMoney + "')"));
                final PlayerProfile playerProfile = new PlayerProfile(
                        playerName,
                        time,
                        time,
                        0
                );
                playerProfile.balance = Configs.instance.startMoney;
                PluginVars.playerProfile.put(uuid, playerProfile);
                return Configs.instance.startMoney;
            }
        } else {
            return econ.getBalance(Bukkit.getOfflinePlayer(uuid));
        }
    }

    /**
     * Check if the player has money enough
     * @param uuid of player
     * @param amount the amount of money needed
     * @return if has or not
     */
    public static boolean hasMoney(UUID uuid, double amount) {
        if (Configs.instance.moduleEconomy) {
            return getMoney(uuid) >= amount;
        } else {
            return econ.has(Bukkit.getOfflinePlayer(uuid), amount);
        }
    }

    /**
     * Defines the balance of the player
     * @param uuid of player
     * @param amount the amount of money to set
     */
    public static void setMoney(UUID uuid, double amount) {
        if (Configs.instance.moduleEconomy) {
            if (PluginVars.playerProfile.containsKey(uuid)) {
                PluginVars.playerProfile.get(uuid).balance = amount;
                EQueries.executeQuery(PluginConstants.getQueryUpdate(Configs.instance.tablePlayer, PluginConstants.BALANCE_STR, amount, PluginConstants.UUID_STR, uuid.toString()));
            } else {
                final long time = System.currentTimeMillis();
                final String playerName = UUIDFetcher.getNameOf(uuid);
                EQueries.executeQuery(PluginConstants.getQueryInsert(Configs.instance.tablePlayer, "(uuid, player_name, time, last, hours, balance)",
                        "('" + uuid.toString() + "', '" + playerName + "', '" + time + "', '" + time + "', '" + 0 + "', '" + Configs.instance.startMoney + "')"));
                final PlayerProfile playerProfile = new PlayerProfile(
                        playerName,
                        time,
                        time,
                        0
                );
                playerProfile.balance = Configs.instance.startMoney;
                PluginVars.playerProfile.put(uuid, playerProfile);
                setMoney(uuid, amount);
            }
        } else {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
            double balance = econ.getBalance(offlinePlayer);
            econ.withdrawPlayer(offlinePlayer, balance);
            econ.depositPlayer(offlinePlayer, amount);
        }
    }

    /**
     * Give money to player account
     * @param uuid of player
     * @param amount the amount of money to give
     */
    public static void addMoney(UUID uuid, double amount) {
        if (Configs.instance.moduleEconomy) {
            setMoney(uuid, getMoney(uuid) + amount);
        } else {
            econ.depositPlayer(Bukkit.getOfflinePlayer(uuid), amount);
        }
    }

    /**
     * Give money to player account
     * @param uuid of player
     * @param amount the amount of money to give
     */
    public static void removeMoney(UUID uuid, double amount) {
        if (Configs.instance.moduleEconomy) {
            setMoney(uuid, getMoney(uuid) - amount);
        } else {
            econ.withdrawPlayer(Bukkit.getOfflinePlayer(uuid), amount);
        }
    }

}
