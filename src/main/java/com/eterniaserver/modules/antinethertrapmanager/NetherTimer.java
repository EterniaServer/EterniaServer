package com.eterniaserver.modules.antinethertrapmanager;

import com.eterniaserver.configs.Vars;
import com.eterniaserver.configs.methods.PlayerMessage;
import com.eterniaserver.modules.teleportsmanager.sql.QueriesW;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class NetherTimer extends org.bukkit.scheduler.BukkitRunnable {
    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            synchronized (player) {
                if (player.getLocation().getBlock().getType() == Material.NETHER_PORTAL) {
                    if (!Vars.playersInPortal.containsKey(player)) {
                        Vars.playersInPortal.put(player, 7);
                    } else if (Vars.playersInPortal.get(player) <= 1) {
                        Location player_location = player.getLocation();
                        if (player_location.getBlock().getType() == Material.NETHER_PORTAL) {
                            player.teleport(QueriesW.getWarp("Spawn"));
                            new PlayerMessage("warps.spawn", player);
                        }
                    } else {
                        Vars.playersInPortal.put(player, Vars.playersInPortal.get(player) - 1);
                        if (Vars.playersInPortal.get(player) < 5) {
                            new PlayerMessage("server.nether-trap", Vars.playersInPortal.get(player), player);
                        }
                    }
                } else Vars.playersInPortal.remove(player);
            }
        }
    }
}