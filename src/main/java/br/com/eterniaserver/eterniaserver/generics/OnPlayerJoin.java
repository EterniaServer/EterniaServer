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
        }
        if (player.hasPermission("eternia.spy")) {
            Vars.spy.put(playerName, true);
        }
        if (Vars.nickname.containsKey(playerName)) {
            player.setDisplayName(ChatColor.translateAlternateColorCodes('&', Vars.nickname.get(playerName)));
        }
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
        event.setJoinMessage(null);
        plugin.getEFiles().broadcastMessage(Strings.M_JOIN, Constants.PLAYER, player.getDisplayName());
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
        EQueries.executeQuery(Constants.getQueryInsert(Constants.TABLE_MUTED, Strings.PNAME, playerName, Strings.TIME, time));
        Vars.playerMuted.put(playerName, time);
    }

    private void playerCashCreate(String playerName) {
        EQueries.executeQuery(Constants.getQueryInsert(Constants.TABLE_CASH, Strings.PNAME, playerName, Strings.BALANCE, 0));
        Vars.cash.put(playerName, 0);
    }

    private void playerKitsCreate(String playerName) {
        final long time = System.currentTimeMillis();
        for (String kit : EterniaServer.kitConfig.getConfigurationSection("kits").getKeys(true)) {
            final String kitName = kit + "." + playerName;
            if (!Vars.kitsCooldown.containsKey(kitName)) {
                EQueries.executeQuery(Constants.getQueryInsert(Constants.TABLE_KITS, Strings.NAME, kitName, Strings.COOLDOWN, time));
                Vars.kitsCooldown.put(kitName, time);
            }
        }
    }

    private void playerMoneyCreate(String playerName) {
        EQueries.executeQuery(Constants.getQueryInsert(Constants.TABLE_MONEY, Strings.PNAME, playerName, Strings.BALANCE, moneyStart));
        Vars.balances.put(playerName, 300.0);
    }

    private void playerProfileCreate(String playerName) {
        final long time = System.currentTimeMillis();
        EQueries.executeQuery(Constants.getQueryInsert(Constants.TABLE_PLAYER, Strings.PNAME, playerName, Strings.TIME, time));
        Vars.playerLogin.put(playerName, time);
    }

    private void playerXPCreate(String playerName) {
        EQueries.executeQuery(Constants.getQueryInsert(Constants.TABLE_XP, Strings.PNAME, playerName, Strings.XP, 0));
        Vars.xp.put(playerName, 0);
    }

    private void playerHomeCreate(String playerName) {
        EQueries.executeQuery(Constants.getQueryInsert(Constants.TABLE_HOME, Strings.PNAME, playerName, Strings.HOMES, ""));
    }

}