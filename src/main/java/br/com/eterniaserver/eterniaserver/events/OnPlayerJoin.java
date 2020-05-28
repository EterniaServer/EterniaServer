package br.com.eterniaserver.eterniaserver.events;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Vars;
import br.com.eterniaserver.eterniaserver.configs.Checks;
import br.com.eterniaserver.eterniaserver.player.PlayerManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnPlayerJoin implements Listener {

    private final EterniaServer plugin;
    private final PlayerManager playerManager;
    private final Messages messages;
    private final Checks checks;
    private final Vars vars;

    public OnPlayerJoin(EterniaServer plugin, PlayerManager playerManager, Messages messages, Checks checks, Vars vars) {
        this.plugin = plugin;
        this.playerManager = playerManager;
        this.messages = messages;
        this.checks = checks;
        this.vars = vars;
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
            checks.addUUIF(player);
            vars.global.put(playerName, 0);
            if (player.hasPermission("eternia.spy")) vars.spy.put(player, true);
            if (!vars.player_muted.containsKey(playerName)) playerManager.checkMuted(playerName);

        }
        event.setJoinMessage(null);
        messages.BroadcastMessage("server.join", "%player_name%", playerName);
    }

}