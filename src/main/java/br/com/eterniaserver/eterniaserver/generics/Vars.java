package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.utils.CustomPlaceholder;
import br.com.eterniaserver.eterniaserver.utils.FormatInfo;
import br.com.eterniaserver.eterniaserver.utils.PlayerTeleport;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Vars {

    private Vars() {
        throw new IllegalStateException("Utility class");
    }

    protected static long nightTime = System.currentTimeMillis();

    protected static final List<CustomPlaceholder> customPlaceholders = new ArrayList<>();
    protected static final List<ItemStack> cashGui = new ArrayList<>();
    protected static final List<String> god = new ArrayList<>();
    protected static final List<String> afk = new ArrayList<>();
    protected static final List<World> skippingWorlds = new ArrayList<>();

    protected static final Map<Player, PlayerTeleport> teleports = new HashMap<>();

    protected static final Map<String, Boolean> spy = new HashMap<>();

    protected static final Map<String, Double> balances = new HashMap<>();

    protected static final Map<String, Integer> cash = new HashMap<>();
    protected static final Map<String, Integer> xp = new HashMap<>();
    protected static final Map<String, Integer> cashBuy = new HashMap<>();
    protected static final Map<String, Integer> global = new HashMap<>();
    protected static final Map<String, Integer> playersInPortal = new HashMap<>();

    protected static final Map<String, FormatInfo> uufi = new HashMap<>();

    protected static final Map<String, Location> homes = new HashMap<>();
    protected static final Map<String, Location> shops = new HashMap<>();
    protected static final Map<String, Location> warps = new HashMap<>();
    protected static final Map<String, Location> back = new HashMap<>();

    protected static final Map<String, Long> tpaTime = new HashMap<>();
    protected static final Map<String, Long> afkTime = new HashMap<>();
    protected static final Map<String, Long> playerMuted = new HashMap<>();
    protected static final Map<String, Long> bedCooldown = new HashMap<>();
    protected static final Map<String, Long> kitsCooldown = new HashMap<>();
    protected static final Map<String, Long> playerLogin = new HashMap<>();

    protected static final Map<String, String> rewards = new HashMap<>();
    protected static final Map<String, String> nick = new HashMap<>();
    protected static final Map<String, String> nickname = new HashMap<>();
    protected static final Map<String, String> tpaRequests = new HashMap<>();
    protected static final Map<String, String> glowingColor = new HashMap<>();
    protected static final Map<String, String> tell = new HashMap<>();
    protected static final Map<String, String[]> home = new HashMap<>();


}
