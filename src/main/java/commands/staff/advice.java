package commands.staff;

import center.vars;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class advice implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.advice"))
            {
                if (args.length >= 1)
                {
                    StringBuilder sb = new StringBuilder();
                    for (String arg : args) {
                        sb.append(arg).append(" ");
                    }
                    sb.substring(0, sb.length() - 1);
                    String s = sb.toString();
                    Bukkit.broadcastMessage(vars.c(replaced(Objects.requireNonNull(center.looper.c.getString("aviso-global")), s)));
                    return true;
                }
                else
                {
                    player.sendMessage(vars.c(center.looper.c.getString("aviso")));
                    return true;
                }
            }
            else
            {
                player.sendMessage(vars.c(center.looper.c.getString("sem-permissao")));
                return true;
            }
        }
        return false;
    }
    private String replaced(String args, String valor)
    {
        return args.replace("%s", valor);
    }
}