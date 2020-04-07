package br.com.eterniaserver.modules.spawnermanager.actions;

import br.com.eterniaserver.configs.methods.ConsoleMessage;
import org.bukkit.ChatColor;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpawnerPlace {
    public SpawnerPlace(BlockPlaceEvent event) {
        ItemStack placed = event.getItemInHand();
        ItemMeta meta = placed.getItemMeta();
        EntityType entity;
        try {
            assert meta != null;
            String entityName = ChatColor.stripColor(meta.getDisplayName()).split(" Spawner")[0].replace("[", "").replace(" ", "_").toUpperCase();
            entity = EntityType.valueOf(entityName);
            CreatureSpawner spawner = (CreatureSpawner) event.getBlock().getState();
            spawner.setSpawnedType(entity);
            spawner.update();
            new ConsoleMessage("spawners.log", event.getPlayer().getName(), entity.name().toLowerCase());
        } catch (NullPointerException|IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
