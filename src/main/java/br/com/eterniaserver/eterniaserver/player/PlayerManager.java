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
        plugin.executeQuery("INSERT INTO " + plugin.serverConfig.getString("sql.table-home") + " (player_name, homes) VALUES('" + playerName + "', '" + "" + "');");
    }

    public void playerXPCreate(String playerName) {
        plugin.executeQuery("INSERT INTO " + plugin.serverConfig.getString("sql.table-xp") + " (player_name, xp) VALUES ('" + playerName + "', '" + 0 + "');");
    }

    public void playerMoneyCreate(String playerName) {
        plugin.executeQuery("INSERT INTO " + plugin.serverConfig.getString("sql.table-money") + " (player_name, balance) VALUES('" + playerName + "', '" + plugin.serverConfig.getDouble("money.start") + "');");
        vars.player_bal.add(playerName);
    }

    public void playerProfileCreate(String playerName) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        plugin.executeQuery("INSERT INTO " + plugin.serverConfig.getString("sql.table-player") + " (player_name, time) VALUES('" + playerName + "', '" + sdf.format(date) + "');");
        vars.player_login.put(playerName, sdf.format(date));
    }

    public boolean playerXPExist(String playerName) {
        if (vars.player_exp.contains(playerName)) return true;

        if (plugin.executeQueryBoolean("SELECT * FROM " + plugin.serverConfig.getString("sql.table-xp") + " WHERE player_name='" + playerName + "';", "player_name").get()) {
            vars.player_exp.add(playerName);
            return true;
        } else {
            return false;
        }
    }

    public boolean playerProfileExist(String playerName) {
        if (vars.player_login.containsKey(playerName)) return true;

        final String profile = plugin.executeQueryString("SELECT * FROM " + plugin.serverConfig.getString("sql.table-player")+ " WHERE player_name='" + playerName + "';", "time").toString();
        if (!profile.equals("")) {
            vars.player_login.put(playerName, profile);
            return true;
        } else {
            return false;
        }
    }


    public boolean playerMoneyExist(String playerName) {
        if (vars.player_bal.contains(playerName)) return true;

        if (plugin.executeQueryBoolean("SELECT * FROM " + plugin.serverConfig.getString("sql.table-money")+ " WHERE player_name='" + playerName + "';", "player_name").get()) {
            vars.player_bal.add(playerName);
            return true;
        } else {
            return false;
        }
    }

    public boolean playerHomeExist(String playerName) {
        if (vars.player_homes.contains(playerName)) return true;

        if (plugin.executeQueryBoolean("SELECT * FROM " + plugin.serverConfig.getString("sql.table-home")+ " WHERE player_name='" + playerName + "';", "player_name").get()) {
            vars.player_homes.add(playerName);
            return true;
        } else {
            return false;
        }
    }

    public boolean playerCooldownExist(String jogador, String kit) {
        if (vars.player_cooldown.contains(kit + "." + jogador)) {
            return true;
        }

        if (plugin.executeQueryBoolean("SELECT * FROM " + plugin.serverConfig.getString("sql.table-kits") + " WHERE name='" + kit + "." + jogador + "';", "name").get()) {
            vars.player_cooldown.add(kit + "." + jogador);
            return true;
        } else {
            return false;
        }
    }

    public boolean registerMuted(String playerName) {
        return plugin.executeQueryBoolean("SELECT * FROM " + plugin.serverConfig.getString("sql.table-muted") + " WHERE player_name='" + playerName + "';", "player_name").get();
    }

    public void checkMuted(String playerName) {
        final String time = plugin.executeQueryString("SELECT * FROM " + plugin.serverConfig.getString("sql.table-muted") + " WHERE player_name='" + playerName + "';", "time").toString();
        if (!time.equals("")) {
            try {
                vars.player_muted.put(playerName, plugin.sdf.parse(time).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            vars.player_muted.put(playerName, System.currentTimeMillis());
        }
    }
}
