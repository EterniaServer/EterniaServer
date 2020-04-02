package com.eterniaserver.events;

import com.eterniaserver.configs.MVar;
import com.eterniaserver.configs.Vars;
import com.eterniaserver.api.WarpAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class NetherPortal extends org.bukkit.scheduler.BukkitRunnable {
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
                            player.teleport(WarpAPI.getWarp("Spawn"));
                            MVar.playerMessage("warps.spawn", player);
                        }
                    } else {
                        Vars.playersInPortal.put(player, Vars.playersInPortal.get(player) - 1);
                        if (Vars.playersInPortal.get(player) < 5) {
                            MVar.playerReplaceMessage("server.nether-trap", Vars.playersInPortal.get(player), player);
                        }
                    }
                } else Vars.playersInPortal.remove(player);
            }
        }
    }
}