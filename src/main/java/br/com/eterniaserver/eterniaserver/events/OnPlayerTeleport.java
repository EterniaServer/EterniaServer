package br.com.eterniaserver.eterniaserver.events;

import br.com.eterniaserver.eterniaserver.EterniaServer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class OnPlayerTeleport implements Listener {

    private final EterniaServer plugin;

    public OnPlayerTeleport(EterniaServer plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        if (event.isCancelled()) return;
        final Player player = event.getPlayer();
        if (plugin.serverConfig.getBoolean("modules.teleports")) plugin.getVars().back.put(player.getName(), player.getLocation());
    }

}