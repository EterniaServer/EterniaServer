package messages;

import center.Vars;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Suicide implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.suicide"))
            {
                if (args.length >= 1)
                {
                    StringBuilder sb = new StringBuilder();
                    for (String arg : args)
                    {
                        sb.append(arg).append(" ");
                    }
                    sb.append("&8- &3").append(player.getName());
                    String s = sb.toString();
                    player.setHealth(0);
                    Bukkit.broadcastMessage(Vars.replaceMessage("suicidio", s));
                }
                else
                {
                    player.setHealth(0);
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