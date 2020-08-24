package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;
import br.com.eterniaserver.eterniaserver.utils.CustomPlaceholder;
import br.com.eterniaserver.eterniaserver.utils.FormatInfo;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

public class Vars {

    private Vars() {
        throw new IllegalStateException("Utility class");
    }

    protected static boolean chatMuted = false;
    protected static long nightTime = System.currentTimeMillis();

    protected static final List<CustomPlaceholder> customPlaceholders = new ArrayList<>();
    protected static final List<World> skippingWorlds = new ArrayList<>();
    protected static final List<String> god = new ArrayList<>();
    protected static final List<String> afk = new ArrayList<>();

    protected static final Map<Player, PlayerTeleport> teleports = new HashMap<>();
    protected static final Map<String, UUID> playersName = new HashMap<>();
    protected static final Map<UUID, String> cashItem = new HashMap<>();

    protected static final Map<UUID, PlayerProfile> playerProfile = new HashMap<>();
    protected static final Map<String, Location> locations = new HashMap<>();
    protected static final Map<String, String> chatLocked = new HashMap<>();

    protected static final Map<String, Long> kitsCooldown = new HashMap<>();
    protected static final Map<String, Boolean> spy = new HashMap<>();
    protected static final Map<String, Integer> playersInPortal = new HashMap<>();
    protected static final Map<String, FormatInfo> uufi = new HashMap<>();
    protected static final Map<String, Location> back = new HashMap<>();
    protected static final Map<String, Long> tpaTime = new HashMap<>();
    protected static final Map<String, Long> afkTime = new HashMap<>();
    protected static final Map<String, Long> bedCooldown = new HashMap<>();
    protected static final Map<String, String> rewards = new HashMap<>();
    protected static final Map<String, String> tpaRequests = new HashMap<>();
    protected static final Map<String, String> glowingColor = new HashMap<>();
    protected static final Map<String, String> tell = new HashMap<>();
    protected static final Map<String, List<Player>> ignoredPlayer = new HashMap<>();


}
