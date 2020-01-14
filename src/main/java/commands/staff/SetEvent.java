package commands.staff;

import center.Main;
import center.NetherTrapCheck;
import center.Vars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class SetEvent implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Main main = Main.getPlugin(Main.class);
            Player player = (Player) sender;
            if(player.hasPermission("eternia.setevent"))
            {
                NetherTrapCheck.file.set("world-e", Objects.requireNonNull(player.getLocation().getWorld()).getName());
                NetherTrapCheck.file.set("x-e", player.getLocation().getX());
                NetherTrapCheck.file.set("y-e", player.getLocation().getY());
                NetherTrapCheck.file.set("z-e", player.getLocation().getZ());
                NetherTrapCheck.file.set("yaw-e", player.getLocation().getYaw());
                NetherTrapCheck.file.set("pitch-e", player.getLocation().getPitch());
                main.saveConfig();
                player.sendMessage(Vars.getString("evento-definido"));
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