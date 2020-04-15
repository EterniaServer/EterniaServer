package br.com.eterniaserver.player;

import br.com.eterniaserver.EterniaServer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlayerManager {

    public static boolean playerXPExist(final String playerName) {
        AtomicBoolean exist = new AtomicBoolean(false);
        final String querie = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-xp") + " WHERE player_name='" + playerName + "';";
        EterniaServer.connection.executeSQLQuery(connection -> {
            PreparedStatement getXP = connection.prepareStatement(querie);
            ResultSet resultSet = getXP.executeQuery();
            if (resultSet.next() && resultSet.getString("player_name") != null) exist.set(true);
            resultSet.close();
            getXP.close();
        });

        return exist.get();
    }

    public static void playerXPCreate(final String playerName) {
        final String querie = "INSERT INTO " + EterniaServer.configs.getString("sql.table-xp") + " (player_name, xp) VALUES ('" + playerName + "', '" + 0 + "');";
        EterniaServer.connection.executeSQLQuery(connection -> {
            PreparedStatement putXP = connection.prepareStatement(querie);
            putXP.execute();
            putXP.close();
        }, true);
    }

    public static void playerMoneyCreate(final String playerName) {
        final String querie = "INSERT INTO " + EterniaServer.configs.getString("sql.table-money") + " (player_name, balance) VALUES('" + playerName + "', '" + EterniaServer.configs.getDouble("money.start") + "');";
        EterniaServer.connection.executeSQLQuery(connection -> {
            PreparedStatement putMoney = connection.prepareStatement(querie);
            putMoney.execute();
            putMoney.close();
        }, true);
    }

    public static boolean playerMoneyExist(final String playerName) {
        AtomicBoolean exist = new AtomicBoolean(false);
        final String querie = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-money")+ " WHERE player_name='" + playerName + "';";
        EterniaServer.connection.executeSQLQuery(connection -> {
            PreparedStatement getMoney = connection.prepareStatement(querie);
            ResultSet resultSet = getMoney.executeQuery();
            if (resultSet.next() && resultSet.getString("player_name") != null) exist.set(true);
            resultSet.close();
            getMoney.close();
        });

        return exist.get();
    }

    public static void playerHomeCreate(final String playerName) {
        final String querie = "INSERT INTO " + EterniaServer.configs.getString("sql.table-home") + " (player_name, homes) VALUES('" + playerName + "', '" + "" + "');";
        EterniaServer.connection.executeSQLQuery(connection -> {
            PreparedStatement putHome = connection.prepareStatement(querie);
            putHome.execute();
            putHome.close();
        }, true);
    }

    public static boolean playerHomeExist(final String playerName) {
        AtomicBoolean exist = new AtomicBoolean(false);
        final String querie = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-home")+ " WHERE player_name='" + playerName + "';";
        EterniaServer.connection.executeSQLQuery(connection -> {
            PreparedStatement getHome = connection.prepareStatement(querie);
            ResultSet resultSet = getHome.executeQuery();
            if (resultSet.next() && resultSet.getString("player_name") != null) exist.set(true);
            resultSet.close();
            getHome.close();
        });

        return exist.get();
    }

}