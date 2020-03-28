package eternia.api;

import eternia.EterniaServer;
import eternia.configs.Vars;
import eternia.player.PlayerManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MoneyAPI {

    public static double getMoney(String playerName) {
        if (Vars.money.containsKey(playerName)) {
            return Vars.money.get(playerName);
        }
        double i = 0;
        if (PlayerManager.PlayerExist(playerName)) {
            try {
                final ResultSet rs = EterniaServer.sqlcon.Query("SELECT * FROM eternia WHERE NAME='" + playerName + "';");
                if (rs.next()) {
                    rs.getDouble("BALANCE");
                }
                i = rs.getDouble("BALANCE");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            PlayerManager.CreatePlayer(playerName);
            i = 300;
        }
        Vars.money.put(playerName, i);
        return i;
    }

    public static boolean hasMoney(String playerName, double Money) {
        return getMoney(playerName) >= Money;
    }

    public static void setMoney(String playerName, double Money) {
        if (PlayerManager.PlayerExist(playerName)) {
            Vars.money.remove(playerName);
            Vars.money.put(playerName, Money);
            EterniaServer.sqlcon.Update("UPDATE eternia SET BALANCE='" + Money + "' WHERE NAME='" + playerName + "';");
        } else {
            PlayerManager.CreatePlayer(playerName);
            setMoney(playerName, Money);
        }
    }

    public static void addMoney(String playerName, double money) {
        if (PlayerManager.PlayerExist(playerName)) {
            setMoney(playerName, getMoney(playerName) + money);
        } else {
            PlayerManager.CreatePlayer(playerName);
            addMoney(playerName, getMoney(playerName) + money);
        }
    }

    public static void removeMoney(String playerName, double money) {
        if (PlayerManager.PlayerExist(playerName)) {
            setMoney(playerName, getMoney(playerName) - money);
        } else {
            PlayerManager.CreatePlayer(playerName);
            removeMoney(playerName, getMoney(playerName) - money);
        }
    }

}