package br.com.eterniaserver.eterniaserver;

import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.utils.CustomPlaceholder;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.utils.FormatInfo;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Vars extends JavaPlugin {

    public final Location error = new Location(Bukkit.getWorld("world"), 666, 666, 666, 666, 666);

    public final List<World> skippingWorlds = new ArrayList<>();
    public final List<String> god = new ArrayList<>();
    public final List<String> afk = new ArrayList<>();
    public final List<CustomPlaceholder> customPlaceholders = new ArrayList<>();

    public final List<String> playerBal = new ArrayList<>();
    public final List<String> playerExp = new ArrayList<>();
    public final List<String> playerHomes = new ArrayList<>();
    public final List<String> playerCooldown = new ArrayList<>();

    public final Map<String, Long> tpaTime = new HashMap<>();
    public final Map<String, Long> playerMuted = new HashMap<>();
    public final Map<String, Long> bedCooldown = new HashMap<>();
    public final Map<String, Long> afktime = new HashMap<>();
    public final Map<String, Double> balances = new HashMap<>();
    public final Map<String, String> tpaRequests = new HashMap<>();
    public final Map<String, String> kitsCooldown = new HashMap<>();
    public final Map<String, String> playerLogin = new HashMap<>();
    public final Map<String, String> tell = new HashMap<>();
    public final Map<Player, Boolean> spy = new HashMap<>();
    public final Map<String, Integer> global = new HashMap<>();
    public final Map<String, Integer> xp = new HashMap<>();
    public final Map<String, Integer> playersInPortal = new HashMap<>();
    public final Map<String, String[]> home = new HashMap<>();
    public final Map<String, Location> warps = new HashMap<>();
    public final Map<String, Location> back = new HashMap<>();
    public final Map<String, Location> shops = new HashMap<>();
    public final Map<String, Location> homes = new HashMap<>();
    public final Map<String, FormatInfo> uufi = new HashMap<>();
    public final Map<Player, PlayerTeleport> teleports = new HashMap<>();

}