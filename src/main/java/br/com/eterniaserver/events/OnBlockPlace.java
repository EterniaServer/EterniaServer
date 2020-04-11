package br.com.eterniaserver.events;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.modules.spawnermanager.actions.SpawnerPlace;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class OnBlockPlace implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlock().getType() == Material.SPAWNER) {
            if (EterniaServer.configs.getBoolean("modules.spawners")) {
                new SpawnerPlace(event);
            }
        }
    }

}
