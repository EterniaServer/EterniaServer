package br.com.eterniaserver.eterniaserver.events;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.Objects;

public class OnEntityInventoryClick implements Listener {

    private final EterniaServer plugin;
    private final EFiles messages;

    public OnEntityInventoryClick(EterniaServer plugin) {
        this.plugin = plugin;
        this.messages = plugin.getEFiles();
    }

    @EventHandler
    public void onEntityInventoryClick(InventoryClickEvent e) {
        if (e.isCancelled()) return;

        final Player player = (Player) e.getWhoClicked();
        final String playerName = player.getName();
        if (plugin.serverConfig.getBoolean("spawners.prevent-anvil") && plugin.serverConfig.getBoolean("modules.spawners")) {
            if (e.getInventory().getType() == InventoryType.ANVIL && Objects.requireNonNull(e.getCurrentItem()).getType() == Material.SPAWNER) {
                e.setCancelled(true);
                messages.sendMessage("spawner.others.change-name", player);
                messages.sendConsole("spawner.log.change-name", "%player_name%", playerName);
            }
        }
    }

}
