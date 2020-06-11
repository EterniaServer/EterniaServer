package br.com.eterniaserver.eterniaserver.events;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Vars;
import br.com.eterniaserver.eterniaserver.player.PlayerManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnPlayerJoin implements Listener {

    private final EterniaServer plugin;
    private final PlayerManager playerManager;
    private final Vars vars;

    public OnPlayerJoin(EterniaServer plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getPlayerManager();
        this.vars = plugin.getVars();
    }

    @EventHandler
    public void OnJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final String playerName = player.getName();
        if (plugin.serverConfig.getBoolean("modules.experience")) {
            if (!playerManager.playerXPExist(playerName)) playerManager.playerXPCreate(playerName);
        }
        if (plugin.serverConfig.getBoolean("modules.playerchecks")) {
            vars.afktime.put(playerName, System.currentTimeMillis());
            if (!playerManager.playerProfileExist(playerName)) playerManager.playerProfileCreate(playerName);
        }
        if (plugin.serverConfig.getBoolean("modules.home")) {
            if (!playerManager.playerHomeExist(playerName)) playerManager.playerHomeCreate(playerName);
        }
        if (plugin.serverConfig.getBoolean("modules.chat")) {
            plugin.getChecks().addUUIF(player);
            vars.global.put(playerName, 0);
            if (player.hasPermission("eternia.spy")) vars.spy.put(player, true);
            if (!vars.player_muted.containsKey(playerName)) playerManager.checkMuted(playerName);

        }
        event.setJoinMessage(null);
        plugin.getMessages().broadcastMessage("server.join", "%player_name%", playerName);
    }

}