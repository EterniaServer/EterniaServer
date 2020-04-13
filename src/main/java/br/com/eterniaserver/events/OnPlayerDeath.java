package br.com.eterniaserver.events;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Vars;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class OnPlayerDeath implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (EterniaServer.configs.getBoolean("modules.teleports")) {
            Vars.back.remove(event.getEntity().getName());
            Vars.back.put(event.getEntity().getName(), event.getEntity().getLocation());
        }
    }

}
