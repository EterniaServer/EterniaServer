package com.eterniaserver.events;

import com.eterniaserver.configs.Vars;
import com.eterniaserver.modules.spawnermanager.actions.SpawnerBreak;
import com.eterniaserver.modules.blockrewardmanager.BlockReward;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class OnBlockBreak implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent breakEvent) {
        if (Vars.spawner) {
            new SpawnerBreak(breakEvent);
        }
        if (Vars.blockreward) {
            new BlockReward(breakEvent);
        }
    }
}