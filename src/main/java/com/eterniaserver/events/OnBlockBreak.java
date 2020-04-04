package com.eterniaserver.events;

import com.eterniaserver.configs.CVar;
import com.eterniaserver.modules.spawnermanager.actions.SpawnerBreak;
import com.eterniaserver.modules.blockrewardmanager.BlockReward;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class OnBlockBreak implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent breakEvent) {
        if (CVar.getBool("modules.spawners")) {
            new SpawnerBreak(breakEvent);
        }
        if (CVar.getBool("modules.block-reward")) {
            new BlockReward(breakEvent);
        }
    }
}