package br.com.eterniaserver.eterniaserver.core;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Lists;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class CheckWorld extends BukkitRunnable {

    private final EterniaServer plugin;

    public CheckWorld(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        Bukkit.getWorlds().stream().filter(this::validateWorld).forEach(this::checkWorld);
    }

    private void checkWorld(final World world) {
        final int sleeping = plugin.getSleeping(world).size();
        if (sleeping > 0) {
            plugin.putInSkipping(world);
            new PassNight(world, plugin).runTaskTimer(plugin, 1, 1);
        }
    }

    private boolean validateWorld(final World world) {
        return !isBlacklisted(world) && !plugin.isSkipping(world) && isNight(world);
    }

    private boolean isBlacklisted(final World world) {
        return plugin.getStringList(Lists.BLACKLISTED_WORLDS_SLEEP).contains(world.getName());
    }

    private boolean isNight(final World world) {
        return world.getTime() > 12950 || world.getTime() < 23950;
    }

}
