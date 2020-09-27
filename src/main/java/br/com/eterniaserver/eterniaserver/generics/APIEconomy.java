package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.Configs;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public interface APIEconomy {

    /**
     * Get the amount formated
     * @param amount to format
     * @return the formated string value
     */
    static String format(double amount) {
        return PluginVars.df2.format(amount);
    }

    /**
     * Get the singular name of current
     * @return the name
     */
    static String singularName() {
        if (Configs.getInstance().moduleEconomy) {
            return Configs.getInstance().singularName;
        } else {
            return PluginVars.getEcon().currencyNameSingular();
        }
    }

    /**
     * Get the plural name of current
     * @return the name
     */
    static String pluralName() {
        if (Configs.getInstance().moduleEconomy) {
            return Configs.getInstance().pluralName;
        } else {
            return PluginVars.getEcon().currencyNamePlural();
        }
    }

    /**
     * Check if player has account
     * @param uuid uuid of player
     */
    static boolean hasAccount(UUID uuid) {
        if (Configs.getInstance().moduleEconomy) {
            return PluginVars.playerProfile.containsKey(uuid);
        } else {
            return PluginVars.getEcon().hasAccount(Bukkit.getOfflinePlayer(uuid));
        }
    }

    /**
     * Create a account for the player
     * @param uuid uuid of player
     */
    static void createAccount(UUID uuid) {
        if (Configs.getInstance().moduleEconomy) {
            final long time = System.currentTimeMillis();
            final String playerName = UUIDFetcher.getNameOf(uuid);
            EQueries.executeQuery(PluginConstants.getQueryInsert(Configs.getInstance().tablePlayer, "(uuid, player_name, time, last, hours, balance)",
                    "('" + uuid.toString() + "', '" + playerName + "', '" + time + "', '" + time + "', '" + 0 + "', '" + Configs.getInstance().startMoney + "')"));
            final PlayerProfile playerProfile = new PlayerProfile(
                    playerName,
                    time,
                    time,
                    0
            );
            playerProfile.balance = Configs.getInstance().startMoney;
            PluginVars.playerProfile.put(uuid, playerProfile);
        } else {
            PluginVars.getEcon().createPlayerAccount(Bukkit.getOfflinePlayer(uuid));
        }
    }

    /**
     * Get the balance of the player
     * @param uuid of player
     * @return the balance
     */
    static double getMoney(UUID uuid) {
        if (Configs.getInstance().moduleEconomy) {
            if (PluginVars.playerProfile.containsKey(uuid)) {
                return PluginVars.playerProfile.get(uuid).balance;
            } else {
                final long time = System.currentTimeMillis();
                final String playerName = UUIDFetcher.getNameOf(uuid);
                EQueries.executeQuery(PluginConstants.getQueryInsert(Configs.getInstance().tablePlayer, "(uuid, player_name, time, last, hours, balance)",
                        "('" + uuid.toString() + "', '" + playerName + "', '" + time + "', '" + time + "', '" + 0 + "', '" + Configs.getInstance().startMoney + "')"));
                final PlayerProfile playerProfile = new PlayerProfile(
                        playerName,
                        time,
                        time,
                        0
                );
                playerProfile.balance = Configs.getInstance().startMoney;
                PluginVars.playerProfile.put(uuid, playerProfile);
                return Configs.getInstance().startMoney;
            }
        } else {
            return PluginVars.getEcon().getBalance(Bukkit.getOfflinePlayer(uuid));
        }
    }

    /**
     * Check if the player has money enough
     * @param uuid of player
     * @param amount the amount of money needed
     * @return if has or not
     */
    static boolean hasMoney(UUID uuid, double amount) {
        if (Configs.getInstance().moduleEconomy) {
            return getMoney(uuid) >= amount;
        } else {
            return PluginVars.getEcon().has(Bukkit.getOfflinePlayer(uuid), amount);
        }
    }

    /**
     * Defines the balance of the player
     * @param uuid of player
     * @param amount the amount of money to set
     */
    static void setMoney(UUID uuid, double amount) {
        if (Configs.getInstance().moduleEconomy) {
            if (PluginVars.playerProfile.containsKey(uuid)) {
                PluginVars.playerProfile.get(uuid).balance = amount;
                EQueries.executeQuery(PluginConstants.getQueryUpdate(Configs.getInstance().tablePlayer, PluginConstants.BALANCE_STR, amount, PluginConstants.UUID_STR, uuid.toString()));
            } else {
                final long time = System.currentTimeMillis();
                final String playerName = UUIDFetcher.getNameOf(uuid);
                EQueries.executeQuery(PluginConstants.getQueryInsert(Configs.getInstance().tablePlayer, "(uuid, player_name, time, last, hours, balance)",
                        "('" + uuid.toString() + "', '" + playerName + "', '" + time + "', '" + time + "', '" + 0 + "', '" + Configs.getInstance().startMoney + "')"));
                final PlayerProfile playerProfile = new PlayerProfile(
                        playerName,
                        time,
                        time,
                        0
                );
                playerProfile.balance = Configs.getInstance().startMoney;
                PluginVars.playerProfile.put(uuid, playerProfile);
                setMoney(uuid, amount);
            }
        } else {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
            double balance = PluginVars.getEcon().getBalance(offlinePlayer);
            PluginVars.getEcon().withdrawPlayer(offlinePlayer, balance);
            PluginVars.getEcon().depositPlayer(offlinePlayer, amount);
        }
    }

    /**
     * Give money to player account
     * @param uuid of player
     * @param amount the amount of money to give
     */
    static void addMoney(UUID uuid, double amount) {
        if (Configs.getInstance().moduleEconomy) {
            setMoney(uuid, getMoney(uuid) + amount);
        } else {
            PluginVars.getEcon().depositPlayer(Bukkit.getOfflinePlayer(uuid), amount);
        }
    }

    /**
     * Give money to player account
     * @param uuid of player
     * @param amount the amount of money to give
     */
    static void removeMoney(UUID uuid, double amount) {
        if (Configs.getInstance().moduleEconomy) {
            setMoney(uuid, getMoney(uuid) - amount);
        } else {
            PluginVars.getEcon().withdrawPlayer(Bukkit.getOfflinePlayer(uuid), amount);
        }
    }

}
