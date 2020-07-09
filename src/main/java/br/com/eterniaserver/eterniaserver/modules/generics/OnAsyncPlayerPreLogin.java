package br.com.eterniaserver.eterniaserver.modules.generics;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.Date;

public class OnAsyncPlayerPreLogin implements Listener {

    private final EterniaServer plugin;

    public OnAsyncPlayerPreLogin(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        final String playerName = event.getName();
        if (plugin.serverConfig.getBoolean("modules.experience")) {
            if (!playerXPExist(playerName)) {
                playerXPCreate(playerName);
            }
        }
        if (plugin.serverConfig.getBoolean("modules.playerchecks")) {
            Vars.afktime.put(playerName, System.currentTimeMillis());
            if (!playerProfileExist(playerName)) {
                playerProfileCreate(playerName);
            }
        }
        if (plugin.serverConfig.getBoolean("modules.home")) {
            if (!playerHomeExist(playerName)) {
                playerHomeCreate(playerName);
            }
        }
    }

    private boolean playerProfileExist(String playerName) {
        if (EterniaServer.player_login.containsKey(playerName)) return true;

        final String profile = EQueries.queryString("SELECT * FROM " + plugin.serverConfig.getString("sql.table-player")+ " WHERE player_name='" + playerName + "';", "time");
        if (profile.equals("")) return false;

        EterniaServer.player_login.put(playerName, profile);
        return true;
    }

    private boolean playerXPExist(String playerName) {
        return Vars.xp.containsKey(playerName);
    }

    private boolean playerHomeExist(String playerName) {
        return Vars.home.containsKey(playerName);
    }

    private void playerProfileCreate(String playerName) {
        Date date = new Date();
        EQueries.executeQuery("INSERT INTO " + plugin.serverConfig.getString("sql.table-player") + " (player_name, time) VALUES('" + playerName + "', '" + plugin.sdf.format(date) + "');");
        EterniaServer.player_login.put(playerName, plugin.sdf.format(date));
    }

    private void playerXPCreate(String playerName) {
        EQueries.executeQuery("INSERT INTO " + plugin.serverConfig.getString("sql.table-xp") + " (player_name, xp) VALUES ('" + playerName + "', '" + 0 + "');");
        Vars.xp.put(playerName, 0);
    }

    private void playerHomeCreate(String playerName) {
        EQueries.executeQuery("INSERT INTO " + plugin.serverConfig.getString("sql.table-home") + " (player_name, homes) VALUES('" + playerName + "', '" + "" + "');");
    }

}
