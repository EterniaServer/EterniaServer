package br.com.eterniaserver.eterniaserver;

import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.utils.CustomPlaceholder;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.utils.FormatInfo;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

interface Vars {

    List<World> skipping_worlds = new ArrayList<>();
    List<String> god = new ArrayList<>();
    List<String> afk = new ArrayList<>();
    List<CustomPlaceholder> customPlaceholders = new ArrayList<>();

    List<String> player_bal = new ArrayList<>();
    List<String> player_exp = new ArrayList<>();
    List<String> player_homes = new ArrayList<>();
    List<String> player_cooldown = new ArrayList<>();

    Map<String, Long> tpa_time = new HashMap<>();
    Map<String, Long> player_muted = new HashMap<>();
    Map<String, Long> bed_cooldown = new HashMap<>();
    Map<String, Long> afktime = new HashMap<>();
    Map<String, Double> balances = new HashMap<>();
    Map<String, String> tpa_requests = new HashMap<>();
    Map<String, String> kits_cooldown = new HashMap<>();
    Map<String, String> player_login = new HashMap<>();
    Map<String, String> tell = new HashMap<>();
    Map<Player, Boolean> spy = new HashMap<>();
    Map<String, Integer> global = new HashMap<>();
    Map<String, Integer> xp = new HashMap<>();
    Map<String, Integer> playersInPortal = new HashMap<>();
    Map<String, Location> back = new HashMap<>();
    Map<String, FormatInfo> uufi = new HashMap<>();
    Map<Player, PlayerTeleport> teleports = new HashMap<>();

}