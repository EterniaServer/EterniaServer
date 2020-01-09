package commands.player;

import center.Vars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Rules implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            if (sender.hasPermission("eternia.rules"))
            {
                String regras = Vars.getString("regras");
                String[] regralista = regras.split("SPLIAG");
                for (int i = 0; i <= regralista.length; i++)
                {
                    sender.sendMessage(regralista[i]);
                }
                return true;
            }
            else
            {
                sender.sendMessage(Vars.getString("sem-permissao"));
            }
        }
        return false;
    }
}