package center;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class NetherTrapCheck extends org.bukkit.scheduler.BukkitRunnable
{
    public static FileConfiguration file;
    NetherTrapCheck(FileConfiguration file)
    {
        NetherTrapCheck.file = file;
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
                    if (!Vars.playersInPortal.containsKey(player))
                    {
                        //noinspection unchecked
                        Vars.playersInPortal.put(player, 7);
                    }
                    else if ((Integer) Vars.playersInPortal.get(player) <= 1)
                    {
                        Location l = player.getLocation();
                        if (l.getBlock().getType() == Material.NETHER_PORTAL)
                        {
                            player.teleport(Vars.spawn);
                            player.sendMessage(Vars.getString("spawn"));
                        }
                    }
                    else
                    {
                        //noinspection unchecked
                        Vars.playersInPortal.put(player, (Integer) Vars.playersInPortal.get(player) - 1);
                        if ((Integer) Vars.playersInPortal.get(player) < 5)
                        {
                            player.sendMessage(Vars.replaceObject("trap-mensagem-tempo", Vars.playersInPortal.get(player)));
                        }
                    }
                }
                else Vars.playersInPortal.remove(player);
            }
        }
    }
}