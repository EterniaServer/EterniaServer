package center;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import java.util.HashMap;

@SuppressWarnings("ConstantConditions")
public class vars
{
    public static final Location spawn = new Location(Bukkit.getWorld(nethertrapcheck.file.getString("world")),
            Double.parseDouble(nethertrapcheck.file.getString("x")),
            Double.parseDouble(nethertrapcheck.file.getString("y")),
            Double.parseDouble(nethertrapcheck.file.getString("z")),
            Float.parseFloat(nethertrapcheck.file.getString("yaw")),
            Float.parseFloat(nethertrapcheck.file.getString("pitch")));
    public static String replaceObject(String message, Object valor)
    {
        String mensagem = nethertrapcheck.file.getString(message);
        assert mensagem != null;
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', (mensagem.replace("%s", String.valueOf(valor))));
    }
    public static String replaceString(String message, String valor)
    {
        String mensagem = nethertrapcheck.file.getString(message);
        assert mensagem != null;
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', (mensagem.replace("%s", valor)));
    }
    public static String getString(String message)
    {
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', (nethertrapcheck.file.getString(message)));
    }
    public static int getInteiro(String valor)
    {
        return nethertrapcheck.file.getInt(valor);
    }
    static final HashMap playersInPortal = new HashMap();
}
