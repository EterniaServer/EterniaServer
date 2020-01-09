package center;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import java.util.HashMap;

@SuppressWarnings("ConstantConditions")
public class vars
{
    private static final World world = Bukkit.getWorld(nethertrapcheck.file.getString("world"));
    private static final double x = Double.parseDouble(nethertrapcheck.file.getString("x"));
    private static final double y = Double.parseDouble(nethertrapcheck.file.getString("y"));
    private static final double z = Double.parseDouble(nethertrapcheck.file.getString("z"));
    private static final float yaw = Float.parseFloat(nethertrapcheck.file.getString("yaw"));
    private static final float pitch = Float.parseFloat(nethertrapcheck.file.getString("pitch"));
    public static final Location spawn = new Location(world, x, y, z, yaw, pitch);
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
