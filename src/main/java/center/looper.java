package center;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Objects;

public class looper extends org.bukkit.scheduler.BukkitRunnable
{
    public static FileConfiguration c;
    looper(FileConfiguration c)
    {
        looper.c = c;
    }

    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    public void run()
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            synchronized (player)
            {
                if (player.getLocation().getBlock().getType() == Material.NETHER_PORTAL)
                {
                    if (!vars.playersInPortal.containsKey(player))
                    {
                        //noinspection unchecked
                        vars.playersInPortal.put(player, 7);
                    }
                    else if ((Integer) vars.playersInPortal.get(player) <= 1)
                    {
                        Location l = player.getLocation();
                        if (l.getBlock().getType() == Material.NETHER_PORTAL)
                        {
                            World world = Bukkit.getWorld(vars.c(c.getString("world")));
                            double x = Double.parseDouble(vars.c(c.getString("x")));
                            double y = Double.parseDouble(vars.c(c.getString("y")));
                            double z = Double.parseDouble(vars.c(c.getString("z")));
                            float yaw = Float.parseFloat(vars.c(c.getString("yaw")));
                            float pitch = Float.parseFloat(vars.c(c.getString("pitch")));
                            player.teleport(new Location(world, x, y, z, yaw, pitch));
                            player.sendMessage(vars.c(c.getString("spawn")));
                        }
                    }
                    else
                    {
                        //noinspection unchecked
                        vars.playersInPortal.put(player, (Integer) vars.playersInPortal.get(player) - 1);
                        if ((Integer) vars.playersInPortal.get(player) < 5)
                        {
                            player.sendMessage(vars.c(replaced(Objects.requireNonNull(c.getString("trap-mensagem-tempo")), player)));
                        }
                    }
                }
                else vars.playersInPortal.remove(player);
            }
        }
    }
    private String replaced(String msg, Player pl)
    {
        return msg.replace("%s", ((Integer) vars.playersInPortal.get(pl)).toString());
    }
}