package br.com.eterniaserver.eterniaserver.core;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.enums.Messages;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class PassNight extends BukkitRunnable {

    private final EterniaServer plugin;
    private final World world;

    public PassNight(final World world, EterniaServer plugin) {
        this.plugin = plugin;
        this.world = world;
        if (TimeUnit.MICROSECONDS.toSeconds(System.currentTimeMillis() - plugin.getNightMessageTime()) > 300) {
            Bukkit.broadcastMessage(plugin.getMessage(Messages.NIGHT_SKIPPING, true, world.getName()));
        }
    }

    @Override
    public void run() {
        final long time = world.getTime();
        final int sleeping = plugin.getSleeping(world).size();
        final int players = plugin.getServer().getMaxPlayers();
        if (sleeping > 0) {
            int x = players / sleeping;
            int timeRate = plugin.getInteger(Integers.NIGHT_SPEED) / x;
            if (time >= (1200 - timeRate * 1.5) && time <= 1200) {
                world.setStorm(false);
                world.setThundering(false);
                world.getPlayers().forEach(player -> player.setStatistic(Statistic.TIME_SINCE_REST, 0));
                Bukkit.getScheduler().runTaskLater(plugin, () -> plugin.removeFromSkipping(world), 20);
                Bukkit.broadcastMessage(plugin.getMessage(Messages.NIGHT_SKIPPED, true, world.getName()));
                plugin.updateNightMessageTime();
                this.cancel();
            } else {
                world.setTime(time + timeRate);
            }
        } else if (plugin.isSkipping(world)) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> plugin.removeFromSkipping(world), 20);
            this.cancel();
        }
    }

}