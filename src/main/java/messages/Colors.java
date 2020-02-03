package messages;

import center.Vars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Colors implements CommandExecutor
{

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            if (sender.hasPermission("eternia.colors"))
            {
                sender.sendMessage(Vars.getMessage("cores"));
            }
            else
            {
                sender.sendMessage(Vars.getMessage("sem-permissao"));
            }
            return true;
        }
        return false;
    }
}
