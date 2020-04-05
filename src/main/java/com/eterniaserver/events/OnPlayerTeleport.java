package com.eterniaserver.events;

import com.eterniaserver.configs.CVar;
import com.eterniaserver.configs.Vars;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class OnPlayerTeleport implements Listener {
    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        if (CVar.getBool("modules.teleports")) {
            Vars.back.remove(event.getPlayer().getName());
            Vars.back.put(event.getPlayer().getName(), event.getPlayer().getLocation());
        }
        if (CVar.getBool("modules.afk")) {
            Vars.afktime.put(event.getPlayer(), System.currentTimeMillis());
        }
    }
}