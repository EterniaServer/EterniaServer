package br.com.eterniaserver.player;

import br.com.eterniaserver.EterniaServer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerTeleport {

    private final Player player;
    private final Location first_location;
    private final Location wantLocation;
    private int cooldown = EterniaServer.configs.getInt("server.cooldown");
    private final String message;

    public PlayerTeleport(Player player, Location wantLocation, String message) {
        this.player = player;
        this.first_location = player.getLocation();
        this.wantLocation = wantLocation;
        this.message = message;
    }

    public boolean hasMoved() {
        return first_location.distanceSquared(player.getLocation()) != 0;
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
