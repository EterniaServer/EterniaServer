package center;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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
        for (Player p : Bukkit.getOnlinePlayers())
        {
            synchronized (p)
            {
                if (p.getLocation().getBlock().getType() == Material.NETHER_PORTAL)
                {
                    if (!vars.playersInPortal.containsKey(p))
                    {
                        //noinspection unchecked
                        vars.playersInPortal.put(p, 7);
                    }
                    else if ((Integer) vars.playersInPortal.get(p) <= 1)
                    {
                        Location l = p.getLocation();
                        if (l.getBlock().getType() == Material.NETHER_PORTAL)
                        {
                            l.getBlock().setType(Material.AIR);
                            p.sendMessage(vars.c(c.getString("trap-mensagem")));
                        }
                    }
                    else
                    {
                        //noinspection unchecked
                        vars.playersInPortal.put(p, (Integer) vars.playersInPortal.get(p) - 1);
                        if ((Integer) vars.playersInPortal.get(p) < 5)
                        {
                            p.sendMessage(vars.c(replaced(Objects.requireNonNull(c.getString("trap-mensagem-tempo")), p)));
                        }
                    }
                }
                else vars.playersInPortal.remove(p);
            }
        }
    }
    private String replaced(String msg, Player pl)
    {
        return msg.replace("%s", ((Integer) vars.playersInPortal.get(pl)).toString());
    }
}