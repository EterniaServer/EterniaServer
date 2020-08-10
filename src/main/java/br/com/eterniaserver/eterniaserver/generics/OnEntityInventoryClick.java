package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import br.com.eterniaserver.eterniaserver.Strings;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class OnEntityInventoryClick implements Listener {

    @EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onEntityInventoryClick(InventoryClickEvent e) {
        if (e.isCancelled()) return;

        final Player player = (Player) e.getWhoClicked();
        final ItemStack itemStack = e.getCurrentItem();
        if (itemStack != null && (EterniaServer.serverConfig.getBoolean("spawners.prevent-anvil")
                && EterniaServer.serverConfig.getBoolean("modules.spawners")
                && e.getInventory().getType() == InventoryType.ANVIL
                && itemStack.getType() == Material.SPAWNER)) {
            e.setCancelled(true);
            player.sendMessage(Strings.MSG_SPAWNER_NAME);
            player.sendMessage(Strings.MSG_SPAWNER_LOG.replace(Constants.PLAYER, player.getDisplayName()));
        }

        if (EterniaServer.serverConfig.getBoolean("modules.cash") && e.getView().getTitle().equals("Cash")) {
            cashGui(player, e);
        }

    }

    private void cashGui(final Player player, InventoryClickEvent e) {
        final String playerName = player.getName();
        e.setCancelled(true);
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        if (!Vars.cashBuy.containsKey(uuid)) {
            int slot = e.getSlot();
            final String guiString = "gui." + slot;
            if (EterniaServer.cashConfig.contains(guiString)) {
                final int cost = EterniaServer.cashConfig.getInt(guiString + ".cost");
                if (APICash.hasCash(uuid, cost)) {
                    player.sendMessage(Strings.M_CASH_COST.replace(Constants.AMOUNT, String.valueOf(cost)));
                    player.sendMessage(Strings.M_CASH);
                    Vars.cashBuy.put(uuid, slot);
                } else {
                    player.sendMessage(Strings.M_CASH_NO);
                }
            }
        } else {
            player.sendMessage(Strings.M_CASH_ALREADY);
            player.sendMessage(Strings.M_CASH);
        }
        player.closeInventory();
    }

}
