package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.utils.PlayerTeleport;

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

            if (Vars.tpaRequests.containsKey(playerName) && Vars.tpaTime.containsKey(playerName) &&
                    TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - Vars.tpaTime.get(playerName)) >= 25) {
                Vars.tpaRequests.remove(playerName);
                Vars.tpaTime.remove(playerName);
            }
            if (location.getBlock().getType() == Material.NETHER_PORTAL) {
                if (!Vars.playersInPortal.containsKey(playerName)) {
                    Vars.playersInPortal.put(playerName, 7);
                } else if (Vars.playersInPortal.get(playerName) <= 1) {
                    if (location.getBlock().getType() == Material.NETHER_PORTAL) {
                        PaperLib.teleportAsync(player, getWarp());
                        messages.sendMessage("teleport.warp.done", player);
                    }
                } else if (Vars.playersInPortal.get(playerName) > 1) {
                    Vars.playersInPortal.put(playerName, Vars.playersInPortal.get(playerName) - 1);
                    if (Vars.playersInPortal.get(playerName) < 5) {
                        messages.sendMessage("server.nether-trap", "%cooldown%", Vars.playersInPortal.get(playerName), player);
                    }
                }
            } else {
                Vars.playersInPortal.remove(playerName);
            }
            if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - Vars.afkTime.get(playerName)) >= EterniaServer.serverConfig.getInt("server.afk-timer")) {
                if (EterniaServer.serverConfig.getBoolean("server.afk-kick")) {
                    if (!Vars.afk.contains(playerName) && !player.hasPermission("eternia.nokickbyafksorrymates")) {
                        messages.broadcastMessage("generic.afk.broadcast-kicked", Constants.PLAYER.get(), player.getDisplayName());
                        player.kickPlayer(messages.getMessage("generic.afk.kicked"));
                    }
                } else {
                    messages.broadcastMessage("generic.afk.enabled", Constants.PLAYER.get(), player.getDisplayName());
                    Vars.afk.add(playerName);
                }
            }
            if (Vars.teleports.containsKey(player)) {
                final PlayerTeleport playerTeleport = Vars.teleports.get(player);
                if (!player.hasPermission("eternia.timing.bypass")) {
                    if (!playerTeleport.hasMoved()) {
                        if (playerTeleport.getCountdown() == 0) {
                            PaperLib.teleportAsync(player, playerTeleport.getWantLocation());
                            messages.sendMessage(playerTeleport.getMessage(), player);
                            Vars.teleports.remove(player);
                        } else {
                            messages.sendMessage("teleport.tp.timing", "%cooldown%", playerTeleport.getCountdown(), player);
                            playerTeleport.decreaseCountdown();
                        }
                    } else {
                        messages.sendMessage("teleport.tp.move", player);
                        Vars.teleports.remove(player);
                    }
                } else {
                    PaperLib.teleportAsync(player, playerTeleport.getWantLocation());
                    messages.sendMessage(playerTeleport.getMessage(), player);
                    Vars.teleports.remove(player);
                }
            }
        }
    }

    private Location getWarp() {
        return Vars.warps.containsKey("spawn") ? Vars.warps.get("spawn") : plugin.error;
    }

}