package commands.player;

import center.Vars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class colors implements CommandExecutor
{

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            if (sender.hasPermission("eternia.colors"))
            {
                sender.sendMessage(Vars.getString("cores"));
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
