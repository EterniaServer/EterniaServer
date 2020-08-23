package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.EterniaServer;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class AccelerateWorld implements Runnable {

    private final EterniaServer plugin;

    public AccelerateWorld(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        Bukkit.getWorlds().stream().filter(this::validateWorld).forEach(this::checkWorld);
    }

    private void checkWorld(final World world) {
        final int sleeping = getSleeping(world).size();
        if (sleeping > 0) {
            Vars.skippingWorlds.add(world);
            new AccelerateNight(world, plugin).runTaskTimer(plugin, 1, 1);
        }
    }

    private boolean validateWorld(final World world) {
        return !isBlacklisted(world)
                && !Vars.skippingWorlds.contains(world)
                && isNight(world);
    }

    private boolean isBlacklisted(final World world) {
        return EterniaServer.serverConfig.getList("bed.blacklisted-worlds").contains(world.getName());
    }

    private boolean isNight(final World world) {
        return world.getTime() > 12950 || world.getTime() < 23950;
    }

    public static List<Player> getSleeping(final World world) {
        return world.getPlayers().stream().filter(Player::isSleeping).collect(toList());
    }

}
