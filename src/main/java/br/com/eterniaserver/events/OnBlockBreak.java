package br.com.eterniaserver.events;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.modules.spawnermanager.actions.SpawnerBreak;
import br.com.eterniaserver.modules.blockrewardmanager.BlockReward;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class OnBlockBreak implements Listener {

    private final EterniaServer plugin;

    public OnBlockBreak(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent breakEvent) {
        if (EterniaServer.configs.getBoolean("modules.spawners")) {
            SpawnerBreak.Break(breakEvent);
        }
        if (EterniaServer.configs.getBoolean("modules.block-reward")) {
            BlockReward.Break(breakEvent, plugin);
        }
    }

}