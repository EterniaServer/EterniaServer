package com.eterniaserver.api;

import com.eterniaserver.configs.Vars;
import com.eterniaserver.player.PlayerManager;
import com.eterniaserver.EterniaServer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MoneyAPI {

    public static double getMoney(String playerName) {
        if (Vars.money.containsKey(playerName)) {
            return Vars.money.get(playerName);
        }
        double i = 0;
        if (PlayerManager.PlayerExistE(playerName)) {
            try {
                final ResultSet rs = EterniaServer.sqlcon.Query("SELECT * FROM economy WHERE player_name='" + playerName + "';");
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
            EterniaServer.sqlcon.Update("UPDATE economy SET balance='" + Money + "' WHERE player_name='" + playerName + "';");
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
