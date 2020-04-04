package com.eterniaserver.events;

import com.eterniaserver.configs.CVar;
import com.eterniaserver.modules.spawnermanager.actions.SpawnerPlace;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class OnBlockPlace implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlock().getType() == Material.SPAWNER) {
            if (CVar.getBool("modules.spawners")) {
                new SpawnerPlace(event);
            }
        }
    }
}
