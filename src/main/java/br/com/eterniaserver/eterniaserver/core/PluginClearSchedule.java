package br.com.eterniaserver.eterniaserver.core;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.objects.EntityControl;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PluginClearSchedule extends BukkitRunnable {

    private final EterniaServer plugin;
    private final Chunk[] checkChunks;

    private final int length;

    public PluginClearSchedule(EterniaServer plugin) {
        this.plugin = plugin;
        length = EterniaServer.getInteger(Integers.CLEAR_RANGE);

        int lengthCube = (this.length * 2) + 1;

        this.checkChunks = new Chunk[(lengthCube * lengthCube)];
    }

    @Override
    public void run() {
        if (!EterniaServer.getBoolean(Booleans.CLEAR_ENTITIES)) {
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> getFromPlayers(player));
        }
    }

    private void getFromPlayers(Player player) {
        updateCheckChunks(player.getChunk());
        Set<Entity> entities = new HashSet<>();

        for (Chunk chunk : checkChunks) {
            Collections.addAll(entities, chunk.getEntities());
        }

        cleanupEntities(entities);
    }

    private void updateCheckChunks(Chunk origin) {
        World world = origin.getWorld();
        int indice = 0;
        int cX = origin.getX();
        int cZ = origin.getZ();

        for (int x = -this.length; x <= this.length; x++) {
            for (int z = -this.length; z <= this.length; z++) {
                checkChunks[indice++] = world.getChunkAt(cX + x, cZ + z);
            }
        }
    }

    private void cleanupEntities(Set<Entity> entities) {
        int[] entityAmounts = new int[EntityType.values().length];

        for (Entity e : entities) {
            EntityType entityType = e.getType();
            EntityControl entityControl = EterniaServer.getControl(entityType);

            if (entityControl.getClearAmount() == -1) {
                continue;
            }

            if (entityAmounts[entityType.ordinal()] > entityControl.getClearAmount()) {
                if (!e.isDead()) {
                    e.remove();
                }
            } else {
                ++entityAmounts[entityType.ordinal()];
            }
        }
    }

}
