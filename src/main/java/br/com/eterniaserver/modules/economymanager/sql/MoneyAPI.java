package br.com.eterniaserver.modules.economymanager.sql;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.player.PlayerManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.atomic.AtomicReference;

public class MoneyAPI {

    public static double getMoney(String playerName) {
        if (Vars.money.containsKey(playerName)) {
            return Vars.money.get(playerName);
        }

        AtomicReference<Double> money = new AtomicReference<>(0.0);
        if (PlayerManager.playerMoneyExist(playerName)) {
            final String querie = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-money") + " WHERE player_name='" + playerName + "';";
            EterniaServer.connection.executeSQLQuery(connection -> {
                PreparedStatement getshop = connection.prepareStatement(querie);
                ResultSet resultSet = getshop.executeQuery();
                if (resultSet.next() && resultSet.getDouble("balance") != 0) {
                    money.set(resultSet.getDouble("balance"));
                }
            });
        } else {
            PlayerManager.playerMoneyCreate(playerName);
            money.set(300.0);
        }

        Vars.money.put(playerName, money.get());
        return money.get();
    }

    public static boolean hasMoney(String playerName, double Money) {
        return getMoney(playerName) >= Money;
    }

    public static void setMoney(String playerName, double Money) {
        if (PlayerManager.playerMoneyExist(playerName)) {
            Vars.money.remove(playerName);
            Vars.money.put(playerName, Money);
            final String querie = "UPDATE " + EterniaServer.configs.getString("sql.table-money") + " SET balance='" + Money + "' WHERE player_name='" + playerName + "';";
            EterniaServer.connection.executeSQLQuery(connection -> {
                PreparedStatement setmoney = connection.prepareStatement(querie);
                setmoney.execute();
                setmoney.close();
            }, true);
        } else {
            PlayerManager.playerMoneyCreate(playerName);
            setMoney(playerName, Money);
        }
    }

    public static void addMoney(String playerName, double money) {
        if (PlayerManager.playerMoneyExist(playerName)) {
            setMoney(playerName, getMoney(playerName) + money);
        } else {
            PlayerManager.playerMoneyCreate(playerName);
            addMoney(playerName, getMoney(playerName) + money);
        }
    }

    public static void removeMoney(String playerName, double money) {
        if (PlayerManager.playerMoneyExist(playerName)) {
            setMoney(playerName, getMoney(playerName) - money);
        } else {
            PlayerManager.playerMoneyCreate(playerName);
            removeMoney(playerName, getMoney(playerName) - money);
        }
    }

}
