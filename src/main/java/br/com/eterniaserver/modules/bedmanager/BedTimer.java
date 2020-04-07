package br.com.eterniaserver.modules.bedmanager;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Vars;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class BedTimer implements Runnable {
    @Override
    public void run() {
        Bukkit.getWorlds().stream().filter(this::validateWorld).forEach(this::checkWorld);
    }

    private void checkWorld(final World world) {
        final int sleeping = getSleeping(world).size();
        if (sleeping > 0) {
            Vars.skipping_worlds.add(world);
            new AccelerateNightTask(world).runTaskTimer(EterniaServer.getMain(), 1, 1);
        }
    }

    private boolean validateWorld(final World world) {
        return !isBlacklisted(world)
                && !Vars.skipping_worlds.contains(world)
                && isNight(world);
    }

    private boolean isBlacklisted(final World world) {
        return Objects.requireNonNull(EterniaServer.getMain().getConfig().getList("bed.blacklisted-worlds")).contains(world.getName());
    }

    private boolean isNight(final World world) {
        return world.getTime() > 12950 || world.getTime() < 23950;
    }

    public static List<Player> getSleeping(final World world) {
        return world.getPlayers().stream().filter(Player::isSleeping).collect(toList());
    }

}
