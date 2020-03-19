package eternia.configs;

import eternia.EterniaServer;
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

    public static final HashMap<Player, Integer> playersInPortal = new HashMap<>();
    public static final HashMap<Player, Location> back = new HashMap<>();
    public static final HashMap<Player, Long> shovel_cooldown = new HashMap<>();
    public static final HashMap<Player, Player> tpa_requests = new HashMap<>();
    public static final Location spawn = new Location(CVar.getWorld("world"), CVar.getDouble("x"),
            CVar.getDouble("y"), CVar.getDouble("z"),
            CVar.getFloat("yaw"), CVar.getFloat("pitch"));
}