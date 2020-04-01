package com.eterniaserver.events;

import com.eterniaserver.configs.MVar;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import java.util.Objects;

public class OnInventoryClick implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory().getType() == InventoryType.ANVIL && Objects.requireNonNull(e.getCurrentItem()).getType() == Material.SPAWNER) {
            Player player = (Player) e.getWhoClicked();
            e.setCancelled(true);
            MVar.playerMessage("spawners.anvil", player);
            MVar.consoleReplaceMessage("spawners.anvil-try", player.getName());
        }
    }
}
