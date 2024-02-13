package br.com.eterniaserver.eterniaserver.modules.bed;

import br.com.eterniaserver.eterniaserver.modules.Constants;

import lombok.Getter;

import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


final class Services {

    private Services() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    static class SleepingService {

        private final List<World> skippingWorlds = new ArrayList<>();
        private final Map<UUID, Long> bedCooldown = new HashMap<>();

        @Getter
        private long nightMessageTime;

        public void updateNightMessageTime() {
            nightMessageTime = System.currentTimeMillis();
        }

        public List<Player> getSleeping(World world) {
            List<Player> listOfSleepingPlayers = new ArrayList<>();
            for (Player player : world.getPlayers()) {
                if (player.isSleeping()) {
                    listOfSleepingPlayers.add(player);
                }
            }

            return listOfSleepingPlayers;
        }

        public void putInSkipping(World world) {
            skippingWorlds.add(world);
        }

        public void removeFromSkipping(World world) {
            skippingWorlds.remove(world);
        }

        public boolean isSkipping(World world) {
            return skippingWorlds.contains(world);
        }

        public long getBedCooldown(UUID uuid) {
            return bedCooldown.getOrDefault(uuid, 0L);
        }

        public void updateBedCooldown(UUID uuid) {
            bedCooldown.put(uuid, System.currentTimeMillis());
        }

    }

}
