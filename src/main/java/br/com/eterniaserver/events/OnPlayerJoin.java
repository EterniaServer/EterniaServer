package br.com.eterniaserver.events;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.player.PlayerManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnPlayerJoin implements Listener {

    @EventHandler
    public void OnJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (EterniaServer.configs.getBoolean("modules.experience")) {
            if (!PlayerManager.playerXPExist(player.getName())) {
                PlayerManager.playerXPCreate(player.getName());
            }
        }
        if (EterniaServer.configs.getBoolean("modules.playerchecks")) {
            Vars.afktime.put(player.getName(), System.currentTimeMillis());
            if (!PlayerManager.playerProfileExist(player.getName())) {
                PlayerManager.playerProfileCreate(player.getName());
            }
        }
        if (EterniaServer.configs.getBoolean("modules.home")) {
            if (!PlayerManager.playerHomeExist(player.getName())) {
                PlayerManager.playerHomeCreate(player.getName());
            }
        }
        if (EterniaServer.configs.getBoolean("modules.chat")) {
            Vars.global.put(player.getName(), 0);
        }
        event.setJoinMessage(null);
        Messages.BroadcastMessage("server.join", "%player_name%", player.getName());
    }

}