package br.com.eterniaserver.eterniaserver.core;

import br.com.eterniaserver.eternialib.SQL;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eternialib.sql.queries.Insert;
import br.com.eterniaserver.eternialib.sql.queries.Update;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.objects.BalanceTop;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.Doubles;
import br.com.eterniaserver.eterniaserver.enums.Lists;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.sql.Connection;
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
     * @return the formatted string value
     */
    static String format(double amount) {
        return Vars.getDf2().format(amount);
    }

    /**
     * Get the singular name of current
     * @return the name
     */
    static String singularName() {
        if (EterniaServer.getBoolean(Booleans.MODULE_ECONOMY)) {
            return EterniaServer.getString(Strings.MONEY_SINGULAR);
        } else {
            return Vars.getEcon().currencyNameSingular();
        }
    }

    /**
     * Get the plural name of current
     * @return the name
     */
    static String pluralName() {
        if (EterniaServer.getBoolean(Booleans.MODULE_ECONOMY)) {
            return EterniaServer.getString(Strings.MONEY_PLURAL);
        } else {
            return Vars.getEcon().currencyNamePlural();
        }
    }

    /**
     * Check if player has account
     * @param uuid uuid of player
     */
    static boolean hasAccount(UUID uuid) {
        if (EterniaServer.getBoolean(Booleans.MODULE_ECONOMY)) {
            return Vars.playerProfile.containsKey(uuid);
        } else {
            return Vars.getEcon().hasAccount(Bukkit.getOfflinePlayer(uuid));
        }
    }

    /**
     * Create a account for the player
     * @param uuid uuid of player
     */
    static void createAccount(UUID uuid) {
        if (EterniaServer.getBoolean(Booleans.MODULE_ECONOMY)) {
            final long time = System.currentTimeMillis();
            final String playerName = UUIDFetcher.getNameOf(uuid);

            Insert insert = new Insert(EterniaServer.getString(Strings.TABLE_PLAYER));
            insert.columns.set("uuid", "player_name", "time", "last", "hours", "balance");
            insert.values.set(uuid.toString(), playerName, time, time, 0, EterniaServer.getDouble(Doubles.START_MONEY));
            SQL.executeAsync(insert);

            final PlayerProfile playerProfile = new PlayerProfile(
                    playerName,
                    time,
                    time,
                    0
            );

            playerProfile.setBalance(EterniaServer.getDouble(Doubles.START_MONEY));
            Vars.playerProfile.put(uuid, playerProfile);
        } else {
            Vars.getEcon().createPlayerAccount(Bukkit.getOfflinePlayer(uuid));
        }
    }

    /**
     * Get the balance of the player
     * @param uuid of player
     * @return the balance
     */
    static double getMoney(UUID uuid) {
        if (EterniaServer.getBoolean(Booleans.MODULE_ECONOMY)) {
            if (Vars.playerProfile.containsKey(uuid)) {
                return Vars.playerProfile.get(uuid).getBalance();
            } else {
                final long time = System.currentTimeMillis();
                final String playerName = UUIDFetcher.getNameOf(uuid);

                Insert insert = new Insert(EterniaServer.getString(Strings.TABLE_PLAYER));
                insert.columns.set("uuid", "player_name", "time", "last", "hours", "balance");
                insert.values.set(uuid.toString(), playerName, time, time, 0, EterniaServer.getDouble(Doubles.START_MONEY));
                SQL.executeAsync(insert);

                final PlayerProfile playerProfile = new PlayerProfile(
                        playerName,
                        time,
                        time,
                        0
                );
                playerProfile.setBalance(EterniaServer.getDouble(Doubles.START_MONEY));
                Vars.playerProfile.put(uuid, playerProfile);
                return EterniaServer.getDouble(Doubles.START_MONEY);
            }
        } else {
            return Vars.getEcon().getBalance(Bukkit.getOfflinePlayer(uuid));
        }
    }

    /**
     * Check if the player has money enough
     * @param uuid of player
     * @param amount the amount of money needed
     * @return if has or not
     */
    static boolean hasMoney(UUID uuid, double amount) {
        if (EterniaServer.getBoolean(Booleans.MODULE_ECONOMY)) {
            return getMoney(uuid) >= amount;
        } else {
            return Vars.getEcon().has(Bukkit.getOfflinePlayer(uuid), amount);
        }
    }

    /**
     * Defines the balance of the player
     * @param uuid of player
     * @param amount the amount of money to set
     */
    static void setMoney(UUID uuid, double amount) {
        if (EterniaServer.getBoolean(Booleans.MODULE_ECONOMY)) {
            if (Vars.playerProfile.containsKey(uuid)) {
                Vars.playerProfile.get(uuid).setBalance(amount);

                Update update = new Update(EterniaServer.getString(Strings.TABLE_PLAYER));
                update.set.set("balance", amount);
                update.where.set("uuid", uuid.toString());
                SQL.executeAsync(update);
            } else {
                final long time = System.currentTimeMillis();
                final String playerName = UUIDFetcher.getNameOf(uuid);

                Insert insert = new Insert(EterniaServer.getString(Strings.TABLE_PLAYER));
                insert.columns.set("uuid", "player_name", "time", "last", "hours", "balance");
                insert.values.set(uuid.toString(), playerName, time, time, 0, EterniaServer.getDouble(Doubles.START_MONEY));
                SQL.executeAsync(insert);

                final PlayerProfile playerProfile = new PlayerProfile(
                        playerName,
                        time,
                        time,
                        0
                );
                playerProfile.setBalance(EterniaServer.getDouble(Doubles.START_MONEY));
                Vars.playerProfile.put(uuid, playerProfile);
                setMoney(uuid, amount);
            }
        } else {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
            double balance = Vars.getEcon().getBalance(offlinePlayer);
            Vars.getEcon().withdrawPlayer(offlinePlayer, balance);
            Vars.getEcon().depositPlayer(offlinePlayer, amount);
        }
    }

    /**
     * Give money to player account
     * @param uuid of player
     * @param amount the amount of money to give
     */
    static void addMoney(UUID uuid, double amount) {
        if (EterniaServer.getBoolean(Booleans.MODULE_ECONOMY)) {
            setMoney(uuid, getMoney(uuid) + amount);
        } else {
            Vars.getEcon().depositPlayer(Bukkit.getOfflinePlayer(uuid), amount);
        }
    }

    /**
     * Give money to player account
     * @param uuid of player
     * @param amount the amount of money to give
     */
    static void removeMoney(UUID uuid, double amount) {
        if (EterniaServer.getBoolean(Booleans.MODULE_ECONOMY)) {
            setMoney(uuid, getMoney(uuid) - amount);
        } else {
            Vars.getEcon().withdrawPlayer(Bukkit.getOfflinePlayer(uuid), amount);
        }
    }

    /**
     * Check if the player is the top money
     * @param uuid of player
     * @return if the player was top money or not
     */
    static boolean isBalanceTop(UUID uuid) {
        if (Vars.balltop == null) {
            updateBalanceTop(20);
        }
        return Vars.balltop.equals(uuid);
    }

    /**
     * Update the baltop list and return And
     * return if the list was updated or not
     * @param size the size of list
     * @return if the list was updated or not
     */
    static CompletableFuture<Boolean> updateBalanceTop(int size) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = SQL.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(new BalanceTop(EterniaServer.getString(Strings.TABLE_PLAYER), size).queryString());
                statement.execute();
                ResultSet resultSet = statement.getResultSet();
                final List<UUID> tempList = new ArrayList<>();
                UUID uuid;
                while (resultSet.next()) {
                    if (tempList.size() < size) {
                        uuid = UUID.fromString(resultSet.getString("uuid"));
                        if (!EterniaServer.getStringList(Lists.BLACKLISTED_BALANCE_TOP).contains(UUIDFetcher.getNameOf(uuid))) {
                            tempList.add(uuid);
                        }
                    }
                }
                Vars.baltopTime = System.currentTimeMillis();
                Vars.baltopList.clear();
                Vars.baltopList.addAll(tempList);
                resultSet.close();
                statement.close();
            } catch (SQLException ignored) {
                APIServer.logError("Erro ao se conectar com a database", 3);
                return false;
            }
            return true;
        });
    }

    /**
     * Get the baltop list
     * @return the baltop list
     */
    static List<UUID> getBalanceTop() {
        return Vars.baltopList;
    }

    /**
     * Get the time from the last update
     * @return the time in long
     */
    static long getBalanceTopTime() {
        return Vars.baltopTime;
    }

}
