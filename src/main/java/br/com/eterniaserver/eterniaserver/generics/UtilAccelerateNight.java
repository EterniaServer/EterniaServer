package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class UtilAccelerateNight extends BukkitRunnable {

    private final EterniaServer plugin;
    private final World world;

    public UtilAccelerateNight(final World world, EterniaServer plugin) {
        this.plugin = plugin;
        this.world = world;
        if (TimeUnit.MICROSECONDS.toSeconds(System.currentTimeMillis() - PluginVars.nightTime) > 300) {
            Bukkit.broadcastMessage(EterniaServer.configs.getMessage(Messages.NIGHT_SKIPPING, true, world.getName()));
        }
    }

    @Override
    public void run() {
        final long time = world.getTime();
        final int sleeping = APIServer.getSleeping(world).size();
        final int players = plugin.getServer().getMaxPlayers();
        if (sleeping > 0) {
            int x = players / sleeping;
            int timeRate = EterniaServer.configs.nightSpeed / x;
            if (time >= (1200 - timeRate * 1.5) && time <= 1200) {
                world.setStorm(false);
                world.setThundering(false);
                world.getPlayers().forEach(player -> player.setStatistic(Statistic.TIME_SINCE_REST, 0));
                Bukkit.getScheduler().runTaskLater(plugin, () -> PluginVars.skippingWorlds.remove(world), 20);
                Bukkit.broadcastMessage(EterniaServer.configs.getMessage(Messages.NIGHT_SKIPPED, true, world.getName()));
                APIServer.changeNightTime(System.currentTimeMillis());
                this.cancel();
            } else {
                world.setTime(time + timeRate);
            }
        } else if (PluginVars.skippingWorlds.contains(world)) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> PluginVars.skippingWorlds.remove(world), 20);
            this.cancel();
        }
    }

}