package br.com.eterniaserver.modules.playerchecksmanager.tasks;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Strings;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.modules.teleportsmanager.TeleportsManager;
import br.com.eterniaserver.player.PlayerTeleport;

import io.papermc.lib.PaperLib;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class Checks extends org.bukkit.scheduler.BukkitRunnable {

    private final EterniaServer plugin;
    private final Messages messages;
    private final Strings strings;
    private final Vars vars;
    private final TeleportsManager teleportsManager;

    public Checks(EterniaServer plugin, Messages messages, Strings strings, Vars vars, TeleportsManager teleportsManager) {
        this.plugin = plugin;
        this.messages = messages;
        this.strings = strings;
        this.vars = vars;
        this.teleportsManager = teleportsManager;
    }

    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getLocation().getBlock().getType() == Material.NETHER_PORTAL) {
                if (!vars.playersInPortal.containsKey(player.getName())) {
                    vars.playersInPortal.put(player.getName(), 7);
                } else if (vars.playersInPortal.get(player.getName()) <= 1) {
                    Location player_location = player.getLocation();
                    if (player_location.getBlock().getType() == Material.NETHER_PORTAL) {
                        PaperLib.teleportAsync(player, teleportsManager.getWarp("spawn"));
                        messages.PlayerMessage("warps.warp", player);
                    }
                } else {
                    vars.playersInPortal.put(player.getName(), vars.playersInPortal.get(player.getName()) - 1);
                    if (vars.playersInPortal.get(player.getName()) < 5) {
                        messages.PlayerMessage("server.nether-trap", "%cooldown%", vars.playersInPortal.get(player.getName()), player);
                    }
                }
            } else vars.playersInPortal.remove(player.getName());
            if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - vars.afktime.get(player.getName())) >= plugin.serverConfig.getInt("server.afk-timer")) {
                if (plugin.serverConfig.getBoolean("server.afk-kick")) {
                    if (!vars.afk.contains(player.getName()) && !player.hasPermission("eternia.nokickbyafksorrymates")) {
                        messages.BroadcastMessage("text.afkkick", "%player_name%", player.getName());
                        player.kickPlayer(strings.getMessage("server.afk-kick"));
                    }
                } else {
                    messages.BroadcastMessage("text.afke", "%player_name%", player.getName());
                    vars.afk.add(player.getName());
                }
            }
            if (vars.teleports.containsKey(player)) {
                final PlayerTeleport playerTeleport = vars.teleports.get(player);
                if (!player.hasPermission("eternia.timing.bypass")) {
                    if (!playerTeleport.hasMoved()) {
                        if (playerTeleport.getCountdown() == 0) {
                            PaperLib.teleportAsync(player, playerTeleport.getWantLocation());
                            messages.PlayerMessage(playerTeleport.getMessage(), player);
                            vars.teleports.remove(player);
                        } else {
                            messages.PlayerMessage("teleport.timing", "%cooldown%", playerTeleport.getCountdown(), player);
                            playerTeleport.decreaseCountdown();
                        }
                    } else {
                        messages.PlayerMessage("warps.move", player);
                        vars.teleports.remove(player);
                    }
                } else {
                    PaperLib.teleportAsync(player, playerTeleport.getWantLocation());
                    messages.PlayerMessage(playerTeleport.getMessage(), player);
                    vars.teleports.remove(player);
                }
            }
        }
    }
}