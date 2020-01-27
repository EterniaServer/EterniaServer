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

public class Crates implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            if (sender.hasPermission("eternia.arena"))
            {
                World world = Bukkit.getWorld((String) Objects.requireNonNull(NetherTrapCheck.file.get("world-c")));
                Location spawn = new Location(world, NetherTrapCheck.file.getDouble("x-c"),
                        NetherTrapCheck.file.getDouble("y-c"), NetherTrapCheck.file.getDouble("z-c"),
                        Float.parseFloat(Objects.requireNonNull(NetherTrapCheck.file.getString("yaw-c"))),
                        Float.parseFloat(Objects.requireNonNull(NetherTrapCheck.file.getString("pitch-c"))));
                Player player = (Player) sender;
                player.teleport(spawn);
                sender.sendMessage(Vars.getString("caixa"));
                return true;
            }
            else
            {
                sender.sendMessage(Vars.getString("sem-permissao"));
                return true;
            }
        }
        return false;
    }
}