package br.com.eterniaserver.events;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.configs.Messages;

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
        if (e.isCancelled()) {
            return;
        }
        if (EterniaServer.configs.getBoolean("spawners.prevent-anvil") && EterniaServer.configs.getBoolean("modules.spawners")) {
            if (e.getInventory().getType() == InventoryType.ANVIL && Objects.requireNonNull(e.getCurrentItem()).getType() == Material.SPAWNER) {
                Player player = (Player) e.getWhoClicked();
                e.setCancelled(true);
                Messages.PlayerMessage("spawners.anvil", player);
                Messages.ConsoleMessage("spawners.anvil-try", "%player_name%", player.getName());
            }
        }
        if (EterniaServer.configs.getBoolean("modules.playerchecks")) {
            Vars.afktime.put(e.getWhoClicked().getName(), System.currentTimeMillis());
        }
    }

}
