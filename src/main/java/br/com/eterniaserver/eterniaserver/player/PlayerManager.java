package br.com.eterniaserver.eterniaserver.player;

import br.com.eterniaserver.eternialib.sql.Queries;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Vars;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PlayerManager {

    private final EterniaServer plugin;
    private final Vars vars;

    public PlayerManager(EterniaServer plugin, Vars vars) {
        this.plugin = plugin;
        this.vars = vars;
    }

    public void playerHomeCreate(String playerName) {
        Queries.executeQuery("INSERT INTO " + plugin.serverConfig.getString("sql.table-home") + " (player_name, homes) VALUES('" + playerName + "', '" + "" + "');");
    }

    public void playerXPCreate(String playerName) {
        Queries.executeQuery("INSERT INTO " + plugin.serverConfig.getString("sql.table-xp") + " (player_name, xp) VALUES ('" + playerName + "', '" + 0 + "');");
    }

    public void playerMoneyCreate(String playerName) {
        Queries.executeQuery("INSERT INTO " + plugin.serverConfig.getString("sql.table-money") + " (player_name, balance) VALUES('" + playerName + "', '" + plugin.serverConfig.getDouble("money.start") + "');");
        vars.player_bal.add(playerName);
    }

    public void playerProfileCreate(String playerName) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        Queries.executeQuery("INSERT INTO " + plugin.serverConfig.getString("sql.table-player") + " (player_name, time) VALUES('" + playerName + "', '" + sdf.format(date) + "');");
        vars.player_login.put(playerName, sdf.format(date));
    }

    public boolean playerXPExist(String playerName) {
        if (vars.player_exp.contains(playerName)) return true;

        if (Queries.queryBoolean("SELECT * FROM " + plugin.serverConfig.getString("sql.table-xp") + " WHERE player_name='" + playerName + "';", "player_name")) {
            vars.player_exp.add(playerName);
            return true;
        } else {
            return false;
        }
    }

    public boolean playerProfileExist(String playerName) {
        if (vars.player_login.containsKey(playerName)) return true;

        final String profile = Queries.queryString("SELECT * FROM " + plugin.serverConfig.getString("sql.table-player")+ " WHERE player_name='" + playerName + "';", "time");
        if (!profile.equals("")) {
            vars.player_login.put(playerName, profile);
            return true;
        } else {
            return false;
        }
    }


    public boolean playerMoneyExist(String playerName) {
        if (vars.player_bal.contains(playerName)) return true;

        if (Queries.queryBoolean("SELECT * FROM " + plugin.serverConfig.getString("sql.table-money")+ " WHERE player_name='" + playerName + "';", "player_name")) {
            vars.player_bal.add(playerName);
            return true;
        } else {
            return false;
        }
    }

    public boolean playerHomeExist(String playerName) {
        if (vars.player_homes.contains(playerName)) return true;

        if (Queries.queryBoolean("SELECT * FROM " + plugin.serverConfig.getString("sql.table-home")+ " WHERE player_name='" + playerName + "';", "player_name")) {
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

        if (Queries.queryBoolean("SELECT * FROM " + plugin.serverConfig.getString("sql.table-kits") + " WHERE name='" + kit + "." + jogador + "';", "name")) {
            vars.player_cooldown.add(kit + "." + jogador);
            return true;
        } else {
            return false;
        }
    }

    public boolean registerMuted(String playerName) {
        return Queries.queryBoolean("SELECT * FROM " + plugin.serverConfig.getString("sql.table-muted") + " WHERE player_name='" + playerName + "';", "player_name");
    }

    public void checkMuted(String playerName) {
        final String time = Queries.queryString("SELECT * FROM " + plugin.serverConfig.getString("sql.table-muted") + " WHERE player_name='" + playerName + "';", "time");
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
