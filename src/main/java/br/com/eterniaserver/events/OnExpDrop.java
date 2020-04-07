package br.com.eterniaserver.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExpBottleEvent;

public class OnExpDrop implements Listener {
    @EventHandler
    public void onExpBottle(ExpBottleEvent event) {
        event.setExperience(10);
    }
}