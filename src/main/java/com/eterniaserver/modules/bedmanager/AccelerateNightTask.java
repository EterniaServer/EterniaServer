package com.eterniaserver.modules.bedmanager;


import com.eterniaserver.EterniaServer;
import com.eterniaserver.configs.Vars;
import com.eterniaserver.configs.methods.BroadcastMessage;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class AccelerateNightTask extends BukkitRunnable {
    private final World world;

    public AccelerateNightTask(final World world) {
        this.world = world;
        new BroadcastMessage("bed.night-skipping", world.getName());
    }

    @Override
    public void run() {
        final long time = world.getTime();
        final int sleeping = BedTimer.getSleeping(world).size();
        double timeRate = 1200;
        if (sleeping != 0) {
            timeRate = Math.min(timeRate, Math.round(timeRate / world.getPlayers().size() * sleeping));
        }
        if (time >= (1200 - timeRate * 1.5) && time <= 1200) {
            world.setStorm(false);
            world.setThundering(false);
            world.getPlayers().forEach(player -> player.setStatistic(Statistic.TIME_SINCE_REST, 0));
            Bukkit.getScheduler().runTaskLaterAsynchronously(EterniaServer.getMain(), () -> Vars.skipping_worlds.remove(world), 20);
            new BroadcastMessage("bed.skip-night", "normalmente");
            this.cancel();
        }
    }
}