package commands.staff;

import center.vars;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class feed implements CommandExecutor
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
                        player.sendMessage(vars.getString("me-enchi"));
                        return true;
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
                            player.sendMessage(vars.replaceString("encheu-barra", target.getName()));
                            target.sendMessage(vars.replaceString("recebeu-barra", player.getName()));
                            return true;
                        }
                        else
                        {
                            player.sendMessage(vars.getString("jogador-offline"));
                            return true;
                        }
                    }
                    else
                    {
                        player.sendMessage(vars.getString("sem-permissao"));
                        return true;
                    }
                }
            }
            else
            {
                player.sendMessage(vars.getString("sem-permissao"));
                return true;
            }
        }
        return false;
    }
}