package center;

import java.util.HashMap;

@SuppressWarnings("ConstantConditions")
public class Vars
{
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
    public static String ChatColor(String message)
    {
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', message);
    }
    public static int getInt(String valor)
    {
        return NetherTrapCheck.file.getInt(valor);
    }
    static final HashMap playersInPortal = new HashMap();
}
