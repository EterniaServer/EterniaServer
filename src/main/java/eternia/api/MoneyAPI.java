package eternia.api;

import eternia.EterniaServer;
import eternia.configs.Vars;
import eternia.player.PlayerManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class MoneyAPI {

    public static double getMoney(UUID uuid) {
        if (Vars.money.containsKey(uuid.toString())) {
            return Vars.money.get(uuid.toString());
        }
        double i = 0;
        if (PlayerManager.PlayerExist(uuid)) {
            try {
                final ResultSet rs = EterniaServer.sqlcon.Query("SELECT * FROM eternia WHERE UUID='" + uuid.toString() + "';");
                if (rs.next()) {
                    rs.getDouble("BALANCE");
                }
                i = rs.getDouble("BALANCE");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            PlayerManager.CreatePlayer(uuid);
            getMoney(uuid);
        }
        Vars.money.put(uuid.toString(), i);
        return i;
    }

    public static boolean hasMoney(UUID uuid, double Money) {
        return getMoney(uuid) >= Money;
    }

    public static void setMoney(UUID uuid, double Money) {
        if (PlayerManager.PlayerExist(uuid)) {
            Vars.money.remove(uuid.toString());
            Vars.money.put(uuid.toString(), Money);
            EterniaServer.sqlcon.Update("UPDATE eternia SET BALANCE='" + Money + "' WHERE UUID='" + uuid.toString() + "';");
        } else {
            PlayerManager.CreatePlayer(uuid);
            setMoney(uuid, Money);
        }
    }

    public static void addMoney(UUID uuid, double money) {
        if (PlayerManager.PlayerExist(uuid)) {
            setMoney(uuid, getMoney(uuid) + money);
        } else {
            PlayerManager.CreatePlayer(uuid);
            addMoney(uuid, getMoney(uuid) + money);
        }
    }

    public static void removeMoney(UUID uuid, double money) {
        if (PlayerManager.PlayerExist(uuid)) {
            setMoney(uuid, getMoney(uuid) - money);
        } else {
            PlayerManager.CreatePlayer(uuid);
            removeMoney(uuid, getMoney(uuid) - money);
        }
    }

}