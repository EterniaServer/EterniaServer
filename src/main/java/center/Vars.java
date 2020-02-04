package center;

import events.NetherPortal;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Objects;

public class Vars
{
    // MÉTODOS
    // Send
    public static void broadcastReplaceMessage(String message, Object valor)
    {
        String mensagem = getString(message);
        assert mensagem != null;
        Bukkit.broadcastMessage(getColor(mensagem.replace("%s", String.valueOf(valor))));
    }
    public static void playerReplaceMessage(String message, Object valor, Player target)
    {
        String mensagem = getString(message);
        assert mensagem != null;
        target.sendMessage(getColor(mensagem.replace("%s", String.valueOf(valor))));
    }
    public static void consoleReplaceMessage(String message, Object valor)
    {
        String mensagem = getString(message);
        assert mensagem != null;
        Bukkit.getConsoleSender().sendMessage(getColor(mensagem.replace("%s", String.valueOf(valor))));
    }
    public static void playerMessage(String message, Player target) { target.sendMessage(getMessage(message)); }
    public static void consoleMessage(String message) { Bukkit.getConsoleSender().sendMessage(getMessage(message)); }
    // Get
    public static String getMessage(String message) { return getColor(getString(message)); }
    public static String getColor(String message) { return org.bukkit.ChatColor.translateAlternateColorCodes('&', message); }
    public static World getWorld(String valor) { return Bukkit.getWorld(Objects.requireNonNull(Vars.getString(valor))); }
    public static boolean getBool(String valor) { return NetherPortal.file.getBoolean(valor); }
    public static String getString(String valor) { return NetherPortal.file.getString(valor); }
    public static double getDouble(String valor) { return NetherPortal.file.getDouble(valor); }
    public static Float getFloat(String valor) { return Float.parseFloat(Objects.requireNonNull(Vars.getString(valor))); }
    public static int getInt(String valor) { return NetherPortal.file.getInt(valor); }
    // Set
    public static void setWorld(String path, Player player) { NetherPortal.file.set(path, Objects.requireNonNull(player.getLocation().getWorld()).getName()); }
    public static void setConfig(String path, Object valor) { NetherPortal.file.set(path, valor); }
    // VARIÁVEIS
    public static final HashMap<Player, Integer> playersInPortal = new HashMap<>();
    public static final HashMap<Player, Location> back = new HashMap<>();
    public static final HashMap<Player, Long> shovel_cooldown = new HashMap<>();
    public static final HashMap<Player, Player> tpa_requests = new HashMap<>();
    public static final Location spawn = new Location(getWorld("world"), Vars.getDouble("x"),
            Vars.getDouble("y"), Vars.getDouble("z"),
            Vars.getFloat("yaw"), Vars.getFloat("pitch"));
}
