package teleports;

import center.Main;
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
                Vars.file.set("world-c", Objects.requireNonNull(player.getLocation().getWorld()).getName());
                Vars.file.set("x-c", player.getLocation().getX());
                Vars.file.set("y-c", player.getLocation().getY());
                Vars.file.set("z-c", player.getLocation().getZ());
                Vars.file.set("yaw-c", player.getLocation().getYaw());
                Vars.file.set("pitch-c", player.getLocation().getPitch());
                main.saveConfig();
                player.sendMessage(Vars.getMessage("caixa-definida"));
                return true;
            }
            else
            {
                player.sendMessage(Vars.getMessage("sem-permissao"));
                return true;
            }
        }
        return false;
    }
}