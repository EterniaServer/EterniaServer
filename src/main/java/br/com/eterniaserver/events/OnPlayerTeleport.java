package br.com.eterniaserver.events;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Vars;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class OnPlayerTeleport implements Listener {

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (EterniaServer.configs.getBoolean("modules.teleports")) {
            Vars.back.put(event.getPlayer().getName(), event.getPlayer().getLocation());
        }
        if (EterniaServer.configs.getBoolean("modules.playerchecks")) {
            Vars.afktime.put(event.getPlayer().getName(), System.currentTimeMillis());
        }
    }

}