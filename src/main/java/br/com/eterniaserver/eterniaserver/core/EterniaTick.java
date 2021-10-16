package br.com.eterniaserver.eterniaserver.core;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;
import br.com.eterniaserver.paperlib.PaperLib;
import br.com.eterniaserver.eterniaserver.objects.User;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


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

                getPlayersInTp(user);
                refreshPlayers(user);
            }
        }
    }

    private void refreshPlayers(User user) {
        EterniaServer.getUserAPI().setNameOnline(user.getName(), user.getUUID());
        EterniaServer.getUserAPI().setNameOnline(user.getDisplayName(), user.getUUID());
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