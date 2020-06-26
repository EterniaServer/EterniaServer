package br.com.eterniaserver.eterniaserver.events;

import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.objects.PlayerManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnPlayerJoin implements Listener {

    private final EterniaServer plugin;
    private final PlayerManager playerManager;

    public OnPlayerJoin(EterniaServer plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getPlayerManager();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final String playerName = player.getName();
        if (plugin.serverConfig.getBoolean("modules.chat")) {
            plugin.getChecks().addUUIF(player);
            EterniaServer.global.put(playerName, 0);
            if (player.hasPermission("eternia.spy")) EterniaServer.spy.put(player, true);
            if (!EterniaServer.player_muted.containsKey(playerName)) playerManager.checkMuted(playerName);
        }
        if (plugin.serverConfig.getBoolean("modules.economy")) {
            if (!plugin.getBalances().containsKey(playerName)) {
                plugin.getBalances().put(playerName, 300.0);
                EQueries.executeQuery("INSERT INTO " + plugin.serverConfig.getString("sql.table-money") + " (player_name, balance) VALUES('" + playerName + "', '" + plugin.serverConfig.getDouble("money.start") + "');");
            }
        }
        event.setJoinMessage(null);
        plugin.getEFiles().broadcastMessage("server.join", "%player_name%", playerName);
    }

}