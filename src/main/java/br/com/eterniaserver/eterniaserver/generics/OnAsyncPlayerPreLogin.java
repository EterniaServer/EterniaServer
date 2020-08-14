package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.configs.Configs;
import br.com.eterniaserver.eterniaserver.configs.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.ArrayList;
import java.util.UUID;

public class OnAsyncPlayerPreLogin implements Listener {

    private final double moneyStart = EterniaServer.serverConfig.getDouble("money.start");

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        final String playerName = event.getName();
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        playerXPExist(uuid);
        playerHomeExist(uuid);
        playerMoneyExist(uuid);
        playerCashExist(uuid);
    }

    private void playerCashExist(UUID uuid) {
        if (EterniaServer.serverConfig.getBoolean("modules.cash") && !Vars.cash.containsKey(uuid)) {
            playerCashCreate(uuid);
        }
    }

    private void playerMoneyExist(UUID uuid) {
        if (EterniaServer.serverConfig.getBoolean("modules.economy") && !Vars.balances.containsKey(uuid)) {
            playerMoneyCreate(uuid);
        }
    }

    private void playerXPExist(UUID uuid) {
        if (EterniaServer.serverConfig.getBoolean("modules.experience") && !Vars.xp.containsKey(uuid)) {
            playerXPCreate(uuid);
        }
    }

    private void playerHomeExist(UUID uuid) {
        if (EterniaServer.serverConfig.getBoolean("modules.home") && !Vars.home.containsKey(uuid)) {
            playerHomeCreate(uuid);
        }
    }

    private void playerCashCreate(UUID uuid) {
        EQueries.executeQuery(Constants.getQueryInsert(Configs.TABLE_CASH, Constants.UUID_STR, uuid.toString(), Constants.BALANCE_STR, 0), false);
        Vars.cash.put(uuid, 0);
    }

    private void playerMoneyCreate(UUID uuid) {
        EQueries.executeQuery(Constants.getQueryInsert(Configs.TABLE_MONEY, Constants.UUID_STR, uuid.toString(), Constants.BALANCE_STR, moneyStart), false);
        Vars.balances.put(uuid, 300.0);
    }

    private void playerXPCreate(UUID uuid) {
        EQueries.executeQuery(Constants.getQueryInsert(Configs.TABLE_XP, Constants.UUID_STR, uuid.toString(), Constants.XP_STR, 0), false);
        Vars.xp.put(uuid, 0);
    }

    private void playerHomeCreate(UUID uuid) {
        EQueries.executeQuery(Constants.getQueryInsert(Configs.TABLE_HOME, Constants.UUID_STR, uuid.toString(), Constants.HOMES_STR, ""), false);
        Vars.home.put(uuid, new ArrayList<>());
    }


}
