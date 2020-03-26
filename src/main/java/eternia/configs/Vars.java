package eternia.configs;

import eternia.EterniaServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import java.util.*;

public class Vars {
    public static Player findPlayer(String targets) {
        Player target = Bukkit.getPlayer(targets);
        assert target != null;
        return target;
    }

    public static String getColor(String message) {
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String getString(String valor) {
        return EterniaServer.getMessages().getString(valor);
    }

    public static Location getLocation(String w, String x, String y, String z, String yaw, String pitch) {
        return (new Location(CVar.getWorld(w), CVar.getDouble(x), CVar.getDouble(y),
                CVar.getDouble(z), CVar.getFloat(yaw), CVar.getFloat(pitch)));
    }

    public static void setLocation(String w, String x, String y, String z, String yaw, String pitch, Player player) {
        CVar.setWorld(w, player);
        CVar.setConfig(x, player.getLocation().getX());
        CVar.setConfig(y, player.getLocation().getY());
        CVar.setConfig(z, player.getLocation().getZ());
        CVar.setConfig(yaw, player.getLocation().getYaw());
        CVar.setConfig(pitch, player.getLocation().getPitch());
        EterniaServer.getMain().saveConfig();
    }

    public static final HashMap<String, Integer> xp = new HashMap<>();
    public static final HashMap<String, Double> money = new HashMap<>();
    public static final HashMap<Player, Integer> playersInPortal = new HashMap<>();
    public static final HashMap<Player, Location> back = new HashMap<>();
    public static final HashMap<Player, Long> shovel_cooldown = new HashMap<>();
    public static final HashMap<Player, Player> tpa_requests = new HashMap<>();
}