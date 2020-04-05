package com.eterniaserver.events;

import com.eterniaserver.configs.CVar;
import com.eterniaserver.configs.Vars;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

import java.util.concurrent.TimeUnit;

public class OnBedEnter implements Listener {
    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent event) {
        if (event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK) {
            if ((TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - getCooldown(event.getPlayer().getName()))) > CVar.getInt("server.cooldown") * 2) {
                    Vars.bed_cooldown.put(event.getPlayer().getName(), System.currentTimeMillis());
                }
        }
    }

    private long getCooldown(final String name) {
        if (!Vars.bed_cooldown.containsKey(name)) {
            return 0;
        } else {
            return Vars.bed_cooldown.get(name);
        }
    }

}
