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
            World world = Bukkit.getWorld(vars.c(center.looper.c.getString("world")));
            double x = Double.parseDouble(vars.c(center.looper.c.getString("x")));
            double y = Double.parseDouble(vars.c(center.looper.c.getString("y")));
            double z = Double.parseDouble(vars.c(center.looper.c.getString("z")));
            float yaw = Float.parseFloat(vars.c(center.looper.c.getString("yaw")));
            float pitch = Float.parseFloat(vars.c(center.looper.c.getString("pitch")));
            Player player = (Player) sender;
            if (args.length == 0)
            {
                if (player.hasPermission("eternia.spawn"))
                {
                    if (player.hasPermission("eternia.timing.bypass"))
                    {
                        player.teleport(new Location(world, x, y, z, yaw, pitch));
                        player.sendMessage(vars.c(center.looper.c.getString("spawn")));
                        return true;
                    }
                    else
                    {
                        int tempo = center.looper.c.getInt("cooldown");
                        player.sendMessage(vars.c(replaced(Objects.requireNonNull(center.looper.c.getString("teleportando-em")), String.valueOf(tempo))));
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(center.main.getMain(), () ->
                        {
                            player.teleport(new Location(world, x, y, z, yaw, pitch));
                            player.sendMessage(vars.c(center.looper.c.getString("spawn")));
                        }, 20 * tempo);
                        return true;
                    }
                }
                else
                {
                    player.sendMessage(vars.c(center.looper.c.getString("sem-permissao")));
                    return true;
                }
            }
            else if (args.length == 1)
            {
                if (player.hasPermission("eternia.spawn.other"))
                {
                    String targetS = args[0];
                    Player target = Bukkit.getPlayer(targetS);
                    assert target != null;
                    if(target.isOnline())
                    {
                        target.teleport(new Location(world, x, y, z, yaw, pitch));
                        target.sendMessage(vars.c(center.looper.c.getString("spawn")));
                        player.sendMessage(vars.c(replaced(Objects.requireNonNull(center.looper.c.getString("teleportou-ele")), target.getName())));
                    }
                    else
                    {
                        player.sendMessage(vars.c(center.looper.c.getString("jogador-offline")));
                        return true;
                    }
                }
                else
                {
                    player.sendMessage(vars.c(center.looper.c.getString("sem-permissao")));
                    return true;
                }
            }
        }
        return false;
    }
    private String replaced(String args, String valor)
    {
        return args.replace("%s", valor);
    }
}
