package com.eterniaserver.modules.bedmanager;

import com.eterniaserver.EterniaServer;
import com.eterniaserver.configs.CVar;
import com.eterniaserver.configs.Vars;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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
        final int needed = getNeeded(world);
        if (needed <= sleeping) {
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

    public static int getPlayers(final World world) {
        return Math.max(0, world.getPlayers().size() - getExcluded(world).size());
    }

    public static int getNeeded(final World world) {
        return Math.max(0, (int) Math.ceil((getPlayers(world)) * (CVar.getDouble("bed.percentage") / 100) - getSleeping(world).size()));
    }

    private static List<Player> getExcluded(final World world) {
        return world.getPlayers().stream().filter(BedTimer::isExcluded).collect(toList());
    }

    private static boolean isExcluded(final Player player) {
        final boolean excludedByCreative = player.getGameMode() == GameMode.CREATIVE;
        final boolean excludedBySpectator = player.getGameMode() == GameMode.SPECTATOR;
        final boolean excludedByPermission = player.hasPermission("eternia.bed");
        return excludedByCreative || excludedBySpectator || excludedByPermission || player.isSleepingIgnored();
    }
}
