package br.com.eterniaserver.modules.economymanager.sql;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.player.PlayerManager;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Queries {

    public static double getMoney(String playerName) {
        if (Vars.money.containsKey(playerName)) {
            return Vars.money.get(playerName);
        }
        double i = 0;
        if (PlayerManager.PlayerExistE(playerName)) {
            try {
                final String querie = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-money") + " WHERE player_name='" + playerName + "';";
                final ResultSet rs = EterniaServer.sqlcon.Query(querie);
                if (rs.next()) {
                    rs.getDouble("balance");
                }
                i = rs.getDouble("balance");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            PlayerManager.CreateEconomy(playerName);
            i = 300;
        }
        Vars.money.put(playerName, i);
        return i;
    }

    public static boolean hasMoney(String playerName, double Money) {
        return getMoney(playerName) >= Money;
    }

    public static void setMoney(String playerName, double Money) {
        if (PlayerManager.PlayerExistE(playerName)) {
            Vars.money.remove(playerName);
            Vars.money.put(playerName, Money);
            final String querie = "UPDATE " + EterniaServer.configs.getString("sql.table-money") + " SET balance='" + Money + "' WHERE player_name='" + playerName + "';";
            EterniaServer.sqlcon.Update(querie);
        } else {
            PlayerManager.CreateEconomy(playerName);
            setMoney(playerName, Money);
        }
    }

    public static void addMoney(String playerName, double money) {
        if (PlayerManager.PlayerExistE(playerName)) {
            setMoney(playerName, getMoney(playerName) + money);
        } else {
            PlayerManager.CreateEconomy(playerName);
            addMoney(playerName, getMoney(playerName) + money);
        }
    }

    public static void removeMoney(String playerName, double money) {
        if (PlayerManager.PlayerExistE(playerName)) {
            setMoney(playerName, getMoney(playerName) - money);
        } else {
            PlayerManager.CreateEconomy(playerName);
            removeMoney(playerName, getMoney(playerName) - money);
        }
    }

}
