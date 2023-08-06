package br.com.eterniaserver.eterniaserver.objects;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;


public class PlayerTeleport {

    private final Location wantLocation;
    private final Component message;
    private int cooldown;

    public PlayerTeleport(int cooldown, Location wantLocation, Component message) {
        this.wantLocation = wantLocation;
        this.message = message;
        this.cooldown = cooldown;
    }


    public int getCountdown() {
        return cooldown;
    }

    public void decreaseCountdown(int ticks) {
        cooldown -= ticks;
    }

    public Location getWantLocation() {
        return wantLocation;
    }

    public Component getMessage() {
        return message;
    }

}
