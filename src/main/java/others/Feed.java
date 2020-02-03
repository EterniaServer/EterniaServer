package others;

import center.Vars;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Feed implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            if(player.hasPermission("eternia.feed"))
            {
                if (args.length == 0)
                {
                    if (command.getName().equalsIgnoreCase("comandos.staff.feed"))
                    {
                        player.setFoodLevel(20);
                        Vars.playerMessage("me-enchi", player);
                    }
                }
                else if (args.length == 1)
                {
                    if (player.hasPermission("eternia.feed.other"))
                    {
                        String targetS = args[0];
                        Player target = Bukkit.getPlayer(targetS);
                        assert target != null;
                        if (target.isOnline())
                        {
                            target.setFoodLevel(20);
                            Vars.playerReplaceMessage("encheu-barra", target.getName(), player);
                            Vars.playerReplaceMessage("recebeu-barra", player.getName(), target);
                        }
                        else
                        {
                            Vars.playerMessage("jogador-offline", player);
                        }
                    }
                    else
                    {
                        Vars.playerMessage("sem-permissao", player);
                    }
                }
            }
            else
            {
                Vars.playerMessage("sem-permissao", player);
            }
        }
        else if (args.length == 1)
        {
            String targetS = args[0];
            Player target = Bukkit.getPlayer(targetS);
            assert target != null;
            if (target.isOnline())
            {
                target.setFoodLevel(20);
                Vars.consoleReplaceMessage("encheu-barra", target.getName());
                Vars.playerReplaceMessage("recebeu-barra", "console", target);
            }
            else
            {
                Vars.consoleMessage("jogador-offline");
            }
        }
        return true;
    }
}