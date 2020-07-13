package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.EterniaServer;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class OnPlayerBlockPlace implements Listener {

    private final EterniaServer plugin;

    public OnPlayerBlockPlace(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerBlockPlace(BlockPlaceEvent event) {
        if (event.getBlock().getType() == Material.SPAWNER && plugin.serverConfig.getBoolean("modules.spawners")) {
            final Block blockPlaced = event.getBlockPlaced();
            final ItemMeta meta = event.getItemInHand().getItemMeta();
            if (meta != null) {
                final String entityName = ChatColor.stripColor(meta.getDisplayName()).split(" Spawner")[0].replace("[", "").replace(" ", "_").toUpperCase();
                final EntityType entity = EntityType.valueOf(entityName);
                CreatureSpawner spawner = (CreatureSpawner) blockPlaced.getState();
                spawner.setSpawnedType(entity);
                spawner.update();
            }
        }
    }

}
