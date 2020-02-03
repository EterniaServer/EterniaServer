package center;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Objects;

public class Vars
{
    public static FileConfiguration file;
    Vars (FileConfiguration file) { Vars.file = file; }
    public static void broadcastReplaceMessage(String message, Object valor)
    {
        String mensagem = getString(message);
        assert mensagem != null;
        Bukkit.broadcastMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', (mensagem.replace("%s", String.valueOf(valor)))));
    }
    public static void playerReplaceMessage(String message, Object valor, Player target)
    {
        String mensagem = getString(message);
        assert mensagem != null;
        target.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', (mensagem.replace("%s", String.valueOf(valor)))));
    }
    public static void consoleReplaceMessage(String message, Object valor)
    {
        String mensagem = getString(message);
        assert mensagem != null;
        Bukkit.getConsoleSender().sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', (mensagem.replace("%s", String.valueOf(valor)))));
    }
    public static void playerMessage(String message, Player target)
    {
        target.sendMessage(getMessage(message));
    }
    public static void consoleMessage(String message)
    {
        Bukkit.getConsoleSender().sendMessage(getMessage(message));
    }
    // Métodos
    public static String getMessage(String message) { return getColor(getString(message)); }
    public static String getColor(String message) { return org.bukkit.ChatColor.translateAlternateColorCodes('&', message); }
    public static boolean getBool(String valor) { return file.getBoolean(valor); }
    public static String getString(String valor) { return file.getString(valor); }
    public static double getDouble(String valor) { return file.getDouble(valor); }
    public static int getInt(String valor) { return file.getInt(valor); }
    // Variáveis
    public static final HashMap<Player, Integer> playersInPortal = new HashMap<>();
    public static final HashMap<Player, Location> back = new HashMap<>();
    public static final HashMap<Player, Long> cooldowns = new HashMap<>();
    public static final Location spawn = new Location(Bukkit.getWorld(Objects.requireNonNull(Vars.getString("world"))),
            Vars.getDouble("x"), Vars.getDouble("y"), Vars.getDouble("z"),
            Float.parseFloat(Objects.requireNonNull(Vars.getString("yaw"))),
            Float.parseFloat(Objects.requireNonNull(Vars.getString("pitch"))));
}
