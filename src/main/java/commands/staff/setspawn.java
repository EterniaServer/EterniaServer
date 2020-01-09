package commands.staff;

import center.vars;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class setspawn implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            if(player.hasPermission("eternia.comandos.staff.fly"))
            {
                center.looper.c.set("world", (Objects.requireNonNull(player.getLocation().getWorld())).toString());
                center.looper.c.set("x", String.valueOf(player.getLocation().getX()));
                center.looper.c.set("y", String.valueOf(player.getLocation().getY()));
                center.looper.c.set("z", String.valueOf(player.getLocation().getZ()));
                center.looper.c.set("yaw", String.valueOf(player.getLocation().getYaw()));
                center.looper.c.set("pitch", String.valueOf(player.getLocation().getPitch()));
                player.sendMessage(vars.c(center.looper.c.getString("spawn-definido")));
                return true;
            }
            else
            {
                player.sendMessage(vars.c(center.looper.c.getString("sem-permissao")));
                return true;
            }
        }
        return false;
    }
}