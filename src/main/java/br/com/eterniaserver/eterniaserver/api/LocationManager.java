package br.com.eterniaserver.eterniaserver.api;

import org.bukkit.Location;

public interface LocationManager {

    Location getLocation(String name);

    void putLocation(String name, Location location);

    void removeLocation(String name);

}
