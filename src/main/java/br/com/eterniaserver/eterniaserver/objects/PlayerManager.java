package br.com.eterniaserver.eterniaserver.objects;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PlayerManager {

    private final EterniaServer plugin;

    public PlayerManager(EterniaServer plugin) {
        this.plugin = plugin;
    }

    public void playerHomeCreate(String playerName) {
        EQueries.executeQuery("INSERT INTO " + plugin.serverConfig.getString("sql.table-home") + " (player_name, homes) VALUES('" + playerName + "', '" + "" + "');");
    }

    public void playerXPCreate(String playerName) {
        EQueries.executeQuery("INSERT INTO " + plugin.serverConfig.getString("sql.table-xp") + " (player_name, xp) VALUES ('" + playerName + "', '" + 0 + "');");
    }

    public void playerMoneyCreate(String playerName) {
        EQueries.executeQuery("INSERT INTO " + plugin.serverConfig.getString("sql.table-money") + " (player_name, balance) VALUES('" + playerName + "', '" + plugin.serverConfig.getDouble("money.start") + "');");
        EterniaServer.player_bal.add(playerName);
    }

    public void playerProfileCreate(String playerName) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        EQueries.executeQuery("INSERT INTO " + plugin.serverConfig.getString("sql.table-player") + " (player_name, time) VALUES('" + playerName + "', '" + sdf.format(date) + "');");
        EterniaServer.player_login.put(playerName, sdf.format(date));
    }

    public boolean playerXPExist(String playerName) {
        if (EterniaServer.player_exp.contains(playerName)) return true;

        if (EQueries.queryBoolean("SELECT * FROM " + plugin.serverConfig.getString("sql.table-xp") + " WHERE player_name='" + playerName + "';", "player_name")) {
            EterniaServer.player_exp.add(playerName);
            return true;
        } else {
            return false;
        }
    }

    public boolean playerProfileExist(String playerName) {
        if (EterniaServer.player_login.containsKey(playerName)) return true;

        final String profile = EQueries.queryString("SELECT * FROM " + plugin.serverConfig.getString("sql.table-player")+ " WHERE player_name='" + playerName + "';", "time");
        if (!profile.equals("")) {
            EterniaServer.player_login.put(playerName, profile);
            return true;
        } else {
            return false;
        }
    }


    public boolean playerMoneyExist(String playerName) {
        if (EterniaServer.player_bal.contains(playerName)) return true;

        if (EQueries.queryBoolean("SELECT * FROM " + plugin.serverConfig.getString("sql.table-money")+ " WHERE player_name='" + playerName + "';", "player_name")) {
            EterniaServer.player_bal.add(playerName);
            return true;
        } else {
            return false;
        }
    }

    public boolean playerHomeExist(String playerName) {
        if (EterniaServer.player_homes.contains(playerName)) return true;

        if (EQueries.queryBoolean("SELECT * FROM " + plugin.serverConfig.getString("sql.table-home")+ " WHERE player_name='" + playerName + "';", "player_name")) {
            EterniaServer.player_homes.add(playerName);
            return true;
        } else {
            return false;
        }
    }

    public boolean playerCooldownExist(String jogador, String kit) {
        if (EterniaServer.player_cooldown.contains(kit + "." + jogador)) {
            return true;
        }

        if (EQueries.queryBoolean("SELECT * FROM " + plugin.serverConfig.getString("sql.table-kits") + " WHERE name='" + kit + "." + jogador + "';", "name")) {
            EterniaServer.player_cooldown.add(kit + "." + jogador);
            return true;
        } else {
            return false;
        }
    }

    public boolean registerMuted(String playerName) {
        return EQueries.queryBoolean("SELECT * FROM " + plugin.serverConfig.getString("sql.table-muted") + " WHERE player_name='" + playerName + "';", "player_name");
    }

    public void checkMuted(String playerName) {
        final String time = EQueries.queryString("SELECT * FROM " + plugin.serverConfig.getString("sql.table-muted") + " WHERE player_name='" + playerName + "';", "time");
        if (!time.equals("")) {
            try {
                EterniaServer.player_muted.put(playerName, plugin.sdf.parse(time).getTime());
            } catch (ParseException e) {
                // todo
            }
        } else {
            EterniaServer.player_muted.put(playerName, System.currentTimeMillis());
        }
    }
}
