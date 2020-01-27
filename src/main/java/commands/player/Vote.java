package commands.player;

import center.NetherTrapCheck;
import center.Vars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Vote implements CommandExecutor
{

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            if (sender.hasPermission("eternia.vote"))
            {
                String sites_voto = NetherTrapCheck.file.getString("votar");
                assert sites_voto != null;
                String[] sites_lista = sites_voto.split("/split/");
                for (String s : sites_lista)
                {
                    sender.sendMessage(Vars.ChatColor(s));
                }
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
