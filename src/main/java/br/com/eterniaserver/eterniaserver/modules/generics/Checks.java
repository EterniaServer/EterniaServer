package br.com.eterniaserver.eterniaserver.modules.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;

import io.papermc.lib.PaperLib;

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

            if (Vars.tpa_requests.containsKey(playerName)) {
                if (Vars.tpa_time.containsKey(playerName) &&
                        TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - Vars.tpa_time.get(playerName)) >= 25) {
                    Vars.tpa_requests.remove(playerName);
                    Vars.tpa_time.remove(playerName);
                }
            }
            if (location.getBlock().getType() == Material.NETHER_PORTAL) {
                if (!EterniaServer.playersInPortal.containsKey(playerName)) {
                    EterniaServer.playersInPortal.put(playerName, 7);
                } else if (EterniaServer.playersInPortal.get(playerName) <= 1) {
                    if (location.getBlock().getType() == Material.NETHER_PORTAL) {
                        PaperLib.teleportAsync(player, getWarp("spawn"));
                        messages.sendMessage("teleport.warp.done", player);
                    }
                } else {
                    EterniaServer.playersInPortal.put(playerName, EterniaServer.playersInPortal.get(playerName) - 1);
                    if (EterniaServer.playersInPortal.get(playerName) < 5) {
                        messages.sendMessage("server.nether-trap", "%cooldown%", EterniaServer.playersInPortal.get(playerName), player);
                    }
                }
            } else {
                EterniaServer.playersInPortal.remove(playerName);
            }
            if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - Vars.afktime.get(playerName)) >= plugin.serverConfig.getInt("server.afk-timer")) {
                if (plugin.serverConfig.getBoolean("server.afk-kick")) {
                    if (!EterniaServer.afk.contains(playerName) && !player.hasPermission("eternia.nokickbyafksorrymates")) {
                        messages.broadcastMessage("generic.afk.broadcast-kicked", "%player_name%", playerName);
                        player.kickPlayer(messages.getMessage("generic.afk.kicked"));
                    }
                } else {
                    messages.broadcastMessage("generic.afk.enabled", "%player_name%", playerName);
                    EterniaServer.afk.add(playerName);
                }
            }
            if (EterniaServer.teleports.containsKey(player)) {
                final PlayerTeleport playerTeleport = EterniaServer.teleports.get(player);
                if (!player.hasPermission("eternia.timing.bypass")) {
                    if (!playerTeleport.hasMoved()) {
                        if (playerTeleport.getCountdown() == 0) {
                            PaperLib.teleportAsync(player, playerTeleport.getWantLocation());
                            messages.sendMessage(playerTeleport.getMessage(), player);
                            EterniaServer.teleports.remove(player);
                        } else {
                            messages.sendMessage("teleport.tp.timing", "%cooldown%", playerTeleport.getCountdown(), player);
                            playerTeleport.decreaseCountdown();
                        }
                    } else {
                        messages.sendMessage("teleport.tp.move", player);
                        EterniaServer.teleports.remove(player);
                    }
                } else {
                    PaperLib.teleportAsync(player, playerTeleport.getWantLocation());
                    messages.sendMessage(playerTeleport.getMessage(), player);
                    EterniaServer.teleports.remove(player);
                }
            }
        }
    }

    private Location getWarp(final String warp) {
        return Vars.warps.containsKey(warp) ? Vars.warps.get(warp) : plugin.error;
    }

}