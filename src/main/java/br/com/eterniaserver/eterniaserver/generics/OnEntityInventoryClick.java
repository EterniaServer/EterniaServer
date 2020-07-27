package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
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

public class OnEntityInventoryClick implements Listener {

    private final EFiles messages;

    public OnEntityInventoryClick(EterniaServer plugin) {
        this.messages = plugin.getEFiles();
    }

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
            messages.sendMessage(Strings.M_SPAWNER_NAME, player);
            messages.sendConsole(Strings.M_SPAWNER_LOG, Constants.PLAYER, player.getDisplayName());
        }

        if (EterniaServer.serverConfig.getBoolean("modules.cash") && e.getView().getTitle().equals("Cash")) {
            cashGui(player, e);
        }

    }

    private void cashGui(final Player player, InventoryClickEvent e) {
        final String playerName = player.getName();
        e.setCancelled(true);
        if (!Vars.cashBuy.containsKey(playerName)) {
            int slot = e.getSlot();
            final String guiString = "gui." + slot;
            if (EterniaServer.cashConfig.contains(guiString)) {
                final int cost = EterniaServer.cashConfig.getInt(guiString + ".cost");
                if (APICash.hasCash(playerName, cost)) {
                    messages.sendMessage(Strings.M_CASH_COST, Constants.AMOUNT, cost, player);
                    messages.sendMessage(Strings.M_CASH, player);
                    Vars.cashBuy.put(playerName, slot);
                } else {
                    messages.sendMessage(Strings.M_CASH_NO, player);
                }
            }
        } else {
            messages.sendMessage(Strings.M_CASH_ALREADY, player);
            messages.sendMessage(Strings.M_CASH, player);
        }
        player.closeInventory();
    }

}
