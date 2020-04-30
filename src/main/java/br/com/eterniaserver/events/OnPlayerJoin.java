package br.com.eterniaserver.events;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.configs.methods.Checks;
import br.com.eterniaserver.player.PlayerManager;

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
        Player player = event.getPlayer();
        if (plugin.serverConfig.getBoolean("modules.experience")) {
            if (!playerManager.playerXPExist(player.getName())) {
                playerManager.playerXPCreate(player.getName());
            }
        }
        if (plugin.serverConfig.getBoolean("modules.playerchecks")) {
            vars.afktime.put(player.getName(), System.currentTimeMillis());
            if (!playerManager.playerProfileExist(player.getName())) {
                playerManager.playerProfileCreate(player.getName());
            }
        }
        if (plugin.serverConfig.getBoolean("modules.home")) {
            if (!playerManager.playerHomeExist(player.getName())) {
                playerManager.playerHomeCreate(player.getName());
            }
        }
        if (plugin.serverConfig.getBoolean("modules.chat")) {
            checks.addUUIF(event.getPlayer());
            vars.global.put(player.getName(), 0);
            if (player.hasPermission("eternia.spy")) {
                vars.spy.put(player, true);
            }
        }
        event.setJoinMessage(null);
        messages.BroadcastMessage("server.join", "%player_name%", player.getName());
    }

}