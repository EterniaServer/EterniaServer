package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class AccelerateNight extends BukkitRunnable {

    private final EterniaServer plugin;
    private final World world;
    private final EFiles messages;

    public AccelerateNight(final World world, EterniaServer plugin) {
        this.plugin = plugin;
        this.world = world;
        this.messages = plugin.getEFiles();
        messages.broadcastMessage("bed.night-skipping", "%world_name%", world.getName());
    }

    @Override
    public void run() {
        final long time = world.getTime();
        final int sleeping = AccelerateWorld.getSleeping(world).size();
        final int players = plugin.getServer().getMaxPlayers();
        double base = EterniaServer.serverConfig.getInt("bed.speed");
        double timeRate;
        if (sleeping > (players / 100) * 15) {
            int x = players / sleeping;
            timeRate = base / x;
            if (time >= (1200 - timeRate * 1.5) && time <= 1200) {
                world.setStorm(false);
                world.setThundering(false);
                world.getPlayers().forEach(player -> player.setStatistic(Statistic.TIME_SINCE_REST, 0));
                Bukkit.getScheduler().runTaskLater(plugin, () -> Vars.skippingWorlds.remove(world), 20);
                messages.broadcastMessage("bed.skip-night");
                this.cancel();
            } else {
                world.setTime(time + (int) timeRate);
            }
        } else if (Vars.skippingWorlds.contains(world)) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> Vars.skippingWorlds.remove(world), 20);
            this.cancel();
        }
    }
}