package commands.staff;

import center.Main;
import center.NetherTrapCheck;
import center.Vars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class SetCrates implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Main main = Main.getPlugin(Main.class);
            Player player = (Player) sender;
            if(player.hasPermission("eternia.setcaixas"))
            {
                NetherTrapCheck.file.set("world-c", Objects.requireNonNull(player.getLocation().getWorld()).getName());
                NetherTrapCheck.file.set("x-c", player.getLocation().getX());
                NetherTrapCheck.file.set("y-c", player.getLocation().getY());
                NetherTrapCheck.file.set("z-c", player.getLocation().getZ());
                NetherTrapCheck.file.set("yaw-c", player.getLocation().getYaw());
                NetherTrapCheck.file.set("pitch-c", player.getLocation().getPitch());
                main.saveConfig();
                player.sendMessage(Vars.getString("caixa-definida"));
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