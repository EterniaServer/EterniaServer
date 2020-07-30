package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.Strings;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnPlayerJoin implements Listener {

    private final double moneyStart = EterniaServer.serverConfig.getDouble("money.start");
    private final EterniaServer plugin;

    public OnPlayerJoin(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final String playerName = player.getName();

        if (EterniaServer.serverConfig.getBoolean("modules.chat")) {
            plugin.getInternMethods().addUUIF(player);
            Vars.global.put(playerName, 0);
            playerMutedExist(playerName);
            if (player.hasPermission("eternia.spy")) {
                Vars.spy.put(playerName, true);
            }
            if (Vars.nickname.containsKey(playerName)) {
                player.setDisplayName(ChatColor.translateAlternateColorCodes('&', Vars.nickname.get(playerName)));
            }
        }

        playerProfileExist(playerName);
        playerXPExist(playerName);
        playerHomeExist(playerName);
        playerMoneyExist(playerName);
        playerCashExist(playerName);
        playerKitsCreate(playerName);
        playerChecks(playerName);

        event.setJoinMessage(null);
        plugin.getEFiles().broadcastMessage(Strings.MSG_JOIN, Constants.PLAYER, player.getDisplayName());
    }

    private void playerMutedExist(String playerName) {
        if (!Vars.playerMuted.containsKey(playerName)) {
            playerMutedCreate(playerName);
        }
    }

    private void playerChecks(String playerName) {
        if (EterniaServer.serverConfig.getBoolean("modules.playerchecks")) {
            Vars.afkTime.put(playerName, System.currentTimeMillis());
        }
    }

    private void playerCashExist(String playerName) {
        if (EterniaServer.serverConfig.getBoolean("modules.cash") && !Vars.cash.containsKey(playerName)) {
            playerCashCreate(playerName);
        }
    }

    private void playerMoneyExist(String playerName) {
        if (EterniaServer.serverConfig.getBoolean("modules.economy") && !Vars.balances.containsKey(playerName)) {
            playerMoneyCreate(playerName);
        }
    }

    private void playerProfileExist(String playerName) {
        if (EterniaServer.serverConfig.getBoolean("modules.playerchecks") && !Vars.playerLogin.containsKey(playerName)) {
            playerProfileCreate(playerName);
        }
    }

    private void playerXPExist(String playerName) {
        if (EterniaServer.serverConfig.getBoolean("modules.experience") && !Vars.xp.containsKey(playerName)) {
            playerXPCreate(playerName);
        }
    }

    private void playerHomeExist(String playerName) {
        if (EterniaServer.serverConfig.getBoolean("modules.home") && !Vars.home.containsKey(playerName)) {
            playerHomeCreate(playerName);
        }
    }

    private void playerMutedCreate(String playerName) {
        final long time = System.currentTimeMillis();
        EQueries.executeQuery(Constants.getQueryInsert(Constants.TABLE_MUTED, Strings.PLAYER_NAME, playerName, Strings.TIME, time));
        Vars.playerMuted.put(playerName, time);
    }

    private void playerCashCreate(String playerName) {
        EQueries.executeQuery(Constants.getQueryInsert(Constants.TABLE_CASH, Strings.PLAYER_NAME, playerName, Strings.BALANCE, 0));
        Vars.cash.put(playerName, 0);
    }

    private void playerKitsCreate(String playerName) {
        if (EterniaServer.serverConfig.getBoolean("modules.kits")) {
            final long time = System.currentTimeMillis();
            for (String kit : EterniaServer.kitConfig.getConfigurationSection("kits").getKeys(true)) {
                final String kitName = kit + "." + playerName;
                if (!Vars.kitsCooldown.containsKey(kitName)) {
                    EQueries.executeQuery(Constants.getQueryInsert(Constants.TABLE_KITS, Strings.NAME, kitName, Strings.COOLDOWN, time));
                    Vars.kitsCooldown.put(kitName, time);
                }
            }
        }
    }

    private void playerMoneyCreate(String playerName) {
        EQueries.executeQuery(Constants.getQueryInsert(Constants.TABLE_MONEY, Strings.PLAYER_NAME, playerName, Strings.BALANCE, moneyStart));
        Vars.balances.put(playerName, 300.0);
    }

    private void playerProfileCreate(String playerName) {
        final long time = System.currentTimeMillis();
        EQueries.executeQuery(Constants.getQueryInsert(Constants.TABLE_PLAYER, Strings.PLAYER_NAME, playerName, Strings.TIME, time));
        Vars.playerLogin.put(playerName, time);
    }

    private void playerXPCreate(String playerName) {
        EQueries.executeQuery(Constants.getQueryInsert(Constants.TABLE_XP, Strings.PLAYER_NAME, playerName, Strings.XP, 0));
        Vars.xp.put(playerName, 0);
    }

    private void playerHomeCreate(String playerName) {
        EQueries.executeQuery(Constants.getQueryInsert(Constants.TABLE_HOME, Strings.PLAYER_NAME, playerName, Strings.HOMES, ""));
    }

}