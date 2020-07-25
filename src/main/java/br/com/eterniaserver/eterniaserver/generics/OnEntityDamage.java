package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.EterniaServer;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class OnEntityDamage implements Listener {

    @EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.isCancelled()) return;
        final Entity entity = event.getEntity();
        if (entity instanceof Player) {
            final Player player = (Player) entity;
            if (Vars.god.contains(player.getName()) && EterniaServer.serverConfig.getBoolean("modules.generic")) {
                event.setCancelled(true);
            }
        }
    }

}
