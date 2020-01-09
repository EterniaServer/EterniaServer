package commands.player;

import center.vars;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.*;

public class spawn implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.timing.bypass"))
            {
                World world = Bukkit.getWorld(vars.c(center.looper.c.getString("world")));
                double x = Double.parseDouble(vars.c(center.looper.c.getString("x")));
                double y = Double.parseDouble(vars.c(center.looper.c.getString("y")));
                double z = Double.parseDouble(vars.c(center.looper.c.getString("z")));
                float yaw = Float.parseFloat(vars.c(center.looper.c.getString("yaw")));
                float pitch = Float.parseFloat(vars.c(center.looper.c.getString("pitch")));
                player.teleport(new Location(world, x, y, z, yaw, pitch));
                player.sendMessage(vars.c(center.looper.c.getString("spawn")));
                return true;
            }
            else
            {
                int tempo = center.looper.c.getInt("cooldown");
                player.sendMessage(vars.c(replaced(Objects.requireNonNull(center.looper.c.getString("teleportando-em")), tempo)));
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(center.main.getMain(), () ->
                {
                    World world = Bukkit.getWorld(vars.c(center.looper.c.getString("world")));
                    double x = Double.parseDouble(vars.c(center.looper.c.getString("x")));
                    double y = Double.parseDouble(vars.c(center.looper.c.getString("y")));
                    double z = Double.parseDouble(vars.c(center.looper.c.getString("z")));
                    float yaw = Float.parseFloat(vars.c(center.looper.c.getString("yaw")));
                    float pitch = Float.parseFloat(vars.c(center.looper.c.getString("pitch")));
                    player.teleport(new Location(world, x, y, z, yaw, pitch));
                    player.sendMessage(vars.c(center.looper.c.getString("spawn")));
                }, 20 * tempo);
                return true;
            }
        }
        return false;
    }
    private String replaced(String args, double valor)
    {
        return args.replace("%s", String.valueOf(valor));
    }
}
