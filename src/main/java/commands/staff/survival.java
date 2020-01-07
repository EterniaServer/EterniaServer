package commands.staff;

import center.vars;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class survival implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            if(player.hasPermission("eternia.comandos.staff.survival"))
            {
                player.setGameMode(GameMode.SURVIVAL);
                player.sendMessage(vars.c(center.looper.c.getString("jogando")));
            }
            else
            {
                player.sendMessage(vars.c(center.looper.c.getString("sem-permissao")));
                return true;
            }
        }
        return false;
    }
}