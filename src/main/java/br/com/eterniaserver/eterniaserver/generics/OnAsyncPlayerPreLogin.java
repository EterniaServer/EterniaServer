package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class OnAsyncPlayerPreLogin implements Listener {

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        final String playerName = event.getName();
        if (EterniaServer.serverConfig.getBoolean("modules.experience") && !playerXPExist(playerName)) {
            playerXPCreate(playerName);
        }
        if (EterniaServer.serverConfig.getBoolean("modules.playerchecks") && !playerProfileExist(playerName)) {
            playerProfileCreate(playerName);
        }
        if (EterniaServer.serverConfig.getBoolean("modules.home") && !playerHomeExist(playerName)) {
            playerHomeCreate(playerName);
        }
        if (EterniaServer.serverConfig.getBoolean("modules.economy") && !playerMoneyExist(playerName)) {
            playerMoneyCreate(playerName);
        }
        if (EterniaServer.serverConfig.getBoolean("modules.cash") && !playerCashExist(playerName)) {
            playerCashCreate(playerName);
        }
        if (EterniaServer.serverConfig.getBoolean("modules.chat") && !playerMutedExist(playerName)) {
            playerMutedCreate(playerName);
        }
        if (EterniaServer.serverConfig.getBoolean("modules.kits")) {
            playerKitsCreate(playerName);
        }
        if (EterniaServer.serverConfig.getBoolean("modules.playerchecks")) {
            Vars.afkTime.put(playerName, System.currentTimeMillis());
        }
    }

    private boolean playerMutedExist(String playerName) {
        return Vars.playerMuted.containsKey(playerName);
    }

    private boolean playerCashExist(String playerName) {
        return Vars.cash.containsKey(playerName);
    }

    private boolean playerMoneyExist(String playerName) {
        return Vars.balances.containsKey(playerName);
    }

    private boolean playerProfileExist(String playerName) {
        return Vars.playerLogin.containsKey(playerName);
    }

    private boolean playerXPExist(String playerName) {
        return Vars.xp.containsKey(playerName);
    }

    private boolean playerHomeExist(String playerName) {
        return Vars.home.containsKey(playerName);
    }

    private void playerMutedCreate(String playerName) {
        final long time = System.currentTimeMillis();
        EQueries.executeQuery("INSERT INTO " + EterniaServer.serverConfig.getString("sql.table-muted") + " (player_name, time) VALUES('" + playerName + "', '" + time + "');", false);
        Vars.playerMuted.put(playerName, time);
    }

    private void playerCashCreate(String playerName) {
        EQueries.executeQuery("INSERT INTO " + EterniaServer.serverConfig.getString("sql.table-cash") + " (player_name, balance) VALUES('" + playerName + "', '0');", false);
        Vars.cash.put(playerName, 0);
    }

    private void playerKitsCreate(String playerName) {
        final long time = System.currentTimeMillis();
        for (String kit : EterniaServer.kitConfig.getConfigurationSection("kits").getKeys(true)) {
            if (!Vars.kitsCooldown.containsKey(kit + "." + playerName)) {
                EQueries.executeQuery("INSERT INTO " + EterniaServer.serverConfig.getString("sql.table-kits") + " (name, cooldown) VALUES('" + kit + "." + playerName + "', '" + time + "');", false);
                Vars.kitsCooldown.put(kit + "." + playerName, time);
            }
        }
    }

    private void playerMoneyCreate(String playerName) {
        EQueries.executeQuery("INSERT INTO " + EterniaServer.serverConfig.getString("sql.table-money") + " (player_name, balance) VALUES('" + playerName + "', '" + EterniaServer.serverConfig.getDouble("money.start") + "');", false);
        Vars.balances.put(playerName, 300.0);
    }

    private void playerProfileCreate(String playerName) {
        final long time = System.currentTimeMillis();
        EQueries.executeQuery("INSERT INTO " + EterniaServer.serverConfig.getString("sql.table-player") + " (player_name, time) VALUES('" + playerName + "', '" + time + "');", false);
        Vars.playerLogin.put(playerName, time);
    }

    private void playerXPCreate(String playerName) {
        EQueries.executeQuery("INSERT INTO " + EterniaServer.serverConfig.getString("sql.table-xp") + " (player_name, xp) VALUES ('" + playerName + "', '" + 0 + "');", false);
        Vars.xp.put(playerName, 0);
    }

    private void playerHomeCreate(String playerName) {
        EQueries.executeQuery("INSERT INTO " + EterniaServer.serverConfig.getString("sql.table-home") + " (player_name, homes) VALUES('" + playerName + "', '" + "" + "');", false);
    }

}
