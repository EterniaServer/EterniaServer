package teleports;

import center.Vars;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportToPlayer implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.tpa"))
            {
                if (args.length == 1)
                {
                    Player target = Bukkit.getPlayer(args[0]);
                    assert target != null;
                    if (target.isOnline())
                    {
                        if (!(target == player))
                        {
                            Vars.tpa_requests.put(target, player);
                            Vars.playerReplaceMessage("pediu-tpa", player.getName(), target);
                            Vars.playerReplaceMessage("enviou-tpa", target.getName(), player);
                        }
                        else
                        {
                            Vars.playerMessage("tpa-voce", player);
                        }
                    }
                    else
                    {
                        Vars.playerMessage("jogador-offline", player);
                    }
                }
                else
                {
                    Vars.playerMessage("tpa-errado", player);
                }
            }
            else
            {
                Vars.playerMessage("sem-permissao", player);
            }
        }
        else
        {
            Vars.consoleMessage("somente-jogador");
        }
        return true;
    }
}
