package others;

import center.Vars;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Name implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            if(player.hasPermission("eternia.name"))
            {
                if (args.length == 0)
                {
                    player.setDisplayName(player.getName());
                    player.sendMessage(Vars.getMessage("nick-removido"));
                }
                else
                {
                    player.setDisplayName(ChatColor.translateAlternateColorCodes('&', args[0]));
                    player.sendMessage(Vars.replaceMessage("nick-novo", player.getDisplayName()));
                }
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