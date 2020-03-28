package eternia.api;

import eternia.EterniaServer;
import eternia.configs.Vars;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExpAPI {

    public static Integer getExp(String playerName) {
        if (Vars.xp.containsKey(playerName)) {
            return Vars.xp.get(playerName);
        }
        int xp = 0;
        try {
            final ResultSet rs = EterniaServer.sqlcon.Query("SELECT XP FROM eternia WHERE NAME='" + playerName + "';");
            if (rs.next()) {
                rs.getInt("XP");
            }
            xp = rs.getInt("XP");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Vars.xp.put(playerName, xp);
        return xp;
    }

    public static void setExp(String playerName, int valor) {
        Vars.xp.remove(playerName);
        Vars.xp.put(playerName, valor);
        EterniaServer.sqlcon.Update("UPDATE eternia SET BALANCE='" + valor + "' WHERE NAME='" + playerName + "';");
    }

    public static void addExp(String playerName, int valor) {
        setExp(playerName, getExp(playerName) + valor);
    }

    public static Integer takeExp(String playerName) {
        setExp(playerName, 0);
        return getExp(playerName);
    }

}
