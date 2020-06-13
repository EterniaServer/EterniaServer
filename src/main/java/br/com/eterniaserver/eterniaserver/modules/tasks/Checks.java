package br.com.eterniaserver.eterniaserver.modules.tasks;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.objects.Vars;
import br.com.eterniaserver.eterniaserver.modules.teleportsmanager.TeleportsManager;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;

import io.papermc.lib.PaperLib;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class Checks extends org.bukkit.scheduler.BukkitRunnable {

    private final EterniaServer plugin;
    private final EFiles messages;
    private final Vars vars;
    private final TeleportsManager teleportsManager;

    public Checks(EterniaServer plugin, TeleportsManager teleportsManager) {
        this.plugin = plugin;
        this.messages = plugin.getEFiles();
        this.vars = plugin.getVars();
        this.teleportsManager = teleportsManager;
    }

    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Location location = player.getLocation();
            final String playerName = player.getName();

            if (vars.tpa_requests.containsKey(playerName)) {
                if (vars.tpa_time.containsKey(playerName) &&
                        TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - vars.tpa_time.get(playerName)) >= 25) {
                    vars.tpa_requests.remove(playerName);
                    vars.tpa_time.remove(playerName);
                }
            }
            if (plugin.serverConfig.getBoolean("server.void-tp") && location.getY() < -10) {
                PaperLib.teleportAsync(player, teleportsManager.getWarp("spawn"));
            }
            if (location.getBlock().getType() == Material.NETHER_PORTAL) {
                if (!vars.playersInPortal.containsKey(playerName)) {
                    vars.playersInPortal.put(playerName, 7);
                } else if (vars.playersInPortal.get(playerName) <= 1) {
                    if (location.getBlock().getType() == Material.NETHER_PORTAL) {
                        PaperLib.teleportAsync(player, teleportsManager.getWarp("spawn"));
                        messages.sendMessage("teleport.warp.done", player);
                    }
                } else {
                    vars.playersInPortal.put(playerName, vars.playersInPortal.get(playerName) - 1);
                    if (vars.playersInPortal.get(playerName) < 5) {
                        messages.sendMessage("server.nether-trap", "%cooldown%", vars.playersInPortal.get(playerName), player);
                    }
                }
            } else {
                vars.playersInPortal.remove(playerName);
            }
            if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - vars.afktime.get(playerName)) >= plugin.serverConfig.getInt("server.afk-timer")) {
                if (plugin.serverConfig.getBoolean("server.afk-kick")) {
                    if (!vars.afk.contains(playerName) && !player.hasPermission("eternia.nokickbyafksorrymates")) {
                        messages.broadcastMessage("generic.afk.broadcast-kicked", "%player_name%", playerName);
                        Bukkit.getScheduler().runTask(plugin, () -> player.kickPlayer(messages.getMessage("generic.afk.kicked")));
                    }
                } else {
                    messages.broadcastMessage("generic.afk.enabled", "%player_name%", playerName);
                    vars.afk.add(playerName);
                }
            }
            if (vars.teleports.containsKey(player)) {
                final PlayerTeleport playerTeleport = vars.teleports.get(player);
                if (!player.hasPermission("eternia.timing.bypass")) {
                    if (!playerTeleport.hasMoved()) {
                        if (playerTeleport.getCountdown() == 0) {
                            PaperLib.teleportAsync(player, playerTeleport.getWantLocation());
                            messages.sendMessage(playerTeleport.getMessage(), player);
                            vars.teleports.remove(player);
                        } else {
                            messages.sendMessage("teleport.tp.timing", "%cooldown%", playerTeleport.getCountdown(), player);
                            playerTeleport.decreaseCountdown();
                        }
                    } else {
                        messages.sendMessage("teleport.tp.move", player);
                        vars.teleports.remove(player);
                    }
                } else {
                    PaperLib.teleportAsync(player, playerTeleport.getWantLocation());
                    messages.sendMessage(playerTeleport.getMessage(), player);
                    vars.teleports.remove(player);
                }
            }
        }
    }
}