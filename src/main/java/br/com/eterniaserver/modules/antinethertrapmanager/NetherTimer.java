package br.com.eterniaserver.modules.antinethertrapmanager;

import br.com.eterniaserver.configs.CVar;
import br.com.eterniaserver.configs.MVar;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.configs.methods.PlayerMessage;
import br.com.eterniaserver.modules.teleportsmanager.sql.QueriesW;
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
                            player.teleport(QueriesW.getWarp("Spawn"));
                            new PlayerMessage("warps.spawn", player);
                        }
                    } else {
                        Vars.playersInPortal.put(player.getName(), Vars.playersInPortal.get(player.getName()) - 1);
                        if (Vars.playersInPortal.get(player.getName()) < 5) {
                            new PlayerMessage("server.nether-trap", Vars.playersInPortal.get(player.getName()), player);
                        }
                    }
                } else Vars.playersInPortal.remove(player.getName());
                if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - Vars.afktime.get(player.getName())) >= CVar.getInt("server.afk-timer")) {
                    if (!player.hasPermission("eternia.afk")) {
                        player.kickPlayer(MVar.getMessage("server.afk-kick"));
                    }
                }
            }
        }
    }
}