package br.com.eterniaserver.player;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.CVar;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerManager {
    public static boolean PlayerExist(final String playerName) {
        try {
            final ResultSet rs = EterniaServer.sqlcon.Query("SELECT * FROM xp WHERE player_name='" + playerName + "';");
            return rs.next() && rs.getString("player_name") != null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void CreatePlayer(final String playerName) {
        if (!PlayerExist(playerName)) {
            EterniaServer.sqlcon.Update("INSERT INTO xp (player_name, xp) VALUES ('" + playerName + "', '" + 0 + "');");
        }
    }

    public static void CreateEconomy(final String playerName) {
        EterniaServer.sqlcon.Update("INSERT INTO economy (player_name, balance) VALUES('" + playerName + "', '" + CVar.getDouble("money.start") + "');");
    }

    public static boolean PlayerExistE(final String playerName) {
        try {
            final ResultSet rs = EterniaServer.sqlcon.Query("SELECT * FROM economy WHERE player_name='" + playerName + "';");
            return rs.next() && rs.getString("player_name") != null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}