package br.com.eterniaserver.eterniaserver.api.interfaces;

import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;
import org.bukkit.Location;

import java.util.UUID;

public interface LocationManager {

    Location getLocation(String name);

    void putLocation(String name, Location location);

    void removeLocation(String name);

    PlayerTeleport getTeleport(UUID uuid);

    void putTeleport(UUID uuid, PlayerTeleport playerTeleport);

    void removeTeleport(UUID uuid);

}
