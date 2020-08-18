package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.configs.Configs;
import br.com.eterniaserver.eterniaserver.configs.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import java.util.UUID;

public class APIEconomy {

    private APIEconomy() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Gets the money in player account
     * @param uuid to check
     * @return Amount currently held in player's account
     */
    public static double getMoney(UUID uuid) {
        if (Vars.balances.containsKey(uuid)) {
            return Vars.balances.get(uuid);
        } else {
            EQueries.executeQuery(Constants.getQueryInsert(Configs.tableMoney, Constants.UUID_STR, uuid.toString(), Constants.BALANCE_STR, EterniaServer.serverConfig.getDouble("money.start")));
            Vars.balances.put(uuid, 300.0);
            return 300.0;
        }
    }

    /**
     * Return a boolean that indicate if the player has money enough.
     * @param uuid to check
     * @param amount to check
     * @return the boolean
     */
    public static boolean hasMoney(UUID uuid, double amount) {
        return getMoney(uuid) >= amount;
    }

    /**
     * Defines the amount money in player's account.
     * @param uuid to check
     * @param amount to set
     */
    public static void setMoney(UUID uuid, double amount) {
        if (Vars.balances.containsKey(uuid)) {
            Vars.balances.put(uuid, amount);
            EQueries.executeQuery(Constants.getQueryUpdate(Configs.tableMoney, Constants.BALANCE_STR, amount, Constants.UUID_STR, uuid.toString()));
        } else {
            EQueries.executeQuery(Constants.getQueryInsert(Configs.tableMoney, Constants.UUID_STR, uuid.toString(), Constants.BALANCE_STR, EterniaServer.serverConfig.getDouble("money.start")));
            Vars.balances.put(uuid, 300.0);
            setMoney(uuid, amount);
        }
    }

    /**
     * Adds money to player account.
     * @param uuid to check
     * @param amount to add
     */
    public static void addMoney(UUID uuid, double amount) {
        setMoney(uuid, getMoney(uuid) + amount);
    }

    /**
     * Removes money to player account.
     * @param uuid to check
     * @param amount to remove
     */
    public static void removeMoney(UUID uuid, double amount) {
        setMoney(uuid, getMoney(uuid) - amount);
    }

}
