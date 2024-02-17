package br.com.eterniaserver.eterniaserver.modules.cash;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.modules.cash.Services.CashService;
import br.com.eterniaserver.eterniaserver.modules.cash.Entities.CashBalance;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;


final class Handlers implements Listener {

    private final EterniaServer plugin;
    private final CashService cashService;

    public Handlers(EterniaServer plugin, CashService cashService) {
        this.plugin = plugin;
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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            CashBalance cashBalance = EterniaLib.getDatabase().get(CashBalance.class, playerUUID);
            if (cashBalance.getUuid() == null) {
                EterniaServer.getCashAPI().setBalance(playerUUID, 0);
            }
        });
    }

}
