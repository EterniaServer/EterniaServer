package commands.player;

import center.Vars;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class suicide implements CommandExecutor
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
                    sb.append("- ").append(player.getName()).append(" ");
                    String s = sb.toString();
                    player.setHealth(0);
                    Bukkit.broadcastMessage(Vars.replaceString("suicidio", s));
                    return true;
                }
                else
                {
                    player.setHealth(0);
                    return true;
                }
            }
            else
            {
                player.sendMessage(Vars.getString("sem-permissao"));
                return true;
            }
        }
        return false;
    }
}