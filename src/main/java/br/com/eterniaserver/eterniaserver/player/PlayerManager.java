package br.com.eterniaserver.eterniaserver.player;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Vars;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class PlayerManager {

    private final EterniaServer plugin;
    private final Vars vars;

    public PlayerManager(EterniaServer plugin, Vars vars) {
        this.plugin = plugin;
        this.vars = vars;
    }

    public void playerHomeCreate(String playerName) {
        final String querie = "INSERT INTO " + plugin.serverConfig.getString("sql.table-home") + " (player_name, homes) VALUES('" + playerName + "', '" + "" + "');";
        plugin.connections.executeSQLQuery(connection -> {
            PreparedStatement putHome = connection.prepareStatement(querie);
            putHome.execute();
            putHome.close();
        }, true);
    }

    public void playerXPCreate(String playerName) {
        final String querie = "INSERT INTO " + plugin.serverConfig.getString("sql.table-xp") + " (player_name, xp) VALUES ('" + playerName + "', '" + 0 + "');";
        plugin.connections.executeSQLQuery(connection -> {
            PreparedStatement putXP = connection.prepareStatement(querie);
            putXP.execute();
            putXP.close();
        }, true);
    }

    public void playerMoneyCreate(String playerName) {
        final String querie = "INSERT INTO " + plugin.serverConfig.getString("sql.table-money") + " (player_name, balance) VALUES('" + playerName + "', '" + plugin.serverConfig.getDouble("money.start") + "');";
        plugin.connections.executeSQLQuery(connection -> {
            PreparedStatement putMoney = connection.prepareStatement(querie);
            putMoney.execute();
            putMoney.close();
        }, true);
        vars.player_bal.add(playerName);
    }

    public void playerProfileCreate(String playerName) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        final String querie = "INSERT INTO " + plugin.serverConfig.getString("sql.table-player") + " (player_name, time) VALUES('" + playerName + "', '" + sdf.format(date) + "');";
        plugin.connections.executeSQLQuery(connection -> {
            PreparedStatement putTime = connection.prepareStatement(querie);
            putTime.execute();
            putTime.close();
        }, true);
        vars.player_login.put(playerName, sdf.format(date));
    }

    public boolean playerXPExist(String playerName) {
        if (vars.player_exp.contains(playerName)) {
            return true;
        }

        AtomicBoolean exist = new AtomicBoolean(false);
        final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-xp") + " WHERE player_name='" + playerName + "';";
        plugin.connections.executeSQLQuery(connection -> {
            PreparedStatement getXP = connection.prepareStatement(querie);
            ResultSet resultSet = getXP.executeQuery();
            if (resultSet.next() && resultSet.getString("player_name") != null) {
                exist.set(true);
                vars.player_exp.add(playerName);
            }
            resultSet.close();
            getXP.close();
        });

        return exist.get();
    }

    public boolean playerProfileExist(String playerName) {
        if (vars.player_login.containsKey(playerName)) {
            return true;
        }

        AtomicBoolean exist = new AtomicBoolean(false);
        final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-player")+ " WHERE player_name='" + playerName + "';";
        plugin.connections.executeSQLQuery(connection -> {
            PreparedStatement playerProfile = connection.prepareStatement(querie);
            ResultSet resultSet = playerProfile.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString("player_name") != null) {
                    exist.set(true);
                }
                if (resultSet.getString("time") != null) {
                    vars.player_login.put(playerName, resultSet.getString("time"));
                }
            }
            resultSet.close();
            playerProfile.close();
        });

        return exist.get();
    }


    public boolean playerMoneyExist(String playerName) {
        if (vars.player_bal.contains(playerName)) {
            return true;
        }

        AtomicBoolean exist = new AtomicBoolean(false);
        final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-money")+ " WHERE player_name='" + playerName + "';";
        plugin.connections.executeSQLQuery(connection -> {
            PreparedStatement getMoney = connection.prepareStatement(querie);
            ResultSet resultSet = getMoney.executeQuery();
            if (resultSet.next() && resultSet.getString("player_name") != null) {
                exist.set(true);
                vars.player_bal.add(playerName);
            }
            resultSet.close();
            getMoney.close();
        });

        return exist.get();
    }

    public boolean playerHomeExist(String playerName) {
        if (vars.player_homes.contains(playerName)) {
            return true;
        }

        AtomicBoolean exist = new AtomicBoolean(false);
        final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-home")+ " WHERE player_name='" + playerName + "';";
        plugin.connections.executeSQLQuery(connection -> {
            PreparedStatement getHome = connection.prepareStatement(querie);
            ResultSet resultSet = getHome.executeQuery();
            if (resultSet.next() && resultSet.getString("player_name") != null) {
                exist.set(true);
                vars.player_homes.add(playerName);
            }
            resultSet.close();
            getHome.close();
        });

        return exist.get();
    }

    public boolean playerCooldownExist(String jogador, String kit) {
        if (vars.player_cooldown.contains(kit + "." + jogador)) {
            return true;
        }

        AtomicBoolean exist = new AtomicBoolean(false);
        final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-kits") + " WHERE name='" + kit + "." + jogador + "';";
        plugin.connections.executeSQLQuery(connection -> {
            PreparedStatement getCooldown = connection.prepareStatement(querie);
            ResultSet resultSet = getCooldown.executeQuery();
            if (resultSet.next() && resultSet.getString("name") != null) {
                exist.set(true);
                vars.player_cooldown.add(kit + "." + jogador);
            }
            resultSet.close();
            getCooldown.close();
        });

        return exist.get();
    }

    public boolean registerMuted(String playerName) {
        AtomicBoolean exist = new AtomicBoolean(false);
        final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-muted")+ " WHERE player_name='" + playerName + "';";
        plugin.connections.executeSQLQuery(connection -> {
            PreparedStatement getMuted = connection.prepareStatement(querie);
            ResultSet resultSet = getMuted.executeQuery();
            if (resultSet.next() && resultSet.getString("player_name") != null) {
                exist.set(true);
            }
            resultSet.close();
            getMuted.close();
        });

        return exist.get();
    }

    public void checkMuted(String playerName) {

        AtomicReference<Boolean> exist = new AtomicReference<>(false);
        final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-muted") + " WHERE player_name='" + playerName + "';";
        plugin.connections.executeSQLQuery(connection -> {
            PreparedStatement getMuted = connection.prepareStatement(querie);
            ResultSet resultSet = getMuted.executeQuery();
            if (resultSet.next() && resultSet.getString("time") != null) {
                try {
                    vars.player_muted.put(playerName, plugin.sdf.parse(resultSet.getString("time")).getTime());
                    exist.set(true);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        if (!exist.get()) vars.player_muted.put(playerName, System.currentTimeMillis());
    }
}
