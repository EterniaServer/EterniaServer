package commands.player;

import center.Vars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Donation implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            if (sender.hasPermission("eternia.donation"))
            {
                sender.sendMessage(Vars.getString("doacao"));
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