package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.UUIDFetcher;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EventEntityDamageByEntity implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            final Player player = (Player) event.getDamager();
            if (player.isFlying() && !player.hasPermission("eternia.fly.bypass")) {
                player.sendMessage(PluginMSGs.FLY_PVP_DISABLED);
                APIFly.setIsOnPvP(UUIDFetcher.getUUIDOf(player.getName()));
                player.setAllowFlight(false);
                player.setFlying(false);
            }
        }
    }

}
