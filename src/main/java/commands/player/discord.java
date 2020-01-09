package commands.player;

import center.vars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class discord implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            if (sender.hasPermission("eternia.discord"))
            {
                sender.sendMessage(vars.getString("discord"));
                return true;
            }
            else
            {
                sender.sendMessage(vars.getString("sem-permissao"));
                return true;
            }
        }
        return false;
    }
}