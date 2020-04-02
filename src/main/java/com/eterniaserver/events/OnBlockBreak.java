package com.eterniaserver.events;

import com.eterniaserver.spawner.SpawnerBreak;
import com.eterniaserver.storage.BlockReward;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class OnBlockBreak implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent breakEvent) {
        new SpawnerBreak(breakEvent);
        new BlockReward(breakEvent);
    }
}