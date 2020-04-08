package br.com.eterniaserver.events;

import br.com.eterniaserver.configs.CVar;
import br.com.eterniaserver.modules.spawnermanager.actions.SpawnerBreak;
import br.com.eterniaserver.modules.blockrewardmanager.BlockReward;

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