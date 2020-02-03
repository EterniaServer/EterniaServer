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

public class Crates implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            if (sender.hasPermission("eternia.arena"))
            {
                World world = Bukkit.getWorld(Objects.requireNonNull(Vars.getString("world-c")));
                player.teleport(new Location(world, Vars.getDouble("x-c"),
                        Vars.getDouble("y-c"), Vars.getDouble("z-c"),
                        Float.parseFloat(Objects.requireNonNull(Vars.getString("yaw-c"))),
                        Float.parseFloat(Objects.requireNonNull(Vars.getString("pitch-c")))));
                Vars.playerMessage("caixa", player);
                return true;
            }
            else
            {
                Vars.playerMessage("sem-permissao", player);
            }
        }
        else
        {
            Vars.consoleMessage("somente-jogador");
        }
        return true;
    }
}