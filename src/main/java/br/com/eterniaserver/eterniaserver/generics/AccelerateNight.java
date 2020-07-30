package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import br.com.eterniaserver.eterniaserver.Strings;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class AccelerateNight extends BukkitRunnable {

    private final EterniaServer plugin;
    private final World world;
    private final EFiles messages;

    public AccelerateNight(final World world, EterniaServer plugin) {
        this.plugin = plugin;
        this.world = world;
        this.messages = plugin.getEFiles();
        if (TimeUnit.MICROSECONDS.toSeconds(System.currentTimeMillis() - Vars.nightTime) > 300) {
            messages.broadcastMessage(Strings.MSG_SKIPPING, Constants.WORLD, world.getName());
        }
    }

    @Override
    public void run() {
        final long time = world.getTime();
        final int sleeping = AccelerateWorld.getSleeping(world).size();
        final int players = plugin.getServer().getMaxPlayers();
        double base = EterniaServer.serverConfig.getInt("bed.speed");
        if (sleeping > 0) {
            int x = players / sleeping;
            double timeRate = base / x;
            if (time >= (1200 - timeRate * 1.5) && time <= 1200) {
                world.setStorm(false);
                world.setThundering(false);
                world.getPlayers().forEach(player -> player.setStatistic(Statistic.TIME_SINCE_REST, 0));
                Bukkit.getScheduler().runTaskLater(plugin, () -> Vars.skippingWorlds.remove(world), 20);
                messages.broadcastMessage(Strings.MSG_SKIP_NIGHT);
                changeNightTime(System.currentTimeMillis());
                this.cancel();
            } else {
                world.setTime(time + (int) timeRate);
            }
        } else if (Vars.skippingWorlds.contains(world)) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> Vars.skippingWorlds.remove(world), 20);
            this.cancel();
        }
    }

    private static void changeNightTime(final long time) {
        Vars.nightTime = time;
    }

}