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
    // Irá retornar uma mensagem já com cores e substituindo na
    // mensagem o argumento "%s" pelo nome do objeto indicado.
    public static String replaceObject(String message, Object valor)
    {
        String mensagem = getString(message);
        assert mensagem != null;
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', (mensagem.replace("%s", String.valueOf(valor))));
    }
    // Irá retornar uma mensagem já com cores e substituindo na
    // mensagem o argumento "%s" por uma string indicada.
    public static String replaceMessage(String message, String valor)
    {
        String mensagem = getString(message);
        assert mensagem != null;
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', (mensagem.replace("%s", valor)));
    }
    // Irá retornar uma mensagem já com cores.
    public static String getMessage(String message)
    {
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', (getString(message)));
    }
    // Serve para aplicar cores a uma string.
    public static String getColor(String message)
    {
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', message);
    }
    // Métodos
    public static boolean getBool(String valor) { return file.getBoolean(valor); }
    public static String getString(String valor) { return file.getString(valor); }
    public static double getDouble(String valor) { return file.getDouble(valor); }
    public static int getInt(String valor) { return file.getInt(valor); }
    // Variáveis
    public static final HashMap<Player, Integer> playersInPortal = new HashMap<>();
    public static final HashMap<Player, Location> back = new HashMap<>();
    public static Location spawn = new Location(Bukkit.getWorld(Objects.requireNonNull(Vars.getString("world"))),
            Vars.getDouble("x"), Vars.getDouble("y"), Vars.getDouble("z"),
            Float.parseFloat(Objects.requireNonNull(Vars.getString("yaw"))),
            Float.parseFloat(Objects.requireNonNull(Vars.getString("pitch"))));
}
