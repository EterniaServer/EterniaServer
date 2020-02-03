package teleports;

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
            Player player = (Player) sender;
            if (sender.hasPermission("eternia.event"))
            {
                World world = Bukkit.getWorld(Objects.requireNonNull(Vars.getString("world-e")));
                player.teleport(new Location(world, Vars.getDouble("x-e"),
                        Vars.getDouble("y-e"), Vars.getDouble("z-e"),
                        Float.parseFloat(Objects.requireNonNull(Vars.getString("yaw-e"))),
                        Float.parseFloat(Objects.requireNonNull(Vars.getString("pitch-e")))));
                Vars.playerMessage("evento", player);
                return true;
            }
            else
            {
                Vars.playerMessage("sem-evento", player);
            }
        }
        else
        {
            Vars.consoleMessage("somente-jogador");
        }
        return true;
    }
}