package eternia.configs;

import eternia.EterniaServer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import java.util.Objects;

public class CVar {
    public static World getWorld(String valor) {
        return Bukkit.getWorld(Objects.requireNonNull(EterniaServer.getMain().getConfig().getString(valor)));
    }

    public static boolean getBool(String valor) {
        return EterniaServer.getMain().getConfig().getBoolean(valor);
    }

    public static double getDouble(String valor) {
        return EterniaServer.getMain().getConfig().getDouble(valor);
    }

    public static Float getFloat(String valor) {
        return Float.parseFloat(Objects.requireNonNull(EterniaServer.getMain().getConfig().getString(valor)));
    }

    public static int getInt(String valor) {
        return EterniaServer.getMain().getConfig().getInt(valor);
    }

    public static void setWorld(String path, Player player) {
        EterniaServer.getMain().getConfig().set(path, Objects.requireNonNull(player.getLocation().getWorld()).getName());
    }

    public static void setConfig(String path, Object valor) {
        EterniaServer.getMain().getConfig().set(path, valor);
    }

}
