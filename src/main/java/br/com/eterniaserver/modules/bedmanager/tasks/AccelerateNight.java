package br.com.eterniaserver.modules.bedmanager.tasks;


import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Vars;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class AccelerateNight extends BukkitRunnable {

    private final EterniaServer plugin;
    private final World world;

    public AccelerateNight(final World world, EterniaServer plugin) {
        this.plugin = plugin;
        this.world = world;
        Messages.BroadcastMessage("bed.night-skipping", "%world_name%", world.getName());
    }

    @Override
    public void run() {
        final long time = world.getTime();
        final int sleeping = AccelerateWorld.getSleeping(world).size();
        final int players = plugin.getServer().getMaxPlayers();
        double base = EterniaServer.configs.getInt("bed.speed");
        double timeRate;
        if (sleeping > 0) {
            int x = players / sleeping;
            timeRate = base / x;
            if (time >= (1200 - timeRate * 1.5) && time <= 1200) {
                world.setStorm(false);
                world.setThundering(false);
                world.getPlayers().forEach(player -> player.setStatistic(Statistic.TIME_SINCE_REST, 0));
                Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> Vars.skipping_worlds.remove(world), 20);
                Messages.BroadcastMessage("bed.skip-night");
                this.cancel();
            } else {
                world.setTime(time + (int) timeRate);
            }
        } else if (Vars.skipping_worlds.contains(world)) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> Vars.skipping_worlds.remove(world), 20);
            this.cancel();
        }
    }
}