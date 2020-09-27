package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PluginClear extends BukkitRunnable {

    private int removed;

    @Override
    public void run() {
        removed = 0;
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (Chunk chunk : around(player.getChunk())) {
                cleanupChunk(chunk);
            }
        }
        if (removed != 0) {
            Bukkit.broadcastMessage(PluginMSGs.REMOVED_ENTITIES.replace(PluginConstants.AMOUNT, String.valueOf(removed)));
        }
    }

    private Collection<Chunk> around(Chunk origin) {
        World world = origin.getWorld();

        int length = (EterniaServer.configs.clearRange * 2) + 1;
        Set<Chunk> chunks = new HashSet<>(length * length);

        int cX = origin.getX();
        int cZ = origin.getZ();

        for (int x = -EterniaServer.configs.clearRange; x <= EterniaServer.configs.clearRange; x++) {
            for (int z = -EterniaServer.configs.clearRange; z <= EterniaServer.configs.clearRange; z++) {
                chunks.add(world.getChunkAt(cX + x, cZ + z));
            }
        }
        return chunks;
    }

    private void cleanupChunk(Chunk chunk) {
        Map<EntityType, Integer> entityIntegerMap = new HashMap<>();
        for (Entity e : chunk.getEntities()) {
            if (e instanceof Item) return;
            int amount = entityIntegerMap.getOrDefault(e.getType(), 0);

            if (amount > 15) {
                e.remove();
                removed++;
            } else {
                entityIntegerMap.put(e.getType(), amount + 1);
            }
        }
    }

}
