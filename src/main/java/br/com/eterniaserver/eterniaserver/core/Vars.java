package br.com.eterniaserver.eterniaserver.core;

import org.bukkit.entity.Player;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Vars {

    private Vars() {
        throw new IllegalStateException("Utility class");
    }

    private static int version = 0;

    private static Location error;

    protected static boolean chatMuted = false;

    protected static final Map<Player, Boolean> vanished = new HashMap<>();

    protected static final Map<UUID, Boolean> godMode = new HashMap<>();
    protected static final Map<UUID, Location> playerLocationMap = new HashMap<>();
    protected static final Map<UUID, Integer> playersInPortal = new HashMap<>();
    protected static final Map<UUID, Boolean> spy = new HashMap<>();
    protected static final Map<UUID, Location> back = new HashMap<>();
    protected static final Map<UUID, String> glowingColor = new HashMap<>();
    protected static final Map<UUID, String> tell = new HashMap<>();
    protected static final Map<UUID, UUID> chatLocked = new HashMap<>();

    protected static final Map<String, UUID> playersName = new HashMap<>();
    protected static final Map<String, Location> locations = new HashMap<>();
    protected static final Map<String, String> rewards = new HashMap<>();

    protected static int getVersion() {
        return version;
    }

    protected static void setVersion(int ver) {
        if (version == 0) version = ver;
    }

    public static Location getError() {
        return error;
    }

    public static void setError(Location location) {
        error = location;
    }

}
