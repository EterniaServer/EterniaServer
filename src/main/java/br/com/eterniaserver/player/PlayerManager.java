package br.com.eterniaserver.player;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.CVar;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerManager {

    public static boolean PlayerExist(final String playerName) {
        try {
            final String querie = "SELECT * FROM " + CVar.getString("sql.table-xp") + " WHERE player_name='" + playerName + "';";
            final ResultSet rs = EterniaServer.sqlcon.Query(querie);
            return rs.next() && rs.getString("player_name") != null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void CreatePlayer(final String playerName) {
        if (!PlayerExist(playerName)) {
            final String querie = "INSERT INTO " + CVar.getString("sql.table-xp") + " (player_name, xp) VALUES ('" + playerName + "', '" + 0 + "');";
            EterniaServer.sqlcon.Update(querie);
        }
    }

    public static void CreateEconomy(final String playerName) {
        final String querie = "INSERT INTO " + CVar.getString("sql.table-money") + " (player_name, balance) VALUES('" + playerName + "', '" + CVar.getDouble("money.start") + "');";
        EterniaServer.sqlcon.Update(querie);
    }

    public static boolean PlayerExistE(final String playerName) {
        try {
            final String querie = "SELECT * FROM " + CVar.getString("sql.table-money")+ " WHERE player_name='" + playerName + "';";
            final ResultSet rs = EterniaServer.sqlcon.Query(querie);
            return rs.next() && rs.getString("player_name") != null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}