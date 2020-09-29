package br.com.eterniaserver.eterniaserver.core;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eternialib.sql.Connections;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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
        if (EterniaServer.configs.moduleEconomy) {
            return EterniaServer.configs.singularName;
        } else {
            return PluginVars.getEcon().currencyNameSingular();
        }
    }

    /**
     * Get the plural name of current
     * @return the name
     */
    static String pluralName() {
        if (EterniaServer.configs.moduleEconomy) {
            return EterniaServer.configs.pluralName;
        } else {
            return PluginVars.getEcon().currencyNamePlural();
        }
    }

    /**
     * Check if player has account
     * @param uuid uuid of player
     */
    static boolean hasAccount(UUID uuid) {
        if (EterniaServer.configs.moduleEconomy) {
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
        if (EterniaServer.configs.moduleEconomy) {
            final long time = System.currentTimeMillis();
            final String playerName = UUIDFetcher.getNameOf(uuid);
            EQueries.executeQuery(PluginConstants.getQueryInsert(EterniaServer.configs.tablePlayer, "(uuid, player_name, time, last, hours, balance)",
                    "('" + uuid.toString() + "', '" + playerName + "', '" + time + "', '" + time + "', '" + 0 + "', '" + EterniaServer.configs.startMoney + "')"));
            final PlayerProfile playerProfile = new PlayerProfile(
                    playerName,
                    time,
                    time,
                    0
            );
            playerProfile.setBalance(EterniaServer.configs.startMoney);
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
        if (EterniaServer.configs.moduleEconomy) {
            if (PluginVars.playerProfile.containsKey(uuid)) {
                return PluginVars.playerProfile.get(uuid).getBalance();
            } else {
                final long time = System.currentTimeMillis();
                final String playerName = UUIDFetcher.getNameOf(uuid);
                EQueries.executeQuery(PluginConstants.getQueryInsert(EterniaServer.configs.tablePlayer, "(uuid, player_name, time, last, hours, balance)",
                        "('" + uuid.toString() + "', '" + playerName + "', '" + time + "', '" + time + "', '" + 0 + "', '" + EterniaServer.configs.startMoney + "')"));
                final PlayerProfile playerProfile = new PlayerProfile(
                        playerName,
                        time,
                        time,
                        0
                );
                playerProfile.setBalance(EterniaServer.configs.startMoney);
                PluginVars.playerProfile.put(uuid, playerProfile);
                return EterniaServer.configs.startMoney;
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
        if (EterniaServer.configs.moduleEconomy) {
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
        if (EterniaServer.configs.moduleEconomy) {
            if (PluginVars.playerProfile.containsKey(uuid)) {
                PluginVars.playerProfile.get(uuid).setBalance(amount);
                EQueries.executeQuery(PluginConstants.getQueryUpdate(EterniaServer.configs.tablePlayer, PluginConstants.BALANCE_STR, amount, PluginConstants.UUID_STR, uuid.toString()));
            } else {
                final long time = System.currentTimeMillis();
                final String playerName = UUIDFetcher.getNameOf(uuid);
                EQueries.executeQuery(PluginConstants.getQueryInsert(EterniaServer.configs.tablePlayer, "(uuid, player_name, time, last, hours, balance)",
                        "('" + uuid.toString() + "', '" + playerName + "', '" + time + "', '" + time + "', '" + 0 + "', '" + EterniaServer.configs.startMoney + "')"));
                final PlayerProfile playerProfile = new PlayerProfile(
                        playerName,
                        time,
                        time,
                        0
                );
                playerProfile.setBalance(EterniaServer.configs.startMoney);
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
        if (EterniaServer.configs.moduleEconomy) {
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
        if (EterniaServer.configs.moduleEconomy) {
            setMoney(uuid, getMoney(uuid) - amount);
        } else {
            PluginVars.getEcon().withdrawPlayer(Bukkit.getOfflinePlayer(uuid), amount);
        }
    }

    /**
     * Check if the player is the top money
     * @param uuid of player
     * @return if the player was top money or not
     */
    static boolean isBaltop(UUID uuid) {
        if (PluginVars.balltop == null) {
            updateBaltop(20);
        }
        return PluginVars.balltop.equals(uuid);
    }

    /**
     * Update the baltop list and return And
     * return if the list was updated or not
     * @param size the size of list
     * @return if the list was updated or not
     */
    static CompletableFuture<Boolean> updateBaltop(int size) {
        return CompletableFuture.supplyAsync(() -> {
            if (EterniaLib.getMySQL()) {
                EterniaLib.getConnections().executeSQLQuery(connection -> {
                    final PreparedStatement getHashMap = connection.prepareStatement(
                            "SELECT " + PluginConstants.UUID_STR +
                                    " FROM " + EterniaServer.configs.tablePlayer +
                                    " ORDER BY " + PluginConstants.BALANCE_STR + " DESC LIMIT " + size + ";");
                    final ResultSet resultSet = getHashMap.executeQuery();
                    final List<UUID> tempList = new ArrayList<>();
                    UUID uuid;
                    while (resultSet.next()) {
                        if (tempList.size() < 10) {
                            uuid = UUID.fromString(resultSet.getString(PluginConstants.UUID_STR));
                            if (!EterniaServer.configs.blacklistedBaltop.contains(UUIDFetcher.getNameOf(uuid))) {
                                tempList.add(uuid);
                            }
                        }
                    }
                    PluginVars.baltopTime = System.currentTimeMillis();
                    PluginVars.baltopList.clear();
                    PluginVars.baltopList.addAll(tempList);
                    getHashMap.close();
                    resultSet.close();
                });
            } else {
                try (PreparedStatement getHashMap = Connections.getSQLite().prepareStatement(
                        "SELECT " + PluginConstants.UUID_STR +
                                " FROM " + EterniaServer.configs.tablePlayer +
                                " ORDER BY " + PluginConstants.BALANCE_STR + " DESC LIMIT " + size + ";"); ResultSet resultSet = getHashMap.executeQuery()) {
                    final List<UUID> tempList = new ArrayList<>();
                    UUID uuid;
                    while (resultSet.next()) {
                        if (tempList.size() < 10) {
                            uuid = UUID.fromString(resultSet.getString(PluginConstants.UUID_STR));
                            if (!EterniaServer.configs.blacklistedBaltop.contains(UUIDFetcher.getNameOf(uuid))) {
                                if (tempList.isEmpty()) {
                                    PluginVars.balltop = uuid;
                                }
                                tempList.add(uuid);
                            }
                        }
                    }
                    PluginVars.baltopTime = System.currentTimeMillis();
                    PluginVars.baltopList.clear();
                    PluginVars.baltopList.addAll(tempList);
                } catch (SQLException ignored) {
                    APIServer.logError("Erro ao se conectar com a database", 3);
                    return false;
                }
            }
            return true;
        });
    }

    /**
     * Get the baltop list
     * @return the baltop list
     */
    static List<UUID> getBaltopList() {
        return PluginVars.baltopList;
    }

    /**
     * Get the time from the last update
     * @return the time in long
     */
    static long getBaltopTime() {
        return PluginVars.baltopTime;
    }

}
