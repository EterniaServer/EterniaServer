package br.com.eterniaserver.eterniaserver.API;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Vars;
import br.com.eterniaserver.eterniaserver.player.PlayerManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.atomic.AtomicReference;

public class Money {

    private final EterniaServer plugin;
    private final PlayerManager playerManager;
    private final Vars vars;

    public Money(EterniaServer plugin, PlayerManager playerManager, Vars vars) {
        this.plugin = plugin;
        this.playerManager = playerManager;
        this.vars = vars;
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

        AtomicReference<Double> value = new AtomicReference<>(0.0);
        if (playerManager.playerMoneyExist(playerName)) {
            final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-money") + " WHERE player_name='" + playerName + "';";
            plugin.connections.executeSQLQuery(connection -> {
                PreparedStatement get_balance = connection.prepareStatement(querie);
                ResultSet resultSet = get_balance.executeQuery();
                if (resultSet.next() && resultSet.getDouble("balance") != 0) {
                    value.set(resultSet.getDouble("balance"));
                }
            });
        } else {
            playerManager.playerMoneyCreate(playerName);
            value.set(300.0);
        }

        vars.balances.put(playerName, value.get());
        return value.get();
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
            final String querie = "UPDATE " + plugin.serverConfig.getString("sql.table-money") + " SET balance='" + amount + "' WHERE player_name='" + playerName + "';";
            plugin.connections.executeSQLQuery(connection -> {
                PreparedStatement setmoney = connection.prepareStatement(querie);
                setmoney.execute();
                setmoney.close();
            }, true);
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
