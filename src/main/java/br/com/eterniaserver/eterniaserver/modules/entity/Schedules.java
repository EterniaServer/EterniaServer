package br.com.eterniaserver.eterniaserver.modules.entity;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.modules.Constants;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

final class Schedules {

    private Schedules() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    static class PluginClearSchedule extends BukkitRunnable {

        private final EterniaServer plugin;

        private final int length;

        public PluginClearSchedule(EterniaServer plugin) {
            this.plugin = plugin;
            length = plugin.getInteger(Integers.CLEAR_RANGE);
        }

        @Override
        public void run() {
            if (!plugin.getBoolean(Booleans.CLEAR_ENTITIES)) {
                return;
            }

            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    updateCheckChunks(player.getChunk());
                }
            });
        }

        private void updateCheckChunks(Chunk origin) {
            World world = origin.getWorld();
            int cX = origin.getX();
            int cZ = origin.getZ();

            for (int x = -this.length; x <= this.length; x++) {
                for (int z = -this.length; z <= this.length; z++) {
                    world.getChunkAtAsync(cX + x, cZ + z).thenAccept(c -> cleanupEntities(c.getEntities()));
                }
            }
        }

        private void cleanupEntities(Entity[] entities) {
            int[] entityAmounts = new int[EntityType.values().length];

            for (Entity e : entities) {
                EntityType entityType = e.getType();
                Utils.EntityControl entityControl = plugin.getControl(entityType);

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
}
