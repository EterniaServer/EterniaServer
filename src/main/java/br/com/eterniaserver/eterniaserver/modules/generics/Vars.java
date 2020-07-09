package br.com.eterniaserver.eterniaserver.modules.generics;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class Vars {

    protected static final Map<String, Location> homes = new HashMap<>();
    protected static final Map<String, Location> shops = new HashMap<>();
    protected static final Map<String, Location> warps = new HashMap<>();
    protected static final Map<String, String[]> home = new HashMap<>();

    protected static final Map<String, Long> tpa_time = new HashMap<>();
    protected static final Map<String, String> tpa_requests = new HashMap<>();
    protected static final Map<String, Double> balances = new HashMap<>();
    protected static final Map<String, Integer> xp = new HashMap<>();
    protected static final Map<String, Long> afktime = new HashMap<>();

}
