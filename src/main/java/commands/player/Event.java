package commands.player;

import center.NetherTrapCheck;
import center.Vars;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class Event implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            if (sender.hasPermission("eternia.event"))
            {
                World world = Bukkit.getWorld((String) Objects.requireNonNull(NetherTrapCheck.file.get("world-e")));
                Location spawn = new Location(world, NetherTrapCheck.file.getDouble("x-e"),
                        NetherTrapCheck.file.getDouble("y-e"), NetherTrapCheck.file.getDouble("z-e"),
                        Float.parseFloat(Objects.requireNonNull(NetherTrapCheck.file.getString("yaw-e"))),
                        Float.parseFloat(Objects.requireNonNull(NetherTrapCheck.file.getString("pitch-e"))));
                Player player = (Player) sender;
                player.teleport(spawn);
                sender.sendMessage(Vars.getString("evento"));
                return true;
            }
            else
            {
                sender.sendMessage(Vars.getString("sem-evento"));
                return true;
            }
        }
        return false;
    }
}