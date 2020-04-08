package br.com.eterniaserver.events;

import br.com.eterniaserver.configs.CVar;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.configs.methods.ConsoleMessage;
import br.com.eterniaserver.configs.methods.PlayerMessage;

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
        if (CVar.getBool("spawners.prevent-anvil") && CVar.getBool("modules.spawners")) {
            if (e.getInventory().getType() == InventoryType.ANVIL && Objects.requireNonNull(e.getCurrentItem()).getType() == Material.SPAWNER) {
                Player player = (Player) e.getWhoClicked();
                e.setCancelled(true);
                new PlayerMessage("spawners.anvil", player);
                new ConsoleMessage("spawners.anvil-try", player.getName());
            }
        }
        if (CVar.getBool("modules.playerchecks")) {
            Vars.afktime.put(e.getWhoClicked().getName(), System.currentTimeMillis());
        }
    }

}
