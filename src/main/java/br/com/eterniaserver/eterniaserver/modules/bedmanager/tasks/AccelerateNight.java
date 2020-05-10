package br.com.eterniaserver.eterniaserver.modules.bedmanager.tasks;


import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Vars;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class AccelerateNight extends BukkitRunnable {

    private final EterniaServer plugin;
    private final World world;
    private final Messages messages;
    private final Vars vars;

    public AccelerateNight(final World world, EterniaServer plugin, Messages messages, Vars vars) {
        this.plugin = plugin;
        this.world = world;
        this.messages = messages;
        this.vars = vars;
        messages.BroadcastMessage("bed.night-skipping", "%world_name%", world.getName());
    }

    @Override
    public void run() {
        final long time = world.getTime();
        final int sleeping = AccelerateWorld.getSleeping(world).size();
        final int players = plugin.getServer().getMaxPlayers();
        double base = plugin.serverConfig.getInt("bed.speed");
        double timeRate;
        if (sleeping > 0) {
            int x = players / sleeping;
            timeRate = base / x;
            if (time >= (1200 - timeRate * 1.5) && time <= 1200) {
                world.setStorm(false);
                world.setThundering(false);
                world.getPlayers().forEach(player -> player.setStatistic(Statistic.TIME_SINCE_REST, 0));
                Bukkit.getScheduler().runTaskLater(plugin, () -> vars.skipping_worlds.remove(world), 20);
                messages.BroadcastMessage("bed.skip-night");
                this.cancel();
            } else {
                world.setTime(time + (int) timeRate);
            }
        } else if (vars.skipping_worlds.contains(world)) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> vars.skipping_worlds.remove(world), 20);
            this.cancel();
        }
    }
}