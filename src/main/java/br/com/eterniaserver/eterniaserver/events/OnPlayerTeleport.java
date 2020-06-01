package br.com.eterniaserver.eterniaserver.events;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Vars;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class OnPlayerTeleport implements Listener {

    private final EterniaServer plugin;
    private final Vars vars;

    public OnPlayerTeleport(EterniaServer plugin, Vars vars) {
        this.plugin = plugin;
        this.vars = vars;
    }


    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        if (event.isCancelled()) return;
        final Player player = event.getPlayer();
        if (plugin.serverConfig.getBoolean("modules.teleports")) vars.back.put(player.getName(), player.getLocation());
    }

}