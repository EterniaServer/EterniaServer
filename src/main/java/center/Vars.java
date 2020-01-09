package center;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import java.util.HashMap;

@SuppressWarnings("ConstantConditions")
public class Vars
{
    public static final Location spawn = new Location(Bukkit.getWorld(NetherTrapCheck.file.getString("world")),
            Double.parseDouble(NetherTrapCheck.file.getString("x")),
            Double.parseDouble(NetherTrapCheck.file.getString("y")),
            Double.parseDouble(NetherTrapCheck.file.getString("z")),
            Float.parseFloat(NetherTrapCheck.file.getString("yaw")),
            Float.parseFloat(NetherTrapCheck.file.getString("pitch")));

    public static String replaceObject(String message, Object valor)
    {
        String mensagem = NetherTrapCheck.file.getString(message);
        assert mensagem != null;
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', (mensagem.replace("%s", String.valueOf(valor))));
    }
    public static String replaceString(String message, String valor)
    {
        String mensagem = NetherTrapCheck.file.getString(message);
        assert mensagem != null;
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', (mensagem.replace("%s", valor)));
    }
    public static String getString(String message)
    {
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', (NetherTrapCheck.file.getString(message)));
    }
    public static int getInteiro(String valor)
    {
        return NetherTrapCheck.file.getInt(valor);
    }
    static final HashMap playersInPortal = new HashMap();
}
