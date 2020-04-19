package br.com.eterniaserver.events;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.player.PlayerManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        }
        if (EterniaServer.configs.getBoolean("modules.home")) {
            if (!PlayerManager.playerHomeExist(player.getName())) {
                PlayerManager.playerHomeCreate(player.getName());
            }
        }
        Vars.teleporting.put(player.getName(), System.currentTimeMillis());
        event.setJoinMessage(null);
        Messages.BroadcastMessage("server.join", player.getName());
    }

}