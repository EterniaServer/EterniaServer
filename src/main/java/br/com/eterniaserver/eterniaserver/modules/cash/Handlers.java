package br.com.eterniaserver.eterniaserver.modules.cash;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;


final class Handlers implements Listener {

    private final Services.Cash cashService;

    public Handlers(final Services.Cash cashService) {
        this.cashService = cashService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityInventoryClick(InventoryClickEvent event) {
        HumanEntity whoClicked = event.getWhoClicked();
        ItemStack currentItem = event.getCurrentItem();

        if (whoClicked instanceof Player player && cashService.guiCash(player, currentItem) != 0) {
            event.setCancelled(true);
        }
    }

}
