package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.Strings;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;

import br.com.eterniaserver.paperlib.PaperLib;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class Checks extends BukkitRunnable {

    private final EterniaServer plugin;
    private final EFiles messages;

    public Checks(EterniaServer plugin) {
        this.plugin = plugin;
        this.messages = plugin.getEFiles();
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Location location = player.getLocation();
            final String playerName = player.getName();

            tpaTime(playerName);
            checkNetherTrap(player, location, playerName);
            checkAFK(player, playerName);
            getPlayersInTp(player);

        }
    }

    private void tpaTime(final String playerName) {
        if (Vars.tpaRequests.containsKey(playerName) && Vars.tpaTime.containsKey(playerName) &&
                TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - Vars.tpaTime.get(playerName)) >= 25) {
            Vars.tpaRequests.remove(playerName);
            Vars.tpaTime.remove(playerName);
        }
    }

    private void checkNetherTrap(final Player player, final Location location, final String playerName) {
        if (location.getBlock().getType() == Material.NETHER_PORTAL) {
            if (!Vars.playersInPortal.containsKey(playerName)) {
                Vars.playersInPortal.put(playerName, 7);
            } else if (Vars.playersInPortal.get(playerName) <= 1) {
                if (location.getBlock().getType() == Material.NETHER_PORTAL) {
                    PaperLib.teleportAsync(player, getWarp());
                    messages.sendMessage(Strings.MSG_WARP_DONE, player);
                }
            } else if (Vars.playersInPortal.get(playerName) > 1) {
                Vars.playersInPortal.put(playerName, Vars.playersInPortal.get(playerName) - 1);
                if (Vars.playersInPortal.get(playerName) < 5) {
                    messages.sendMessage(Strings.MSG_NETHER_TRAP, Constants.COOLDOWN, Vars.playersInPortal.get(playerName), player);
                }
            }
        } else {
            Vars.playersInPortal.remove(playerName);
        }
    }

    private void checkAFK(final Player player, final String playerName) {
        if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - Vars.afkTime.get(playerName)) >= EterniaServer.serverConfig.getInt("server.afk-timer")) {
            if (EterniaServer.serverConfig.getBoolean("server.afk-kick")) {
                if (!Vars.afk.contains(playerName) && !player.hasPermission("eternia.nokickbyafksorrymates")) {
                    messages.broadcastMessage(Strings.MSG_AFK_BROAD, Constants.PLAYER, player.getDisplayName());
                    player.kickPlayer(messages.getMessage(Strings.MSG_AFK_KICKED));
                }
            } else {
                messages.broadcastMessage(Strings.MSG_AFK_ENABLE, Constants.PLAYER, player.getDisplayName());
                Vars.afk.add(playerName);
            }
        }
    }

    private void getPlayersInTp(final Player player) {
        if (Vars.teleports.containsKey(player)) {
            final PlayerTeleport playerTeleport = Vars.teleports.get(player);
            if (!player.hasPermission("eternia.timing.bypass")) {
                if (!playerTeleport.hasMoved()) {
                    if (playerTeleport.getCountdown() == 0) {
                        PaperLib.teleportAsync(player, playerTeleport.getWantLocation());
                        messages.sendMessage(playerTeleport.getMessage(), player);
                        Vars.teleports.remove(player);
                    } else {
                        messages.sendMessage(Strings.MSG_TELEPORT_TIMING, Constants.COOLDOWN, playerTeleport.getCountdown(), player);
                        playerTeleport.decreaseCountdown();
                    }
                } else {
                    messages.sendMessage(Strings.MSG_TELEPORT_MOVE, player);
                    Vars.teleports.remove(player);
                }
            } else {
                PaperLib.teleportAsync(player, playerTeleport.getWantLocation());
                messages.sendMessage(playerTeleport.getMessage(), player);
                Vars.teleports.remove(player);
            }
        }
    }

    private Location getWarp() {
        return Vars.warps.containsKey("spawn") ? Vars.warps.get("spawn") : plugin.error;
    }

}