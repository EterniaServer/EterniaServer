package center;

import java.util.HashMap;

@SuppressWarnings("ConstantConditions")
public class Vars
{
    // Irá retornar uma mensagem já com cores e substituindo na
    // mensagem o argumento "%s" pelo nome do objeto indicado.
    public static String replaceObject(String message, Object valor)
    {
        String mensagem = NetherTrapCheck.file.getString(message);
        assert mensagem != null;
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', (mensagem.replace("%s", String.valueOf(valor))));
    }
    // Irá retornar uma mensagem já com cores e substituindo na
    // mensagem o argumento "%s" por uma string indicada.
    public static String replaceString(String message, String valor)
    {
        String mensagem = NetherTrapCheck.file.getString(message);
        assert mensagem != null;
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', (mensagem.replace("%s", valor)));
    }
    // Irá retornar uma mensagem já com cores.
    public static String getString(String message)
    {
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', (NetherTrapCheck.file.getString(message)));
    }
    // Serve para aplicar cores a uma string.
    public static String ChatColor(String message)
    {
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', message);
    }
    // Retorna um inteiro.
    public static int getInt(String valor)
    {
        return NetherTrapCheck.file.getInt(valor);
    }
    // Salva a localização de todos os jogadores que estão em um portal
    // do Nether para caso eles estejam presos.
    static final HashMap playersInPortal = new HashMap();
}
