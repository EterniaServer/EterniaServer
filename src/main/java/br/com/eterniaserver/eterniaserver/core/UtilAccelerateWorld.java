package br.com.eterniaserver.eterniaserver.core;

import br.com.eterniaserver.eterniaserver.EterniaServer;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class UtilAccelerateWorld implements Runnable {

    private final EterniaServer plugin;

    public UtilAccelerateWorld(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        Bukkit.getWorlds().stream().filter(this::validateWorld).forEach(this::checkWorld);
    }

    private void checkWorld(final World world) {
        final int sleeping = APIServer.getSleeping(world).size();
        if (sleeping > 0) {
            Vars.skippingWorlds.add(world);
            new UtilAccelerateNight(world, plugin).runTaskTimer(plugin, 1, 1);
        }
    }

    private boolean validateWorld(final World world) {
        return !isBlacklisted(world)
                && !Vars.skippingWorlds.contains(world)
                && isNight(world);
    }

    private boolean isBlacklisted(final World world) {
        return EterniaServer.configs.blacklistedWorldsBed.contains(world.getName());
    }

    private boolean isNight(final World world) {
        return world.getTime() > 12950 || world.getTime() < 23950;
    }

}
