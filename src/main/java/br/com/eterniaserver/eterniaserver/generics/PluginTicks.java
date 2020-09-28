package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;
import br.com.eterniaserver.paperlib.PaperLib;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PluginTicks extends BukkitRunnable {

    private final EterniaServer plugin;

    public PluginTicks(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {

            Location location = player.getLocation();
            String playerName = player.getName();

            tpaTime(playerName);
            checkNetherTrap(player, location, playerName);
            checkAFK(player, playerName);
            getPlayersInTp(player);
            refreshPlayers(player);
            optimizedMoveEvent(player, location);

        }
    }

    private void optimizedMoveEvent(Player player, Location location) {
        Location firstLocation = PluginVars.playerLocationMap.getOrDefault(player, location);

        if (!(firstLocation.getBlockX() == location.getBlockX() && firstLocation.getBlockY() == location.getBlockY() && firstLocation.getBlockZ() == location.getBlockZ())) {
            final String playerName = player.getName();
            PluginVars.afkTime.put(playerName, System.currentTimeMillis());
            if (PluginVars.afk.contains(playerName)) {
                PluginVars.afk.remove(playerName);
                Bukkit.broadcastMessage(EterniaServer.configs.getMessage(Messages.AFK_LEAVE, true, playerName, player.getDisplayName()));
            }
        }

        PluginVars.playerLocationMap.put(player, location);
    }

    private void refreshPlayers(Player player) {
        UUID uuid = UUIDFetcher.getUUIDOf(player.getName());
        PluginVars.playersName.put("@" + player.getName(), uuid);
        PluginVars.playersName.put("@" + player.getDisplayName(), uuid);
    }

    private void tpaTime(final String playerName) {
        if (PluginVars.tpaRequests.containsKey(playerName) && PluginVars.tpaTime.containsKey(playerName) &&
                TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - PluginVars.tpaTime.get(playerName)) >= 25) {
            PluginVars.tpaRequests.remove(playerName);
            PluginVars.tpaTime.remove(playerName);
        }
    }

    private void checkNetherTrap(final Player player, final Location location, final String playerName) {
        if (location.getBlock().getType() == Material.NETHER_PORTAL) {
            int time = PluginVars.playersInPortal.getOrDefault(playerName, -1);
            if (time == -1) {
                PluginVars.playersInPortal.put(playerName, 10);
            } else if (PluginVars.playersInPortal.get(playerName) == 1) {
                runSync(() -> PaperLib.teleportAsync(player, getWarp()));
                EterniaServer.configs.sendMessage(player, Messages.WARP_SPAWN_TELEPORTED);
            } else if (time > 1) {
                PluginVars.playersInPortal.put(playerName, time - 1);
                if ((time - 1) < 5) {
                    EterniaServer.configs.sendMessage(player, Messages.SERVER_NETHER_TRAP_TIMING, String.valueOf(time - 1));
                }
            }
        } else {
            PluginVars.playersInPortal.put(playerName, -1);
        }
    }

    private void checkAFK(final Player player, final String playerName) {
        if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - PluginVars.afkTime.getOrDefault(playerName, System.currentTimeMillis())) >= EterniaServer.configs.afkTimer) {
            if (EterniaServer.configs.afkKick) {
                if (!PluginVars.afk.contains(playerName) && !player.hasPermission("eternia.nokickbyafksorrymates")) {
                    Bukkit.broadcastMessage(EterniaServer.configs.getMessage(Messages.AFK_BROADCAST_KICK, true, playerName, player.getDisplayName()));
                    PluginVars.afkTime.remove(playerName);
                    runSync(() -> player.kickPlayer(EterniaServer.configs.getMessage(Messages.AFK_KICKED, true)));
                } else if (!PluginVars.afk.contains(playerName) && !player.hasPermission("eternia.nokickbyafksorrymates") && player.hasPermission("eternia.afk")) {
                    Bukkit.broadcastMessage(EterniaServer.configs.getMessage(Messages.AFK_AUTO_ENTER, false, playerName, player.getDisplayName()));
                    APIPlayer.putAfk(playerName);
                }
            } else {
                Bukkit.broadcastMessage(EterniaServer.configs.getMessage(Messages.AFK_AUTO_ENTER, false, playerName, player.getDisplayName()));
                PluginVars.afk.add(playerName);
            }
        }
    }

    private void getPlayersInTp(final Player player) {
        if (PluginVars.teleports.containsKey(player)) {
            final PlayerTeleport playerTeleport = PluginVars.teleports.get(player);
            if (!player.hasPermission("eternia.timing.bypass")) {
                if (!playerTeleport.hasMoved()) {
                    if (playerTeleport.getCountdown() == 0) {
                        runSync(()-> PaperLib.teleportAsync(player, playerTeleport.getWantLocation()));
                        player.sendMessage(playerTeleport.getMessage());
                        PluginVars.teleports.remove(player);
                    } else {
                        EterniaServer.configs.sendMessage(player, Messages.TELEPORT_TIMING, String.valueOf(playerTeleport.getCountdown()));
                        playerTeleport.decreaseCountdown();
                    }
                } else {
                    EterniaServer.configs.sendMessage(player, Messages.TELEPORT_MOVED);
                    PluginVars.teleports.remove(player);
                }
            } else {
                runSync(()-> PaperLib.teleportAsync(player, playerTeleport.getWantLocation()));
                player.sendMessage(playerTeleport.getMessage());
                PluginVars.teleports.remove(player);
            }
        }
    }

    private Location getWarp() {
        return PluginVars.locations.getOrDefault("warp.spawn", PluginVars.error);
    }

    private void runSync(Runnable runnable) {
        if (EterniaServer.configs.asyncCheck) {
            Bukkit.getScheduler().runTask(plugin, runnable);
            return;
        }
        runnable.run();
    }

}