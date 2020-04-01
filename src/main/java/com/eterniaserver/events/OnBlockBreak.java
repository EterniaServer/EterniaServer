package com.eterniaserver.events;

import com.eterniaserver.spawner.SpawnerBreak;
import com.eterniaserver.storage.RewardBlockBreak;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class OnBlockBreak implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        new RewardBlockBreak(event.getBlock().getType(), event);
        if (event.getBlock().getType() == Material.SPAWNER) {
            new SpawnerBreak(event);
        }
    }
}