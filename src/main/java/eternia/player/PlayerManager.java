package eternia.player;

import eternia.EterniaServer;
import eternia.configs.CVar;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerManager {
    public static boolean PlayerExist(final String playerName) {
        try {
            final ResultSet rs = EterniaServer.sqlcon.Query("SELECT * FROM eternia WHERE NAME='" + playerName + "';");
            return rs.next() && rs.getString("NAME") != null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void CreatePlayer(final String playerName) {
        if (!PlayerExist(playerName)) {
            EterniaServer.sqlcon.Update("INSERT INTO eternia (NAME, XP, BALANCE) VALUES ('" + playerName + "', '" + 0 + "','" + CVar.getDouble("money.start") + "');");
        }
    }
}