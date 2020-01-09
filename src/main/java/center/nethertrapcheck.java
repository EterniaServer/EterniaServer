package center;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class nethertrapcheck extends org.bukkit.scheduler.BukkitRunnable
{
    public static FileConfiguration file;
    nethertrapcheck(FileConfiguration file)
    {
        nethertrapcheck.file = file;
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
                            player.teleport(vars.spawn);
                            player.sendMessage(vars.getString("spawn"));
                        }
                    }
                    else
                    {
                        //noinspection unchecked
                        vars.playersInPortal.put(player, (Integer) vars.playersInPortal.get(player) - 1);
                        if ((Integer) vars.playersInPortal.get(player) < 5)
                        {
                            player.sendMessage(vars.replaceObject("trap-mensagem-tempo", vars.playersInPortal.get(player)));
                        }
                    }
                }
                else vars.playersInPortal.remove(player);
            }
        }
    }
}