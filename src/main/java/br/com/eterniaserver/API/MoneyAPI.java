package br.com.eterniaserver.API;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.player.PlayerManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.atomic.AtomicReference;

public class MoneyAPI {

    /**
     *
     * @param player_name The player name in String.
     * @return The balance of player.
     */
    public static double getMoney(String player_name) {
        if (Vars.balances.containsKey(player_name)) {
            return Vars.balances.get(player_name);
        }

        AtomicReference<Double> value = new AtomicReference<>(0.0);
        if (PlayerManager.playerMoneyExist(player_name)) {
            final String querie = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-money") + " WHERE player_name='" + player_name + "';";
            EterniaServer.connection.executeSQLQuery(connection -> {
                PreparedStatement get_balance = connection.prepareStatement(querie);
                ResultSet resultSet = get_balance.executeQuery();
                if (resultSet.next() && resultSet.getDouble("balance") != 0) {
                    value.set(resultSet.getDouble("balance"));
                }
            });
        } else {
            PlayerManager.playerMoneyCreate(player_name);
            value.set(300.0);
        }

        Vars.balances.put(player_name, value.get());
        return value.get();
    }

    /**
     *
     * @param player_name The player name in String.
     * @param value The value of money.
     * @return A boolean that indicate if player has enough money.
     */
    public static boolean hasMoney(String player_name, double value) {
        return getMoney(player_name) >= value;
    }

    public static void setMoney(String player_name, double value) {
        if (PlayerManager.playerMoneyExist(player_name)) {
            Vars.balances.put(player_name, value);
            final String querie = "UPDATE " + EterniaServer.configs.getString("sql.table-money") + " SET balance='" + value + "' WHERE player_name='" + player_name + "';";
            EterniaServer.connection.executeSQLQuery(connection -> {
                PreparedStatement setmoney = connection.prepareStatement(querie);
                setmoney.execute();
                setmoney.close();
            }, true);
        } else {
            PlayerManager.playerMoneyCreate(player_name);
            setMoney(player_name, value);
        }
    }

    public static void removeMoney(String player_name, double value) {
        setMoney(player_name, getMoney(player_name) - value);
    }


    public static void addMoney(String player_name, double value) {
        setMoney(player_name, getMoney(player_name) + value);
    }

}
