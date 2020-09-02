package br.com.eterniaserver.eterniaserver.generics;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EventEntityDamage implements Listener {

    @EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            final Player player = (Player) event.getEntity();
            if (PluginVars.god.contains(player.getName())) {
                event.setCancelled(true);
            }
        }
    }

}
