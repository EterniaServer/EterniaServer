package commands.staff;

import center.Main;
import center.NetherTrapCheck;
import center.Vars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class SetArena implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Main main = Main.getPlugin(Main.class);
            Player player = (Player) sender;
            if(player.hasPermission("eternia.setarena"))
            {
                NetherTrapCheck.file.set("world-a", Objects.requireNonNull(player.getLocation().getWorld()).getName());
                NetherTrapCheck.file.set("x-a", player.getLocation().getX());
                NetherTrapCheck.file.set("y-a", player.getLocation().getY());
                NetherTrapCheck.file.set("z-a", player.getLocation().getZ());
                NetherTrapCheck.file.set("yaw-a", player.getLocation().getYaw());
                NetherTrapCheck.file.set("pitch-a", player.getLocation().getPitch());
                main.saveConfig();
                player.sendMessage(Vars.getString("arena-definida"));
                return true;
            }
            else
            {
                player.sendMessage(Vars.getString("sem-permissao"));
                return true;
            }
        }
        return false;
    }
}