package br.com.eterniaserver.API;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.player.PlayerManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.atomic.AtomicReference;

public class Money {

    /**
     * Gets the money in player account
     * @param playerName to check
     * @return Amount currently held in player's account
     */
    public static double getMoney(String playerName) {
        if (Vars.balances.containsKey(playerName)) {
            return Vars.balances.get(playerName);
        }

        AtomicReference<Double> value = new AtomicReference<>(0.0);
        if (PlayerManager.playerMoneyExist(playerName)) {
            final String querie = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-money") + " WHERE player_name='" + playerName + "';";
            EterniaServer.connection.executeSQLQuery(connection -> {
                PreparedStatement get_balance = connection.prepareStatement(querie);
                ResultSet resultSet = get_balance.executeQuery();
                if (resultSet.next() && resultSet.getDouble("balance") != 0) {
                    value.set(resultSet.getDouble("balance"));
                }
            });
        } else {
            PlayerManager.playerMoneyCreate(playerName);
            value.set(300.0);
        }

        Vars.balances.put(playerName, value.get());
        return value.get();
    }

    /**
     * Return a boolean that indicate if the player has money enough.
     * @param playerName to check
     * @param amount to check
     * @return the boolean
     */
    public static boolean hasMoney(String playerName, double amount) {
        return getMoney(playerName) >= amount;
    }

    /**
     * Defines the amount money in player's account.
     * @param playerName to check
     * @param amount to set
     */
    public static void setMoney(String playerName, double amount) {
        if (PlayerManager.playerMoneyExist(playerName)) {
            Vars.balances.put(playerName, amount);
            final String querie = "UPDATE " + EterniaServer.configs.getString("sql.table-money") + " SET balance='" + amount + "' WHERE player_name='" + playerName + "';";
            EterniaServer.connection.executeSQLQuery(connection -> {
                PreparedStatement setmoney = connection.prepareStatement(querie);
                setmoney.execute();
                setmoney.close();
            }, true);
        } else {
            PlayerManager.playerMoneyCreate(playerName);
            setMoney(playerName, amount);
        }
    }

    /**
     * Adds money to player account.
     * @param playerName to check
     * @param amount to add
     */
    public static void addMoney(String playerName, double amount) {
        setMoney(playerName, getMoney(playerName) + amount);
    }

    /**
     * Removes money to player account.
     * @param playerName to check
     * @param amount to remove
     */
    public static void removeMoney(String playerName, double amount) {
        setMoney(playerName, getMoney(playerName) - amount);
    }

}
