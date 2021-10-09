package br.com.eterniaserver.eterniaserver.core;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;
import br.com.eterniaserver.paperlib.PaperLib;
import br.com.eterniaserver.eterniaserver.objects.User;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class EterniaTick extends BukkitRunnable {

    private final EterniaServer plugin;

    public EterniaTick(final EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player != null) {
                User user = new User(player);

                tpaTime(user);
                checkAFK(user);
                getPlayersInTp(user);
                refreshPlayers(user);
                optimizedMoveEvent(user);
            }
        }
    }

    private void optimizedMoveEvent(User user) {
        Location location = user.getLocation();
        Location firstLocation = EterniaServer.getUserAPI().getLocationOfUser(user.getUUID(), location);

        if (!(firstLocation.getBlockX() == location.getBlockX() && firstLocation.getBlockY() == location.getBlockY() && firstLocation.getBlockZ() == location.getBlockZ())) {
            user.updateAfkTime();
            if (user.isAfk()) {
                user.changeAfkState();
                Bukkit.broadcastMessage(plugin.getMessage(Messages.AFK_LEAVE, true, user.getName(), user.getDisplayName()));
            }
        }

        EterniaServer.getUserAPI().putLocationOfUser(user.getUUID(), location);
    }

    private void refreshPlayers(User user) {
        EterniaServer.getUserAPI().setNameOnline(user.getName(), user.getUUID());
        EterniaServer.getUserAPI().setNameOnline(user.getDisplayName(), user.getUUID());
    }

    private void tpaTime(User user) {
        if (EterniaServer.getUserAPI().hasTpaRequest(user.getUUID()) && TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - EterniaServer.getUserAPI().getAFKTime(user.getUUID())) >= 25) {
            EterniaServer.getUserAPI().removeTpaRequest(user.getUUID());
        }
    }

    private void checkAFK(User user) {
        if (!plugin.getBoolean(Booleans.MODULE_GENERIC)) {
            return;
        }

        if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - user.getAfkTime()) < plugin.getInteger(Integers.AFK_TIMER)) return;

        if (!plugin.getBoolean(Booleans.AFK_KICK)) {
            Bukkit.broadcastMessage(plugin.getMessage(Messages.AFK_AUTO_ENTER, false, user.getName(), user.getDisplayName()));
            user.changeAfkState();
            return;
        }

        if (!user.isAfk() && user.hasPermission(plugin.getString(Strings.PERM_AFK))) {
            Bukkit.broadcastMessage(plugin.getMessage(Messages.AFK_AUTO_ENTER, false, user.getName(), user.getDisplayName()));
            user.changeAfkState();
            return;
        }

        if (!user.isAfk() && !user.hasPermission(plugin.getString(Strings.PERM_NO_KICK_BY_AFK))) {
            Bukkit.broadcastMessage(plugin.getMessage(Messages.AFK_BROADCAST_KICK, true, user.getName(), user.getDisplayName()));
            user.clear();
            user.kick(plugin.getMessage(Messages.AFK_KICKED, true));
        }

    }

    private void getPlayersInTp(User user) {

        if (!user.isTeleporting()) {
            return;
        }

        final PlayerTeleport playerTeleport = EterniaServer.getUserAPI().getPlayerTeleport(user.getUUID());

        if (user.hasPermission(plugin.getString(Strings.PERM_TIMING_BYPASS))) {
            PaperLib.teleportAsync(user.getPlayer(), playerTeleport.getWantLocation());
            user.getPlayer().sendMessage(playerTeleport.getMessage());
            user.removeFromTeleporting();
            return;
        }

        if (playerTeleport.getCountdown() <= 0) {
            PaperLib.teleportAsync(user.getPlayer(), playerTeleport.getWantLocation());
            user.getPlayer().sendMessage(playerTeleport.getMessage());
            user.removeFromTeleporting();
            return;
        }

        if (playerTeleport.hasMoved()) {
            plugin.sendMessage(user.getPlayer(), Messages.TELEPORT_MOVED);
            user.removeFromTeleporting();
            return;
        }

        plugin.sendMessage(user.getPlayer(), Messages.TELEPORT_TIMING, String.valueOf(playerTeleport.getCountdown()));
        playerTeleport.decreaseCountdown();
    }

}