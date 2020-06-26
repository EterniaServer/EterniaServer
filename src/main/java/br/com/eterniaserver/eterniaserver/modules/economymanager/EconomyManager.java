package br.com.eterniaserver.eterniaserver.modules.economymanager;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.EterniaServer;

public class EconomyManager {

    private final EterniaServer plugin;

    public EconomyManager(EterniaServer plugin) {
        this.plugin = plugin;
    }

    /**
     * Gets the money in player account
     * @param playerName to check
     * @return Amount currently held in player's account
     */
    public double getMoney(String playerName) {
        if (plugin.getBalances().containsKey(playerName)) {
            return plugin.getBalances().get(playerName);
        } else {
            EQueries.executeQuery("INSERT INTO " + plugin.serverConfig.getString("sql.table-money") + " (player_name, balance) VALUES('" + playerName + "', '" + plugin.serverConfig.getDouble("money.start") + "');");
            plugin.getBalances().put(playerName, 300.0);
            return 300.0;
        }
    }

    /**
     * Return a boolean that indicate if the player has money enough.
     * @param playerName to check
     * @param amount to check
     * @return the boolean
     */
    public boolean hasMoney(String playerName, double amount) {
        return getMoney(playerName) >= amount;
    }

    /**
     * Defines the amount money in player's account.
     * @param playerName to check
     * @param amount to set
     */
    public void setMoney(String playerName, double amount) {
        if (plugin.getBalances().containsKey(playerName)) {
            plugin.getBalances().put(playerName, amount);
            EQueries.executeQuery("UPDATE " + plugin.serverConfig.getString("sql.table-money") + " SET balance='" + amount + "' WHERE player_name='" + playerName + "';");
        } else {
            EQueries.executeQuery("INSERT INTO " + plugin.serverConfig.getString("sql.table-money") + " (player_name, balance) VALUES('" + playerName + "', '" + plugin.serverConfig.getDouble("money.start") + "');");
            plugin.getBalances().put(playerName, 300.0);
            setMoney(playerName, amount);
        }
    }

    /**
     * Adds money to player account.
     * @param playerName to check
     * @param amount to add
     */
    public void addMoney(String playerName, double amount) {
        setMoney(playerName, getMoney(playerName) + amount);
    }

    /**
     * Removes money to player account.
     * @param playerName to check
     * @param amount to remove
     */
    public void removeMoney(String playerName, double amount) {
        setMoney(playerName, getMoney(playerName) - amount);
    }

}
