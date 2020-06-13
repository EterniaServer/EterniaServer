package br.com.eterniaserver.eterniaserver.modules.economymanager;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.objects.Vars;
import br.com.eterniaserver.eterniaserver.objects.PlayerManager;

public class EconomyManager {

    private final EterniaServer plugin;
    private final PlayerManager playerManager;
    private final Vars vars;

    public EconomyManager(EterniaServer plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getPlayerManager();
        this.vars = plugin.getVars();
    }

    /**
     * Gets the money in player account
     * @param playerName to check
     * @return Amount currently held in player's account
     */
    public double getMoney(String playerName) {
        if (vars.balances.containsKey(playerName)) {
            return vars.balances.get(playerName);
        }

        double value;

        if (playerManager.playerMoneyExist(playerName)) {
            final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-money") + " WHERE player_name='" + playerName + "';";
            value = EQueries.queryDouble(querie, "balance");
        } else {
            playerManager.playerMoneyCreate(playerName);
            value = 300.0;
        }

        vars.balances.put(playerName, value);
        return value;
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
        if (playerManager.playerMoneyExist(playerName)) {
            vars.balances.put(playerName, amount);
            EQueries.executeQuery("UPDATE " + plugin.serverConfig.getString("sql.table-money") + " SET balance='" + amount + "' WHERE player_name='" + playerName + "';");
        } else {
            playerManager.playerMoneyCreate(playerName);
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
