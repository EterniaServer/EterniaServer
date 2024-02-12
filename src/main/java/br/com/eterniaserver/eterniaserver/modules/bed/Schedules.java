package br.com.eterniaserver.eterniaserver.modules.bed;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.enums.Lists;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

final class Schedules {

    private Schedules() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    static class PassNight extends BukkitRunnable {

        private final World world;
        private final EterniaServer plugin;
        private final Services.SleepingService sleepingService;

        public PassNight(final World world, EterniaServer plugin, Services.SleepingService sleepingService) {
            this.world = world;
            this.plugin = plugin;
            this.sleepingService = sleepingService;
            if (TimeUnit.MICROSECONDS.toSeconds(System.currentTimeMillis() - sleepingService.getNightMessageTime()) > 300) {
                plugin.getServer().broadcast(plugin.getMiniMessage(Messages.NIGHT_SKIPPING, true, world.getName()));
            }
        }

        @Override
        public void run() {
            long time = world.getTime();
            int sleeping = sleepingService.getSleeping(world).size();
            int players = plugin.getServer().getMaxPlayers();

            if (sleeping > 0) {
                int x = players / sleeping;
                int timeRate = plugin.getInteger(Integers.NIGHT_SPEED) / x;
                if (time >= (1200 - timeRate * 1.5) && time <= 1200) {
                    world.setStorm(false);
                    world.setThundering(false);
                    world.getPlayers().forEach(player -> player.setStatistic(Statistic.TIME_SINCE_REST, 0));
                    plugin.getServer().getScheduler().runTaskLater(plugin, () -> sleepingService.removeFromSkipping(world), 20);
                    plugin.getServer().broadcast(plugin.getMiniMessage(Messages.NIGHT_SKIPPED, true, world.getName()));
                    sleepingService.updateNightMessageTime();
                    this.cancel();
                } else {
                    world.setTime(time + timeRate);
                }
            } else if (sleepingService.isSkipping(world)) {
                plugin.getServer().getScheduler().runTaskLater(plugin, () -> sleepingService.removeFromSkipping(world), 20);
                this.cancel();
            }
        }

    }

    static class CheckWorld extends BukkitRunnable {

        private final EterniaServer plugin;
        private final Services.SleepingService sleepingService;

        public CheckWorld(EterniaServer plugin, Services.SleepingService sleepingService) {
            this.plugin = plugin;
            this.sleepingService = sleepingService;
        }

        @Override
        public void run() {
            plugin.getServer().getWorlds().stream().filter(this::validateWorld).forEach(this::checkWorld);
        }

        private void checkWorld(World world) {
            int sleeping = sleepingService.getSleeping(world).size();
            if (sleeping > 0) {
                sleepingService.putInSkipping(world);
                new PassNight(world, plugin, sleepingService).runTaskTimer(plugin, 1, 1);
            }
        }

        private boolean validateWorld(World world) {
            return !isBlacklisted(world) && !sleepingService.isSkipping(world) && isNight(world);
        }

        private boolean isBlacklisted(World world) {
            return plugin.getStringList(Lists.BLACKLISTED_WORLDS_SLEEP).contains(world.getName());
        }

        private boolean isNight(World world) {
            return world.getTime() > 12950 || world.getTime() < 23950;
        }

    }

}
