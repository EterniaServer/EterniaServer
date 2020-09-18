package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;
import br.com.eterniaserver.eterniaserver.objects.FormatInfo;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PluginVars {

    private PluginVars() {
        throw new IllegalStateException("Utility class");
    }

    public static final Location error = new Location(Bukkit.getWorld("world"), 666, 666, 666, 666, 666);
    public static final DecimalFormat df2 = new DecimalFormat(".##");

    public static final List<String> arrData = List.of("tblack", "tdarkblue", "tdarkgreen", "tdarkaqua", "tdarkred",
            "tdarkpurple", "tgold", "tlightgray", "tdarkgray", "tblue", "tgreen", "taqua", "tred", "tpurple", "tyellow",
            "twhite");

    public static final List<String> entityList = List.of("BEE", "BLAZE", "CAT", "CAVE_SPIDER", "CHICKEN", "COD",
            "COW", "CREEPER", "DOLPHIN", "DONKEY", "DROWNED", "ELDER_GUARDIAN", "ENDER_DRAGON", "ENDERMAN", "ENDERMITE",
            "EVOKER", "FOX", "GHAST", "GIANT", "GUARDIAN", "HOGLIN", "HORSE", "HUSK", "ILLUSIONER", "IRON_GOLEM",
            "MAGMA_CUBE", "MULE", "PANDA", "PARROT", "PHANTOM", "PIG", "PIGLIN", "PILLAGER", "POLAR_BEAR", "PUFFERFISH",
            "RABBIT", "RAVAGER", "SALMON", "SHEEP", "SILVERFISH", "SKELETON", "SKELETON_HORSE", "SLIME", "SNOW_GOLEM",
            "SPIDER", "SQUID", "STRAY", "STRIDER", "TURTLE", "VEX", "VILLAGER", "VINDICATOR", "WITCH", "WITHER",
            "WITHER_SKELETON", "WOLF", "ZOGLIN", "ZOMBIE", "ZOMBIE_HORSE", "ZOMBIFIED_PIGLIN", "ZOMBIE_VILLAGER");

    public static final List<String> colorsString = List.of("black", "darkblue",
            "darkgreen", "darkaqua", "darkred", "darkpurple", "gold", "lightgray", "darkgray", "blue", "green",
            "aqua", "red", "purple", "yellow", "white");

    public static final List<ChatColor> colors = List.of(ChatColor.BLACK, ChatColor.DARK_BLUE, ChatColor.DARK_GREEN,
            ChatColor.DARK_AQUA, ChatColor.DARK_RED, ChatColor.DARK_PURPLE, ChatColor.GOLD, ChatColor.GRAY,
            ChatColor.DARK_GRAY, ChatColor.BLUE, ChatColor.GREEN, ChatColor.AQUA, ChatColor.RED, ChatColor.LIGHT_PURPLE,
            ChatColor.YELLOW, ChatColor.WHITE);

    protected static boolean chatMuted = false;
    protected static long nightTime = System.currentTimeMillis();

    protected static final List<UtilCustomPlaceholder> customPlaceholders = new ArrayList<>();
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
