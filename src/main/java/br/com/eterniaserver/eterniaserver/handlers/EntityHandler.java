package br.com.eterniaserver.eterniaserver.handlers;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.core.User;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.core.APICash;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class EntityHandler implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            User user = new User((Player) event.getEntity());
            if (user.getGodMode()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player player = (Player) event.getDamager();
            User user = new User(player);
            if (player.isFlying() && !player.hasPermission(EterniaServer.getString(Strings.PERM_FLY_BYPASS))) {
                user.setIsOnPvP();
                user.sendMessage(Messages.FLY_ENTER_PVP);
                player.setAllowFlight(false);
                player.setFlying(false);
            }
        }
    }

    @EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onEntityInventoryClick(InventoryClickEvent e) {
        if (!EterniaServer.getBoolean(Booleans.MODULE_CASH) && !EterniaServer.getBoolean(Booleans.MODULE_SPAWNERS)) return;

        final Player player = (Player) e.getWhoClicked();
        final ItemStack itemStack = e.getCurrentItem();
        if (itemStack != null && (EterniaServer.getBoolean(Booleans.PREVENT_ANVIL)
                && EterniaServer.getBoolean(Booleans.MODULE_SPAWNERS)
                && e.getInventory().getType() == InventoryType.ANVIL
                && itemStack.getType() == Material.SPAWNER)) {
            e.setCancelled(true);
            EterniaServer.sendMessage(player, Messages.SPAWNER_CANT_CHANGE_NAME);
        }

        if (EterniaServer.getBoolean(Booleans.MODULE_CASH)) {
            String title = e.getView().getTitle();
            if ("Cash".equals(title)) {
                APICash.menuGui(player, e.getSlot());
                e.setCancelled(true);
            } else if (EterniaServer.getGuisInvert().containsKey(title)) {
                APICash.permGui(player, title, e.getSlot());
                e.setCancelled(true);
            }
        }

    }

}
