package br.com.eterniaserver.eterniaserver.core;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.PlayerRelated;
import br.com.eterniaserver.eterniaserver.api.ServerRelated;
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

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            User user = new User(player);

            tpaTime(user);
            // checkNetherTrap(user); todo Optimize the method.
            checkAFK(user);
            getPlayersInTp(user);
            refreshPlayers(user);
            optimizedMoveEvent(user);
            commandsConfirmTime(user);

        }
    }

    private void commandsConfirmTime(User user) {
        if (!ServerRelated.hasCommandToRun(user.getUUID())) {
            return;
        }

        if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - ServerRelated.getCommandToRun(user.getUUID()).getTime()) > EterniaServer.getInteger(Integers.COMMAND_CONFIRM_TIME)) {
            ServerRelated.removeCommandToRun(user.getUUID());
        }
    }

    private void optimizedMoveEvent(User user) {
        Location location = user.getLocation();
        Location firstLocation = PlayerRelated.getLocationOfUser(user.getUUID(), location);

        if (!(firstLocation.getBlockX() == location.getBlockX() && firstLocation.getBlockY() == location.getBlockY() && firstLocation.getBlockZ() == location.getBlockZ())) {
            user.updateAfkTime();
            if (user.isAfk()) {
                user.changeAfkState();
                Bukkit.broadcastMessage(EterniaServer.getMessage(Messages.AFK_LEAVE, true, user.getName(), user.getDisplayName()));
            }
        }

        PlayerRelated.putLocationOfUser(user.getUUID(), location);
    }

    private void refreshPlayers(User user) {
        PlayerRelated.setNameOnline(user.getName(), user.getUUID());
        PlayerRelated.setNameOnline(user.getDisplayName(), user.getUUID());
    }

    private void tpaTime(User user) {
        if (PlayerRelated.hasTpaRequest(user.getUUID()) && TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - PlayerRelated.getAFKTime(user.getUUID())) >= 25) {
            PlayerRelated.removeTpaRequest(user.getUUID());
        }
    }

    private void checkNetherTrap(User user) {

        if (user.getLocation().getBlock().getType() != Material.NETHER_PORTAL) {
            PlayerRelated.putInPortal(user.getUUID(), -1);
            return;
        }

        int time = PlayerRelated.getInPortal(user.getUUID());

        if (time == -1) {
            PlayerRelated.putInPortal(user.getUUID(), 10);
            return;
        }

        if (time == 1) {
            PlayerRelated.putInPortal(user.getUUID(), -1);
            PaperLib.teleportAsync(user.getPlayer(), ServerRelated.getLocation("warp.spawn"));
            user.sendMessage(Messages.WARP_SPAWN_TELEPORTED);
            return;
        }

        if (--time < 5) {
            user.sendMessage(Messages.SERVER_NETHER_TRAP_TIMING, String.valueOf(time));
        }

        PlayerRelated.putInPortal(user.getUUID(), time);

    }

    private void checkAFK(User user) {
        if (!EterniaServer.getBoolean(Booleans.MODULE_GENERIC)) {
            return;
        }

        if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - user.getAfkTime()) < EterniaServer.getInteger(Integers.AFK_TIMER)) return;

        if (!EterniaServer.getBoolean(Booleans.AFK_KICK)) {
            Bukkit.broadcastMessage(EterniaServer.getMessage(Messages.AFK_AUTO_ENTER, false, user.getName(), user.getDisplayName()));
            user.changeAfkState();
            return;
        }

        if (!user.isAfk() && user.hasPermission(EterniaServer.getString(Strings.PERM_AFK))) {
            Bukkit.broadcastMessage(EterniaServer.getMessage(Messages.AFK_AUTO_ENTER, false, user.getName(), user.getDisplayName()));
            user.changeAfkState();
            return;
        }

        if (!user.isAfk() && !user.hasPermission(EterniaServer.getString(Strings.PERM_NO_KICK_BY_AFK))) {
            Bukkit.broadcastMessage(EterniaServer.getMessage(Messages.AFK_BROADCAST_KICK, true, user.getName(), user.getDisplayName()));
            user.clear();
            user.kick(EterniaServer.getMessage(Messages.AFK_KICKED, true));
        }

    }

    private void getPlayersInTp(User user) {

        if (!user.isTeleporting()) {
            return;
        }

        final PlayerTeleport playerTeleport = PlayerRelated.getPlayerTeleport(user.getUUID());

        if (user.hasPermission(EterniaServer.getString(Strings.PERM_TIMING_BYPASS))) {
            PaperLib.teleportAsync(user.getPlayer(), playerTeleport.getWantLocation());
            user.getPlayer().sendMessage(playerTeleport.getMessage());
            user.removeFromTeleporting();
            return;
        }

        if (playerTeleport.getCountdown() == 0) {
            PaperLib.teleportAsync(user.getPlayer(), playerTeleport.getWantLocation());
            user.getPlayer().sendMessage(playerTeleport.getMessage());
            user.removeFromTeleporting();
            return;
        }

        if (playerTeleport.hasMoved()) {
            user.sendMessage(Messages.TELEPORT_MOVED);
            user.removeFromTeleporting();
            return;
        }

        user.sendMessage(Messages.TELEPORT_TIMING, String.valueOf(playerTeleport.getCountdown()));
        playerTeleport.decreaseCountdown();

    }

}