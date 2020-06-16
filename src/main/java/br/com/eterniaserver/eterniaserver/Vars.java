package br.com.eterniaserver.eterniaserver;

import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.utils.CustomPlaceholder;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.utils.FormatInfo;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

interface Vars {

    Location error = new Location(Bukkit.getWorld("world"), 666, 666, 666, 666, 666);

    List<World> skipping_worlds = new ArrayList<>();
    List<String> god = new ArrayList<>();
    List<String> afk = new ArrayList<>();
    List<CustomPlaceholder> customPlaceholders = new ArrayList<>();

    List<String> player_bal = new ArrayList<>();
    List<String> player_exp = new ArrayList<>();
    List<String> player_homes = new ArrayList<>();
    List<String> player_cooldown = new ArrayList<>();

    HashMap<String, Long> tpa_time = new HashMap<>();
    HashMap<String, Long> player_muted = new HashMap<>();
    HashMap<String, Long> bed_cooldown = new HashMap<>();
    HashMap<String, Long> afktime = new HashMap<>();
    HashMap<String, Double> balances = new HashMap<>();
    HashMap<String, String> tpa_requests = new HashMap<>();
    HashMap<String, String> kits_cooldown = new HashMap<>();
    HashMap<String, String> player_login = new HashMap<>();
    HashMap<String, String> tell = new HashMap<>();
    HashMap<Player, Boolean> spy = new HashMap<>();
    HashMap<String, Integer> global = new HashMap<>();
    HashMap<String, Integer> xp = new HashMap<>();
    HashMap<String, Integer> playersInPortal = new HashMap<>();
    HashMap<String, String[]> home = new HashMap<>();
    HashMap<String, Location> warps = new HashMap<>();
    HashMap<String, Location> back = new HashMap<>();
    HashMap<String, Location> shops = new HashMap<>();
    HashMap<String, Location> homes = new HashMap<>();
    HashMap<String, FormatInfo> uufi = new HashMap<>();
    HashMap<Player, PlayerTeleport> teleports = new HashMap<>();

}