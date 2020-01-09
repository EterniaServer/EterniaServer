package commands.player;

import center.vars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class rules implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            if (sender.hasPermission("eternia.rules"))
            {
                String[] regras = (vars.getString("regras")).split("SPLIAG");
                for (int i = 0; i <= regras.length; i++)
                {
                    sender.sendMessage(regras[i]);
                }
                return true;
            }
            else
            {
                sender.sendMessage(vars.getString("sem-permissao"));
            }
        }
        return false;
    }
}