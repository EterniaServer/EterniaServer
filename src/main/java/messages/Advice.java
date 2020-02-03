package messages;

import center.Vars;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Advice implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender.hasPermission("eternia.advice"))
        {
            if (args.length >= 1)
            {
                StringBuilder sb = new StringBuilder();
                for (String arg : args)
                {
                    sb.append(arg).append(" ");
                }
                sb.substring(0, sb.length() - 1);
                String s = sb.toString();
                Bukkit.broadcastMessage(Vars.replaceMessage("aviso-global", s));
            }
            else
            {
                sender.sendMessage(Vars.getMessage("aviso"));
            }
        }
        else
        {
            sender.sendMessage(Vars.getMessage("sem-permissao"));
        }
        return true;
    }
}