package br.com.eterniaserver.eterniaserver.events;

import br.com.eterniaserver.eternialib.UUIDFetcher;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.core.APICash;
import br.com.eterniaserver.eterniaserver.core.APIPlayer;
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
            final Player player = (Player) event.getEntity();
            if (APIPlayer.isGod(player.getName())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            final Player player = (Player) event.getDamager();
            if (player.isFlying() && !player.hasPermission("eternia.fly.bypass")) {
                APIPlayer.setIsOnPvP(UUIDFetcher.getUUIDOf(player.getName()));
                EterniaServer.configs.sendMessage(player, Messages.FLY_ENTER_PVP);
                player.setAllowFlight(false);
                player.setFlying(false);
            }
        }
    }

    @EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onEntityInventoryClick(InventoryClickEvent e) {
        if (!EterniaServer.configs.moduleCash && !EterniaServer.configs.moduleSpawners) return;

        final Player player = (Player) e.getWhoClicked();
        final ItemStack itemStack = e.getCurrentItem();
        if (itemStack != null && (EterniaServer.configs.preventAnvil
                && EterniaServer.configs.moduleSpawners
                && e.getInventory().getType() == InventoryType.ANVIL
                && itemStack.getType() == Material.SPAWNER)) {
            e.setCancelled(true);
            EterniaServer.configs.sendMessage(player, Messages.SPAWNER_CANT_CHANGE_NAME);
        }

        if (EterniaServer.configs.moduleCash) {
            switch (e.getView().getTitle()) {
                case "Cash":
                    APICash.menuGui(player, e.getSlot());
                    e.setCancelled(true);
                    break;
                case "Perm":
                    e.setCancelled(chooseGUi(player, "guis.perm.", e.getSlot()));
                    break;
                case "Pacotes":
                    e.setCancelled(chooseGUi(player, "guis.pacotes.", e.getSlot()));
                    break;
                case "Tags":
                    e.setCancelled(chooseGUi(player, "guis.tags.", e.getSlot()));
                    break;
                case "Spawners":
                    e.setCancelled(chooseGUi(player, "guis.spawners.", e.getSlot()));
                    break;
                default:
                    break;
            }
        }

    }

    private boolean chooseGUi(Player player, String guiName, int slot) {
        APICash.permGui(player, guiName + slot);
        return true;
    }

}
