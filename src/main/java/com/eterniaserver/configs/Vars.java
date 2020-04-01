package com.eterniaserver.configs;

import com.eterniaserver.EterniaServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class Vars {

    public static String getColor(String message) {
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String getString(String valor) {
        return EterniaServer.getMessages().getString(valor);
    }

    public static final Location error = new Location(Bukkit.getWorld("world"), 666, 666, 666, 666, 666);
    public static final int cooldown = CVar.getInt("server.cooldown");
    public static final List<String> god = new ArrayList<>();
    public static final HashMap<String, Location> warps = new HashMap<>();
    public static final HashMap<String, Integer> xp = new HashMap<>();
    public static final HashMap<String, Double> money = new HashMap<>();
    public static final HashMap<Player, Integer> playersInPortal = new HashMap<>();
    public static final HashMap<Player, Location> back = new HashMap<>();
    public static final HashMap<Player, Long> shovel_cooldown = new HashMap<>();
    public static final HashMap<Player, Player> tpa_requests = new HashMap<>();
}