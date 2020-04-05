package com.eterniaserver.modules.afkmanager;

import com.eterniaserver.configs.CVar;
import com.eterniaserver.configs.Vars;
import com.eterniaserver.configs.MVar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class AFKTimer extends org.bukkit.scheduler.BukkitRunnable {
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - Vars.afktime.get(player)) >= CVar.getInt("server.afk-timer")) {
                if (!player.hasPermission("eternia.afk")) {
                    player.kickPlayer(MVar.getMessage("server.afk-kick"));
                }
            }
        }
    }
}
