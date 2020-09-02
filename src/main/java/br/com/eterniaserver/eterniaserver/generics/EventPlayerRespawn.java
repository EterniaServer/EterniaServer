package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class EventPlayerRespawn implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (PluginVars.locations.containsKey("warp.spawn") && EterniaServer.serverConfig.getBoolean("modules.teleports")) event.setRespawnLocation(PluginVars.locations.get("warp.spawn"));
    }

}
