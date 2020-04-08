package br.com.eterniaserver.events;

import br.com.eterniaserver.configs.CVar;
import br.com.eterniaserver.configs.Vars;

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
        if (CVar.getBool("modules.playerchecks")) {
            Vars.afktime.put(event.getPlayer().getName(), System.currentTimeMillis());
        }
    }

}