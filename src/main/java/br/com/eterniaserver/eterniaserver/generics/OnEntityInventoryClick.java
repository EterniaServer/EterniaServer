package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.Objects;

public class OnEntityInventoryClick implements Listener {

    private final EFiles messages;

    public OnEntityInventoryClick(EterniaServer plugin) {
        this.messages = plugin.getEFiles();
    }

    @EventHandler
    public void onEntityInventoryClick(InventoryClickEvent e) {
        if (e.isCancelled()) return;

        final Player player = (Player) e.getWhoClicked();
        if (EterniaServer.serverConfig.getBoolean("spawners.prevent-anvil") &&
                EterniaServer.serverConfig.getBoolean("modules.spawners") &&
                e.getInventory().getType() == InventoryType.ANVIL &&
                Objects.requireNonNull(e.getCurrentItem()).getType() == Material.SPAWNER) {
            e.setCancelled(true);
            messages.sendMessage("spawner.others.change-name", player);
            messages.sendConsole("spawner.log.change-name", Constants.PLAYER.get(), player.getDisplayName());
        }

        if (EterniaServer.serverConfig.getBoolean("modules.spawners") && e.getView().getTitle().equals("Cash")) {
            final String playerName = player.getName();
            e.setCancelled(true);
            if (!Vars.cashBuy.containsKey(playerName)) {
                int slot = e.getSlot();
                if (EterniaServer.cashConfig.contains("gui." + slot)) {
                    final int cost = EterniaServer.cashConfig.getInt("gui." + slot + ".cost");
                    if (APICash.hasCash(playerName, cost)) {
                        messages.sendMessage("cash.cost", Constants.AMOUNT.get(), cost, player);
                        messages.sendMessage("cash.use", player);
                        Vars.cashBuy.put(playerName, slot);
                    } else {
                        messages.sendMessage("cash.no-cash", player);
                    }
                }
            } else {
                messages.sendMessage("cash.already", player);
                messages.sendMessage("cash.use", player);
            }
            player.closeInventory();
        }

    }

}
