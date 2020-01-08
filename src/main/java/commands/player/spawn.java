package commands.player;

import center.main;
import center.vars;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Objects;

public class spawn implements CommandExecutor
{
    private HashMap<String, Integer> cd = new HashMap<>();
    private HashMap<String, Location> e = new HashMap<>();
    private Player player;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            player = (Player) sender;
            if(player.hasPermission("eternia.timing.bypass"))
            {
                World world = Bukkit.getWorld(vars.c(center.looper.c.getString("world")));
                double x = Double.parseDouble(vars.c(center.looper.c.getString("x")));
                double y = Double.parseDouble(vars.c(center.looper.c.getString("y")));
                double z = Double.parseDouble(vars.c(center.looper.c.getString("z")));
                float yaw = Float.parseFloat(vars.c(center.looper.c.getString("yaw")));
                float pitch = Float.parseFloat(vars.c(center.looper.c.getString("pitch")));
                player.teleport(new Location(world, x, y, z, yaw, pitch));
                player.sendMessage(vars.c(center.looper.c.getString("spawn")));
            }
            else
            {
                e.put(player.getName(), player.getLocation());
                cd.put(player.getName(), 4);
                doCountDown();
            }
            return true;
        }
        return false;
    }
    private void doCountDown()
    {
        player.sendMessage(vars.c(replaced(Objects.requireNonNull(center.looper.c.getString("teleporte-falta")), cd.get(player.getName()))));
        if(cd.get(player.getName()) > 0)
        {
            if (player.getLocation().getX() == e.get(player.getName()).getX() && player.getLocation().getZ() == e.get(player.getName()).getZ())
            {
                cd.put(player.getName(), cd.get(player.getName()) - 1);
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        doCountDown();
                    }
                }.runTaskLater(main.getPlugin(main.class), 20);
            }
            else
            {
                player.sendMessage(vars.c(center.looper.c.getString("nao-se-mova")));
                cd.put(player.getName(), 4);
            }
        }
        else
        {
            World world = Bukkit.getWorld(vars.c(center.looper.c.getString("world")));
            double x = Double.parseDouble(vars.c(center.looper.c.getString("x")));
            double y = Double.parseDouble(vars.c(center.looper.c.getString("y")));
            double z = Double.parseDouble(vars.c(center.looper.c.getString("z")));
            float yaw = Float.parseFloat(vars.c(center.looper.c.getString("yaw")));
            float pitch = Float.parseFloat(vars.c(center.looper.c.getString("pitch")));
            player.teleport(new Location(world, x, y, z, yaw, pitch));
            player.sendMessage(vars.c(center.looper.c.getString("spawn")));
            cd.put(player.getName(), 4);
        }
    }
    private String replaced(String args, double valor)
    {
        return args.replace("%s", String.valueOf(valor));
    }
}
