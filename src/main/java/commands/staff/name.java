package commands.staff;

import center.vars;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class name implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            if(player.hasPermission("eternia.comandos.staff.nome"))
            {
                if (args.length == 0)
                {
                    player.setDisplayName(player.getName());
                    player.sendMessage(vars.c(center.looper.c.getString("nick-removido")));
                    return true;
                } else {
                    player.setDisplayName(ChatColor.translateAlternateColorCodes('&', args[0]));
                    player.sendMessage(vars.c(replaced(Objects.requireNonNull(center.looper.c.getString("nick-novo")), player.getDisplayName())));
                    return true;
                }
            } else {
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