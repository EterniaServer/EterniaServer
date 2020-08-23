package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.configs.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Strings;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;
import br.com.eterniaserver.paperlib.PaperLib;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Checks extends BukkitRunnable {

    private final EterniaServer plugin;

    public Checks(EterniaServer plugin) {
        this.plugin = plugin;
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
            refreshPlayers(player);
        }
    }

    private void refreshPlayers(Player player) {
        UUID uuid = UUIDFetcher.getUUIDOf(player.getName());
        Vars.playersName.put("@" + player.getName(), uuid);
        Vars.playersName.put("@" + player.getDisplayName(), uuid);
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
                    runSync(() -> PaperLib.teleportAsync(player, getWarp()));
                    player.sendMessage(Strings.MSG_WARP_DONE);
                }
            } else if (Vars.playersInPortal.get(playerName) > 1) {
                Vars.playersInPortal.put(playerName, Vars.playersInPortal.get(playerName) - 1);
                if (Vars.playersInPortal.get(playerName) < 5) {
                    player.sendMessage(Strings.MSG_NETHER_TRAP.replace(Constants.COOLDOWN, String.valueOf(Vars.playersInPortal.get(playerName))));
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
                    Bukkit.broadcastMessage(InternMethods.putName(player, Strings.MSG_AFK_BROAD));
                    runSync(() -> player.kickPlayer(Strings.MSG_AFK_KICKED));
                }
            } else {
                Bukkit.broadcastMessage(InternMethods.putName(player, Strings.MSG_AFK_ENABLE));
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
                        runSync(()-> PaperLib.teleportAsync(player, playerTeleport.getWantLocation()));
                        player.sendMessage(playerTeleport.getMessage());
                        Vars.teleports.remove(player);
                    } else {
                        player.sendMessage(Strings.MSG_TELEPORT_TIMING.replace(Constants.COOLDOWN, String.valueOf(playerTeleport.getCountdown())));
                        playerTeleport.decreaseCountdown();
                    }
                } else {
                    player.sendMessage(Strings.MSG_TELEPORT_MOVE);
                    Vars.teleports.remove(player);
                }
            } else {
                runSync(()-> PaperLib.teleportAsync(player, playerTeleport.getWantLocation()));
                player.sendMessage(playerTeleport.getMessage());
                Vars.teleports.remove(player);
            }
        }
    }

    private Location getWarp() {
        return Vars.warps.getOrDefault("spawn", EterniaServer.error);
    }

    public void runSync(Runnable runnable) {
        if (EterniaServer.serverConfig.getBoolean("server.async-check")) {
            Bukkit.getScheduler().runTask(plugin, runnable);
            return;
        }
        runnable.run();
    }

}