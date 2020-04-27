package br.com.eterniaserver.configs;

import br.com.eterniaserver.modules.chatmanager.act.utils.CustomPlaceholder;
import br.com.eterniaserver.modules.chatmanager.act.utils.FormatInfo;
import br.com.eterniaserver.player.PlayerTeleport;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

public class Vars {

    public static final Location error = new Location(Bukkit.getWorld("world"), 666, 666, 666, 666, 666);

    public static final List<World> skipping_worlds = new ArrayList<>();
    public static final List<String> god = new ArrayList<>();
    public static final List<String> afk = new ArrayList<>();
    public static final List<CustomPlaceholder> customPlaceholders = new ArrayList<>();

    public static final List<String> player_bal = new ArrayList<>();
    public static final List<String> player_exp = new ArrayList<>();
    public static final List<String> player_homes = new ArrayList<>();
    public static final List<String> player_cooldown = new ArrayList<>();

    public static final HashMap<String, Long> bed_cooldown = new HashMap<>();
    public static final HashMap<String, Long> afktime = new HashMap<>();
    public static final HashMap<String, Double> balances = new HashMap<>();
    public static final HashMap<String, String> tpa_requests = new HashMap<>();
    public static final HashMap<String, String> kits_cooldown = new HashMap<>();
    public static final HashMap<String, String> player_login = new HashMap<>();
    public static final HashMap<String, String> tell = new HashMap<>();
    public static final HashMap<String, Integer> global = new HashMap<>();
    public static final HashMap<String, Integer> xp = new HashMap<>();
    public static final HashMap<String, Integer> playersInPortal = new HashMap<>();
    public static final HashMap<String, String[]> home = new HashMap<>();
    public static final HashMap<String, Location> warps = new HashMap<>();
    public static final HashMap<String, Location> back = new HashMap<>();
    public static final HashMap<String, Location> shops = new HashMap<>();
    public static final HashMap<String, Location> homes = new HashMap<>();
    public static final HashMap<UUID, FormatInfo> uufi = new HashMap<>();
    public static final HashMap<Player, PlayerTeleport> teleports = new HashMap<>();

}