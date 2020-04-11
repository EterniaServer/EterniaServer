package br.com.eterniaserver.player;

import br.com.eterniaserver.EterniaServer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerManager {

    public static boolean PlayerExist(final String playerName) {
        try {
            final String querie = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-xp") + " WHERE player_name='" + playerName + "';";
            final ResultSet rs = EterniaServer.connection.Query(querie);
            return rs.next() && rs.getString("player_name") != null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void CreatePlayer(final String playerName) {
        if (!PlayerExist(playerName)) {
            final String querie = "INSERT INTO " + EterniaServer.configs.getString("sql.table-xp") + " (player_name, xp) VALUES ('" + playerName + "', '" + 0 + "');";
            EterniaServer.connection.Update(querie);
        }
    }

    public static void CreateEconomy(final String playerName) {
        final String querie = "INSERT INTO " + EterniaServer.configs.getString("sql.table-money") + " (player_name, balance) VALUES('" + playerName + "', '" + EterniaServer.configs.getDouble("money.start") + "');";
        EterniaServer.connection.Update(querie);
    }

    public static boolean PlayerExistE(final String playerName) {
        try {
            final String querie = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-money")+ " WHERE player_name='" + playerName + "';";
            final ResultSet rs = EterniaServer.connection.Query(querie);
            return rs.next() && rs.getString("player_name") != null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void CreateHome(final String playerName) {
        final String querie = "INSERT INTO " + EterniaServer.configs.getString("sql.table-home") + " (name, homes) VALUES('" + playerName + "', '" + "" + "');";
        EterniaServer.connection.Update(querie);
    }

    public static boolean PlayerExistH(final String playerName) {
        try {
            final String querie = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-home")+ " WHERE name='" + playerName + "';";
            final ResultSet rs = EterniaServer.connection.Query(querie);
            return rs.next() && rs.getString("name") != null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}