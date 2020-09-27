package br.com.eterniaserver.eterniaserver.objects;

import br.com.eterniaserver.eterniaserver.Configs;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerTeleport {

    private final Player player;
    private final Location firstLocation;
    private final Location wantLocation;
    private final String message;
    private int cooldown;

    public PlayerTeleport(Player player, Location wantLocation, String message) {
        this.player = player;
        this.firstLocation = player.getLocation();
        this.wantLocation = wantLocation;
        this.message = message;
        this.cooldown = Configs.getInstance().cooldown;
    }

    public boolean hasMoved() {
        final Location location = player.getLocation();
        return !(firstLocation.getBlockX() == location.getBlockX() && firstLocation.getBlockY() == location.getBlockY() && firstLocation.getBlockZ() == location.getBlockZ());
    }

    public int getCountdown() {
        return cooldown;
    }

    public void decreaseCountdown() {
        cooldown -= 1;
    }

    public Location getWantLocation() {
        return wantLocation;
    }

    public String getMessage() {
        return message;
    }

}
