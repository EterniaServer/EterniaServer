package br.com.eterniaserver.eterniaserver.player;

import br.com.eterniaserver.eterniaserver.EterniaServer;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerTeleport {

    private final Player player;
    private final Location first_location, wantLocation;
    private final String message;
    private int cooldown;

    public PlayerTeleport(Player player, Location wantLocation, String message, EterniaServer plugin) {
        this.player = player;
        this.first_location = player.getLocation();
        this.wantLocation = wantLocation;
        this.message = message;
        this.cooldown = plugin.serverConfig.getInt("server.cooldown");
    }

    public boolean hasMoved() {
        if (first_location.getWorld() != player.getLocation().getWorld()) return true;
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
