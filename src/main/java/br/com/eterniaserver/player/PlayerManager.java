package br.com.eterniaserver.player;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Vars;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerManager {

    public static void playerHomeCreate(String playerName) {
        final String querie = "INSERT INTO " + EterniaServer.configs.getString("sql.table-home") + " (player_name, homes) VALUES('" + playerName + "', '" + "" + "');";
        EterniaServer.connection.executeSQLQuery(connection -> {
            PreparedStatement putHome = connection.prepareStatement(querie);
            putHome.execute();
            putHome.close();
        }, true);
    }

    public static void playerXPCreate(String playerName) {
        final String querie = "INSERT INTO " + EterniaServer.configs.getString("sql.table-xp") + " (player_name, xp) VALUES ('" + playerName + "', '" + 0 + "');";
        EterniaServer.connection.executeSQLQuery(connection -> {
            PreparedStatement putXP = connection.prepareStatement(querie);
            putXP.execute();
            putXP.close();
        }, true);
    }

    public static void playerMoneyCreate(String playerName) {
        final String querie = "INSERT INTO " + EterniaServer.configs.getString("sql.table-money") + " (player_name, balance) VALUES('" + playerName + "', '" + EterniaServer.configs.getDouble("money.start") + "');";
        EterniaServer.connection.executeSQLQuery(connection -> {
            PreparedStatement putMoney = connection.prepareStatement(querie);
            putMoney.execute();
            putMoney.close();
        }, true);
        Vars.player_bal.add(playerName);
    }

    public static void playerProfileCreate(String playerName) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        final String querie = "INSERT INTO " + EterniaServer.configs.getString("sql.table-player") + " (player_name, time) VALUES('" + playerName + "', '" + sdf.format(date) + "');";
        EterniaServer.connection.executeSQLQuery(connection -> {
            PreparedStatement putTime = connection.prepareStatement(querie);
            putTime.execute();
            putTime.close();
        }, true);
        Vars.player_login.put(playerName, sdf.format(date));
    }

    public static boolean playerXPExist(String playerName) {
        if (Vars.player_exp.contains(playerName)) {
            return true;
        }

        AtomicBoolean exist = new AtomicBoolean(false);
        final String querie = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-xp") + " WHERE player_name='" + playerName + "';";
        EterniaServer.connection.executeSQLQuery(connection -> {
            PreparedStatement getXP = connection.prepareStatement(querie);
            ResultSet resultSet = getXP.executeQuery();
            if (resultSet.next() && resultSet.getString("player_name") != null) {
                exist.set(true);
                Vars.player_exp.add(playerName);
            }
            resultSet.close();
            getXP.close();
        });

        return exist.get();
    }

    public static boolean playerProfileExist(String playerName) {
        if (Vars.player_login.containsKey(playerName)) {
            return true;
        }

        AtomicBoolean exist = new AtomicBoolean(false);
        final String querie = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-player")+ " WHERE player_name='" + playerName + "';";
        EterniaServer.connection.executeSQLQuery(connection -> {
            PreparedStatement playerProfile = connection.prepareStatement(querie);
            ResultSet resultSet = playerProfile.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString("player_name") != null) {
                    exist.set(true);
                }
                if (resultSet.getString("time") != null) {
                    Vars.player_login.put(playerName, resultSet.getString("time"));
                }
            }
            resultSet.close();
            playerProfile.close();
        });

        return exist.get();
    }


    public static boolean playerMoneyExist(String playerName) {
        if (Vars.player_bal.contains(playerName)) {
            return true;
        }

        AtomicBoolean exist = new AtomicBoolean(false);
        final String querie = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-money")+ " WHERE player_name='" + playerName + "';";
        EterniaServer.connection.executeSQLQuery(connection -> {
            PreparedStatement getMoney = connection.prepareStatement(querie);
            ResultSet resultSet = getMoney.executeQuery();
            if (resultSet.next() && resultSet.getString("player_name") != null) {
                exist.set(true);
                Vars.player_bal.add(playerName);
            }
            resultSet.close();
            getMoney.close();
        });

        return exist.get();
    }

    public static boolean playerHomeExist(String playerName) {
        if (Vars.player_homes.contains(playerName)) {
            return true;
        }

        AtomicBoolean exist = new AtomicBoolean(false);
        final String querie = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-home")+ " WHERE player_name='" + playerName + "';";
        EterniaServer.connection.executeSQLQuery(connection -> {
            PreparedStatement getHome = connection.prepareStatement(querie);
            ResultSet resultSet = getHome.executeQuery();
            if (resultSet.next() && resultSet.getString("player_name") != null) {
                exist.set(true);
                Vars.player_homes.add(playerName);
            }
            resultSet.close();
            getHome.close();
        });

        return exist.get();
    }

    public static boolean playerCooldownExist(String jogador, String kit) {
        if (Vars.player_cooldown.contains(kit + "." + jogador)) {
            return true;
        }

        AtomicBoolean exist = new AtomicBoolean(false);
        final String querie = "SELECT * FROM " + EterniaServer.configs.getString("sql.table-kits") + " WHERE name='" + kit + "." + jogador + "';";
        EterniaServer.connection.executeSQLQuery(connection -> {
            PreparedStatement getCooldown = connection.prepareStatement(querie);
            ResultSet resultSet = getCooldown.executeQuery();
            if (resultSet.next() && resultSet.getString("name") != null) {
                exist.set(true);
                Vars.player_cooldown.add(kit + "." + jogador);
            }
            resultSet.close();
            getCooldown.close();
        });

        return exist.get();

    }

}