package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.EterniaServer;

public class APIEconomy {

    /**
     * Gets the money in player account
     * @param playerName to check
     * @return Amount currently held in player's account
     */
    public static double getMoney(String playerName) {
        if (Vars.balances.containsKey(playerName)) {
            return Vars.balances.get(playerName);
        } else {
            EQueries.executeQuery("INSERT INTO " + EterniaServer.serverConfig.getString("sql.table-money") + " (player_name, balance) VALUES('" + playerName + "', '" + EterniaServer.serverConfig.getDouble("money.start") + "');");
            Vars.balances.put(playerName, 300.0);
            return 300.0;
        }
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
        if (Vars.balances.containsKey(playerName)) {
            Vars.balances.put(playerName, amount);
            EQueries.executeQuery("UPDATE " + EterniaServer.serverConfig.getString("sql.table-money") + " SET balance='" + amount + "' WHERE player_name='" + playerName + "';");
        } else {
            EQueries.executeQuery("INSERT INTO " + EterniaServer.serverConfig.getString("sql.table-money") + " (player_name, balance) VALUES('" + playerName + "', '" + EterniaServer.serverConfig.getDouble("money.start") + "');");
            Vars.balances.put(playerName, 300.0);
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
