package commands.staff;

import center.nethertrapcheck;
import center.vars;
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
            if(player.hasPermission("eternia.setspawn"))
            {
                nethertrapcheck.file.set("world", (Objects.requireNonNull(player.getLocation().getWorld())).toString());
                nethertrapcheck.file.set("x", String.valueOf(player.getLocation().getX()));
                nethertrapcheck.file.set("y", String.valueOf(player.getLocation().getY()));
                nethertrapcheck.file.set("z", String.valueOf(player.getLocation().getZ()));
                nethertrapcheck.file.set("yaw", String.valueOf(player.getLocation().getYaw()));
                nethertrapcheck.file.set("pitch", String.valueOf(player.getLocation().getPitch()));
                player.sendMessage(vars.getString("spawn-definido"));
                return true;
            }
            else
            {
                player.sendMessage(vars.getString("sem-permissao"));
                return true;
            }
        }
        return false;
    }
}