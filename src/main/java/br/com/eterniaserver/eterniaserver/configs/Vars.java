package br.com.eterniaserver.eterniaserver.configs;

import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.utils.CustomPlaceholder;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.utils.FormatInfo;
import br.com.eterniaserver.eterniaserver.player.PlayerTeleport;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Vars {

    public final Location error = new Location(Bukkit.getWorld("world"), 666, 666, 666, 666, 666);

    public final List<World> skipping_worlds = new ArrayList<>();
    public final List<String> god = new ArrayList<>();
    public final List<String> afk = new ArrayList<>();
    public final List<CustomPlaceholder> customPlaceholders = new ArrayList<>();

    public final List<String> player_bal = new ArrayList<>();
    public final List<String> player_exp = new ArrayList<>();
    public final List<String> player_homes = new ArrayList<>();
    public final List<String> player_cooldown = new ArrayList<>();

    public final HashMap<String, Long> tpa_time = new HashMap<>();
    public final HashMap<String, Long> player_muted = new HashMap<>();
    public final HashMap<String, Long> bed_cooldown = new HashMap<>();
    public final HashMap<String, Long> afktime = new HashMap<>();
    public final HashMap<String, Double> balances = new HashMap<>();
    public final HashMap<String, String> tpa_requests = new HashMap<>();
    public final HashMap<String, String> kits_cooldown = new HashMap<>();
    public final HashMap<String, String> player_login = new HashMap<>();
    public final HashMap<String, String> tell = new HashMap<>();
    public final HashMap<Player, Boolean> spy = new HashMap<>();
    public final HashMap<String, Integer> global = new HashMap<>();
    public final HashMap<String, Integer> xp = new HashMap<>();
    public final HashMap<String, Integer> playersInPortal = new HashMap<>();
    public final HashMap<String, String[]> home = new HashMap<>();
    public final HashMap<String, Location> warps = new HashMap<>();
    public final HashMap<String, Location> back = new HashMap<>();
    public final HashMap<String, Location> shops = new HashMap<>();
    public final HashMap<String, Location> homes = new HashMap<>();
    public final HashMap<String, FormatInfo> uufi = new HashMap<>();
    public final HashMap<Player, PlayerTeleport> teleports = new HashMap<>();

}