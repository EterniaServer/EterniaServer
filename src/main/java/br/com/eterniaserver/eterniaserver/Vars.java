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

class Vars extends JavaPlugin {

    // Placeholders
    public final String strPlayNamP = "%player_name%";
    public final String stringTimeP = "%time%";
    // Server
    public final String mChatStr = "modules.chat";
    public final String svChecks = "server.checks";
    public final String svCooldo = "server.cooldown";
    // Tables
    public final String tbHomeSt = "sql.table-home";
    // SQL
    public final String tbCreate = "CREATE TABLE IF NOT EXISTS ";
    public final String qInsertI = "INSERT INTO ";
    public final String qSeleAll = "SELECT * FROM ";
    public final String qUpdateS = "UPDATE ";
    public final String qWhePlNa = " WHERE player_name='";

    public final Location error = new Location(Bukkit.getWorld("world"), 666, 666, 666, 666, 666);

    public final List<World> skippingWorlds = new ArrayList<>();
    public final List<String> god = new ArrayList<>();
    public final List<String> afk = new ArrayList<>();
    public final List<CustomPlaceholder> customPlaceholders = new ArrayList<>();

    public final List<String> playerBal = new ArrayList<>();
    public final List<String> playerExp = new ArrayList<>();
    public final List<String> playerHomes = new ArrayList<>();
    public final List<String> playerCooldown = new ArrayList<>();

    public final HashMap<String, Long> tpaTime = new HashMap<>();
    public final HashMap<String, Long> playerMuted = new HashMap<>();
    public final HashMap<String, Long> bedCooldown = new HashMap<>();
    public final HashMap<String, Long> afktime = new HashMap<>();
    public final HashMap<String, Double> balances = new HashMap<>();
    public final HashMap<String, String> tpaRequests = new HashMap<>();
    public final HashMap<String, String> kitsCooldown = new HashMap<>();
    public final HashMap<String, String> playerLogin = new HashMap<>();
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