package br.com.eterniaserver.eterniaserver.modules.cash;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;


final class Handlers implements Listener {

    private final Services.Cash cashService;

    public Handlers(final Services.Cash cashService) {
        this.cashService = cashService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            if (cashService.guiCash(player, event.getCurrentItem()) != 0) {
                event.setCancelled(true);
            }
        }
    }

}
