package br.com.eterniaserver.eterniaserver.core;

import br.com.eterniaserver.eterniaserver.EterniaServer;

import br.com.eterniaserver.eterniaserver.enums.ConfigLists;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class CheckWorld implements Runnable {

    private final EterniaServer plugin;

    public CheckWorld(EterniaServer plugin) {
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
            new PassNight(world, plugin).runTaskTimer(plugin, 1, 1);
        }
    }

    private boolean validateWorld(final World world) {
        return !isBlacklisted(world)
                && !Vars.skippingWorlds.contains(world)
                && isNight(world);
    }

    private boolean isBlacklisted(final World world) {
        return EterniaServer.getStringList(ConfigLists.BLACKLISTED_WORLDS_SLEEP).contains(world.getName());
    }

    private boolean isNight(final World world) {
        return world.getTime() > 12950 || world.getTime() < 23950;
    }

}
