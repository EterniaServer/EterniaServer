package com.eterniaserver.events;

import com.eterniaserver.configs.CVar;
import com.eterniaserver.configs.methods.BroadcastMessage;
import com.eterniaserver.player.PlayerManager;
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
        event.setJoinMessage(null);
        new BroadcastMessage("server.join", player.getName());
    }
}