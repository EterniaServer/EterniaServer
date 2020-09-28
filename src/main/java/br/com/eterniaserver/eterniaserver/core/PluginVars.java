package br.com.eterniaserver.eterniaserver.core;

import br.com.eterniaserver.eterniaserver.objects.CashGui;
import br.com.eterniaserver.eterniaserver.objects.FormatInfo;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scoreboard.Scoreboard;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PluginVars {

    private PluginVars() {
        throw new IllegalStateException("Utility class");
    }

    private static Scoreboard scoreboard;
    private static int version = 0;
    private static CashGui cashGui;
    private static Economy econ;

    private static Location error;
    protected static final NumberFormat df2 = NumberFormat.getInstance(new Locale("pt", "BR"));

    public static final List<String> entityList = List.of("BEE", "BLAZE", "CAT", "CAVE_SPIDER", "CHICKEN", "COD",
            "COW", "CREEPER", "DOLPHIN", "DONKEY", "DROWNED", "ELDER_GUARDIAN", "ENDER_DRAGON", "ENDERMAN", "ENDERMITE",
            "EVOKER", "FOX", "GHAST", "GIANT", "GUARDIAN", "HOGLIN", "HORSE", "HUSK", "ILLUSIONER", "IRON_GOLEM",
            "MAGMA_CUBE", "MULE", "PANDA", "PARROT", "PHANTOM", "PIG", "PIGLIN", "PILLAGER", "POLAR_BEAR", "PUFFERFISH",
            "RABBIT", "RAVAGER", "SALMON", "SHEEP", "SILVERFISH", "SKELETON", "SKELETON_HORSE", "SLIME", "SNOW_GOLEM",
            "SPIDER", "SQUID", "STRAY", "STRIDER", "TURTLE", "VEX", "VILLAGER", "VINDICATOR", "WITCH", "WITHER",
            "WITHER_SKELETON", "WOLF", "ZOGLIN", "ZOMBIE", "ZOMBIE_HORSE", "ZOMBIFIED_PIGLIN", "ZOMBIE_VILLAGER");

    public static final List<ChatColor> colors = List.of(ChatColor.BLACK, ChatColor.DARK_BLUE, ChatColor.DARK_GREEN,
            ChatColor.DARK_AQUA, ChatColor.DARK_RED, ChatColor.DARK_PURPLE, ChatColor.GOLD, ChatColor.GRAY,
            ChatColor.DARK_GRAY, ChatColor.BLUE, ChatColor.GREEN, ChatColor.AQUA, ChatColor.RED, ChatColor.LIGHT_PURPLE,
            ChatColor.YELLOW, ChatColor.WHITE);

    protected static boolean chatMuted = false;
    protected static long nightTime = System.currentTimeMillis();

    protected static final List<UtilCustomPlaceholder> customPlaceholders = new ArrayList<>();
    protected static final List<World> skippingWorlds = new ArrayList<>();

    protected static final Set<String> god = new HashSet<>();
    protected static final Set<String> afk = new HashSet<>();

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
    protected static final Map<Player, Location> playerLocationMap = new HashMap<>();
    protected static final Map<String, Long> bedCooldown = new HashMap<>();
    protected static final Map<String, String> rewards = new HashMap<>();
    protected static final Map<String, String> tpaRequests = new HashMap<>();
    protected static final Map<String, String> glowingColor = new HashMap<>();
    protected static final Map<String, String> tell = new HashMap<>();
    protected static final Map<String, List<Player>> ignoredPlayer = new HashMap<>();

    protected static Scoreboard getScoreboard() {
        return scoreboard;
    }

    protected static void setScoreboard(Scoreboard sc) {
        if (scoreboard == null) scoreboard = sc;
    }

    protected static CashGui getCashGui() {
        return cashGui;
    }

    protected static void setCashGui(CashGui cashG) {
        if (cashGui == null) cashGui = cashG;
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

    public static void setError(Location location) {
        error = location;
    }
}
