package br.com.eterniaserver.eterniaserver.objects;

import org.bukkit.Location;


public class PlayerTeleport {

    private final Location wantLocation;
    private final String message;
    private int cooldown;

    public PlayerTeleport(final int cooldown, final Location wantLocation, final String message) {
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

    public String getMessage() {
        return message;
    }

}
