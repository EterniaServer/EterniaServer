package br.com.eterniaserver.events;

import br.com.eterniaserver.configs.CVar;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.configs.methods.BroadcastMessage;
import br.com.eterniaserver.player.PlayerManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnPlayerJoin implements Listener {

    @EventHandler
    public void OnJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (CVar.getBool("modules.experience")) {
            if (!PlayerManager.PlayerExist(player.getName())) {
                PlayerManager.CreatePlayer(player.getName());
            }
        }
        if (CVar.getBool("modules.playerchecks")) {
            Vars.afktime.put(player.getName(), System.currentTimeMillis());
        }
        event.setJoinMessage(null);
        new BroadcastMessage("server.join", player.getName());
    }

}