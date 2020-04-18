package br.com.eterniaserver.modules.antinethertrapmanager;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Strings;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.modules.teleportsmanager.sql.WarpsAPI;
import io.papermc.lib.PaperLib;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class NetherTimer extends org.bukkit.scheduler.BukkitRunnable {
    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            synchronized (player) {
                if (player.getLocation().getBlock().getType() == Material.NETHER_PORTAL) {
                    if (!Vars.playersInPortal.containsKey(player.getName())) {
                        Vars.playersInPortal.put(player.getName(), 7);
                    } else if (Vars.playersInPortal.get(player.getName()) <= 1) {
                        Location player_location = player.getLocation();
                        if (player_location.getBlock().getType() == Material.NETHER_PORTAL) {
                            PaperLib.teleportAsync(player, WarpsAPI.getWarp("spawn"));
                            Messages.PlayerMessage("warps.warp", "Spawn", player);
                        }
                    } else {
                        Vars.playersInPortal.put(player.getName(), Vars.playersInPortal.get(player.getName()) - 1);
                        if (Vars.playersInPortal.get(player.getName()) < 5) {
                            Messages.PlayerMessage("server.nether-trap", Vars.playersInPortal.get(player.getName()), player);
                        }
                    }
                } else Vars.playersInPortal.remove(player.getName());
                if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - Vars.afktime.get(player.getName())) >= EterniaServer.configs.getInt("server.afk-timer")) {
                    if (!Vars.afk.contains(player.getName())) {
                        player.kickPlayer(Strings.getMessage("server.afk-kick"));
                    }
                }
            }
        }
    }
}