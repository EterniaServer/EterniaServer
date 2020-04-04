package com.eterniaserver.events;

import com.eterniaserver.configs.CVar;
import com.eterniaserver.configs.Vars;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class OnPlayerMove extends org.bukkit.scheduler.BukkitRunnable {
    // OnPlayerMoveEvent um pouco mais leve.
    public void run(){
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (Vars.playerposition.get(player) != player.getLocation()) {
                if (CVar.getBool("modules.afk")) {
                    Vars.afktime.put(player, System.currentTimeMillis());
                }
                if (CVar.getBool("modules.teleports")) {
                    Vars.moved.put(player, true);
                }
            }
        }
    }
}
