package br.com.eterniaserver.eterniaserver.core;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.objects.CashItem;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scoreboard.Scoreboard;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class Vars {

    private Vars() {
        throw new IllegalStateException("Utility class");
    }

    private static Scoreboard scoreboard;
    private static int version = 0;
    private static Economy econ;

    protected static UUID balltop;
    protected static long baltopTime = 0;

    private static Location error;
    private static NumberFormat df2;

    protected static boolean chatMuted = false;
    protected static long nightTime = System.currentTimeMillis();

    protected static final List<World> skippingWorlds = new ArrayList<>();
    protected static final List<UUID> baltopList = new ArrayList<>();

    protected static final Map<Player, Boolean> vanished = new HashMap<>();

    protected static final Map<UUID, PlayerTeleport> teleports = new HashMap<>();
    protected static final Map<UUID, CashItem> cashItem = new HashMap<>();
    protected static final Map<UUID, PlayerProfile> playerProfile = new HashMap<>();
    protected static final Map<UUID, UUID> tpaRequests = new HashMap<>();
    protected static final Map<UUID, Boolean> godMode = new HashMap<>();
    protected static final Map<UUID, Long> tpaTime = new HashMap<>();
    protected static final Map<UUID, Location> playerLocationMap = new HashMap<>();
    protected static final Map<UUID, Boolean> onAfk = new HashMap<>();
    protected static final Map<UUID, Long> afkTime = new HashMap<>();
    protected static final Map<UUID, Integer> playersInPortal = new HashMap<>();
    protected static final Map<UUID, Boolean> spy = new HashMap<>();
    protected static final Map<UUID, Long> bedCooldown = new HashMap<>();
    protected static final Map<UUID, Location> back = new HashMap<>();
    protected static final Map<UUID, String> glowingColor = new HashMap<>();
    protected static final Map<UUID, String> tell = new HashMap<>();
    protected static final Map<UUID, UUID> chatLocked = new HashMap<>();

    protected static final Map<String, UUID> playersName = new HashMap<>();
    protected static final Map<String, Location> locations = new HashMap<>();
    protected static final Map<String, Long> kitsCooldown = new HashMap<>();
    protected static final Map<String, String> rewards = new HashMap<>();

    protected static Scoreboard getScoreboard() {
        return scoreboard;
    }

    protected static void setScoreboard(Scoreboard sc) {
        if (scoreboard == null) scoreboard = sc;
    }

    protected static int getVersion() {
        return version;
    }

    protected static void setVersion(int ver) {
        if (version == 0) version = ver;
    }

    protected static Economy getEcon() {
        return econ;
    }

    public static void setEcon(Economy economy) {
        if (econ == null) econ = economy;
    }

    public static Location getError() {
        return error;
    }

    public static NumberFormat getDf2() {
        if (df2 == null) {
            df2 = NumberFormat.getInstance(new Locale(EterniaServer.getString(Strings.MONEY_LANGUAGE), EterniaServer.getString(Strings.MONEY_COUNTRY)));
        }
        return df2;
    }

    public static void setError(Location location) {
        error = location;
    }

}
