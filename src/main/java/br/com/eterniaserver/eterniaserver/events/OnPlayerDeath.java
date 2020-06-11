package br.com.eterniaserver.eterniaserver.events;

import br.com.eterniaserver.eterniaserver.EterniaServer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class OnPlayerDeath implements Listener {

    private final EterniaServer plugin;

    public OnPlayerDeath(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (plugin.serverConfig.getBoolean("modules.teleports")) {
            final Player player = event.getEntity();
            plugin.getVars().back.put(player.getName(), player.getLocation());
        }
    }

}
