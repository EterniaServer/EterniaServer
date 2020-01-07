package commands.player;

import center.vars;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class suicide implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(args.length >= 1)
        {
            if (sender instanceof Player)
            {
                Player player = (Player) sender;
                StringBuilder sb = new StringBuilder();
                for (String arg : args)
                {
                    sb.append(arg).append(" ");
                }
                sb.append("- ").append(player.getName()).append(" ");
                String s = sb.toString();
                player.setHealth(0);
                Bukkit.broadcastMessage(vars.c(replaced(Objects.requireNonNull(center.looper.c.getString("suicidio")), s)));
                return true;
            }
        } else {
            if (sender instanceof Player)
            {
                Player player = (Player) sender;
                player.setHealth(0);
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