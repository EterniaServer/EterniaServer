package com.eterniaserver.configs;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

public class Vars {

    public static final Location error = new Location(Bukkit.getWorld("world"), 666, 666, 666, 666, 666);

    public static final ArrayList<String> god = new ArrayList<>();
    public static final ArrayList<World> skipping_worlds = new ArrayList<>();

    public static final HashMap<String, Long> bed_cooldown = new HashMap<>();
    public static final HashMap<Player, Long> afktime = new HashMap<>();
    public static final HashMap<Player, Long> shovel_cooldown = new HashMap<>();
    public static final HashMap<String, Double> money = new HashMap<>();
    public static final HashMap<String, String> tpa_requests = new HashMap<>();
    public static final HashMap<Player, Boolean> moved = new HashMap<>();
    public static final HashMap<String, Integer> xp = new HashMap<>();
    public static final HashMap<Player, Integer> playersInPortal = new HashMap<>();
    public static final HashMap<Player, Location> playerposition = new HashMap<>();
    public static final HashMap<String, Location> warps = new HashMap<>();
    public static final HashMap<String, Location> back = new HashMap<>();
}