package center;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class Vars {
    private static final Main plugin = Main.getMain();

    // MÉTODOS
    private static boolean PlayerExist(UUID uuid) {
        try {
            String select_player = "SELECT * FROM eternia WHERE UUID = ?";
            PreparedStatement find_player = plugin.getConnection().prepareStatement(select_player);
            find_player.setString(1, uuid.toString());
            ResultSet player_list = find_player.executeQuery();
            while (player_list.next()) {
                if (player_list.getObject("UUID") != null) {
                    find_player.close();
                    player_list.close();
                    return true;
                }
            }
            find_player.close();
            player_list.close();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void CreatePlayer(final UUID uuid, Player player) {
        try {
            if (!PlayerExist(uuid)) {
                String create_player = "INSERT INTO eternia (UUID,NAME,XP,BALANCE) VALUES (?,?,?,?)";
                PreparedStatement save_player = plugin.getConnection().prepareStatement(create_player);
                save_player.setString(1, uuid.toString());
                save_player.setString(2, player.getName());
                save_player.setInt(3, 0);
                save_player.setDouble(4, 0.0);
                save_player.executeUpdate();
                save_player.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Send
    public static void broadcastReplaceMessage(String message, Object valor) {
        String mensagem;
        mensagem = getString(message);
        Bukkit.broadcastMessage(getColor(mensagem.replace("%s", String.valueOf(valor))));
    }

    public static void playerReplaceMessage(String message, Object valor, Player target) {
        String mensagem;
        mensagem = getString(message);
        target.sendMessage(getColor(mensagem.replace("%s", String.valueOf(valor))));
    }

    public static void consoleReplaceMessage(String message, Object valor) {
        String mensagem;
        mensagem = getString(message);
        Bukkit.getConsoleSender().sendMessage(getColor(mensagem.replace("%s", String.valueOf(valor))));
    }

    public static void playerMessage(String message, Player target) {
        target.sendMessage(getMessage(message));
    }

    public static void consoleMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(getMessage(message));
    }

    // Get
    public static String getMessage(String message) {
        return getColor(getString(message));
    }

    public static String getColor(String message) {
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String getString(String valor) {
        return Main.getMessages().getString(valor);
    }

    public static World getWorld(String valor) {
        return Bukkit.getWorld(Objects.requireNonNull(Main.getMain().getConfig().getString(valor)));
    }

    public static boolean getBool(String valor) {
        return Main.getMain().getConfig().getBoolean(valor);
    }

    public static double getDouble(String valor) {
        return Main.getMain().getConfig().getDouble(valor);
    }

    public static Float getFloat(String valor) {
        return Float.parseFloat(Objects.requireNonNull(Main.getMain().getConfig().getString(valor)));
    }

    public static int getInt(String valor) {
        return Main.getMain().getConfig().getInt(valor);
    }

    // Set
    public static void setWorld(String path, Player player) {
        Main.getMain().getConfig().set(path, Objects.requireNonNull(player.getLocation().getWorld()).getName());
    }

    public static void setConfig(String path, Object valor) {
        Main.getMain().getConfig().set(path, valor);
    }

    // VARIÁVEIS
    public static final HashMap<Player, Integer> playersInPortal = new HashMap<>();
    public static final HashMap<Player, Location> back = new HashMap<>();
    public static final HashMap<Player, Long> shovel_cooldown = new HashMap<>();
    public static final HashMap<Player, Player> tpa_requests = new HashMap<>();
    public static final Location spawn = new Location(getWorld("world"), Vars.getDouble("x"),
            Vars.getDouble("y"), Vars.getDouble("z"),
            Vars.getFloat("yaw"), Vars.getFloat("pitch"));
}
