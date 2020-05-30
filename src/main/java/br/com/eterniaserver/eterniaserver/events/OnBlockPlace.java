package br.com.eterniaserver.eterniaserver.events;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class OnBlockPlace implements Listener {

    private final EterniaServer plugin;
    private final Messages messages;

    public OnBlockPlace(EterniaServer plugin, Messages messages) {
        this.plugin = plugin;
        this.messages = messages;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled()) return;

        if (event.getBlock().getType() == Material.SPAWNER) {
            if (plugin.serverConfig.getBoolean("modules.spawners")) {
                ItemStack placed = event.getItemInHand();
                ItemMeta meta = placed.getItemMeta();
                EntityType entity;
                try {
                    if (meta != null) {
                        String entityName = ChatColor.stripColor(meta.getDisplayName()).split(" Spawner")[0].replace("[", "").replace(" ", "_").toUpperCase();
                        entity = EntityType.valueOf(entityName);
                        CreatureSpawner spawner = (CreatureSpawner) event.getBlock().getState();
                        spawner.setSpawnedType(entity);
                        spawner.update();
                        messages.sendConsole("spawners.log", "%player_name%", event.getPlayer().getName(), "%mob_type%", entity.name().toLowerCase());
                    }
                } catch (NullPointerException|IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
