package br.com.eterniaserver.eterniaserver.modules.bed;

import br.com.eterniaserver.eterniaserver.modules.Constants;

import lombok.Getter;

import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

final class Services {

    private Services() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    static class SleepingService {

        private final List<World> skippingWorlds = new ArrayList<>();

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

    }

}
