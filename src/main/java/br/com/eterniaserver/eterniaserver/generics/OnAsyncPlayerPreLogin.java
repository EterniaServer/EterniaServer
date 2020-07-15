package br.com.eterniaserver.eterniaserver.generics;

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
        if (EterniaServer.serverConfig.getBoolean("modules.experience") && !playerXPExist(playerName)) {
            playerXPCreate(playerName);
        }
        if (EterniaServer.serverConfig.getBoolean("modules.playerchecks")) {
            Vars.afkTime.put(playerName, System.currentTimeMillis());
            if (!playerProfileExist(playerName)) {
                playerProfileCreate(playerName);
            }
        }
        if (EterniaServer.serverConfig.getBoolean("modules.home") && !playerHomeExist(playerName)) {
            playerHomeCreate(playerName);
        }
        if (EterniaServer.serverConfig.getBoolean("modules.economy") && !Vars.balances.containsKey(playerName)) {
            Vars.balances.put(playerName, 300.0);
            EQueries.executeQuery("INSERT INTO " + EterniaServer.serverConfig.getString("sql.table-money") + " (player_name, balance) VALUES('" + playerName + "', '" + EterniaServer.serverConfig.getDouble("money.start") + "');");
        }
    }

    private boolean playerProfileExist(String playerName) {
        if (Vars.playerLogin.containsKey(playerName)) return true;

        final String profile = EQueries.queryString("SELECT * FROM " + EterniaServer.serverConfig.getString("sql.table-player")+ " WHERE player_name='" + playerName + "';", "time");
        if (profile.equals("")) return false;

        Vars.playerLogin.put(playerName, profile);
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
        EQueries.executeQuery("INSERT INTO " + EterniaServer.serverConfig.getString("sql.table-player") + " (player_name, time) VALUES('" + playerName + "', '" + plugin.sdf.format(date) + "');", false);
        Vars.playerLogin.put(playerName, plugin.sdf.format(date));
    }

    private void playerXPCreate(String playerName) {
        EQueries.executeQuery("INSERT INTO " + EterniaServer.serverConfig.getString("sql.table-xp") + " (player_name, xp) VALUES ('" + playerName + "', '" + 0 + "');", false);
        Vars.xp.put(playerName, 0);
    }

    private void playerHomeCreate(String playerName) {
        EQueries.executeQuery("INSERT INTO " + EterniaServer.serverConfig.getString("sql.table-home") + " (player_name, homes) VALUES('" + playerName + "', '" + "" + "');", false);
    }

}
