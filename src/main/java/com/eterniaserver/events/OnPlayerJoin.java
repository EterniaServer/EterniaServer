package com.eterniaserver.events;

import com.eterniaserver.configs.MVar;
import com.eterniaserver.player.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnPlayerJoin implements Listener {
    @EventHandler
    public void OnJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!PlayerManager.PlayerExist(player.getName())) {
            PlayerManager.CreatePlayer(player.getName());
        }
        event.setJoinMessage(null);
        MVar.broadcastReplaceMessage("server.join", player.getName());
    }
}