package br.com.eterniaserver.modules.spawnermanager.actions;

import br.com.eterniaserver.configs.Messages;
import org.bukkit.ChatColor;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpawnerPlace {
    public static void Place(BlockPlaceEvent event) {
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
            Messages.ConsoleMessage("spawners.log", "%player_name%", event.getPlayer().getName(), "%mob_type%", entity.name().toLowerCase());
        } catch (NullPointerException|IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
