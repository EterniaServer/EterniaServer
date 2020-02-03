package teleports;

import center.Vars;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportAll implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.spectator"))
            {
                for (Player other : Bukkit.getOnlinePlayers())
                {
                    if (other != player)
                    {
                        other.teleport(player);
                    }
                }
                player.sendMessage(Vars.getMessage("teleportou-geral"));
            }
            else
            {
                player.sendMessage(Vars.getMessage("sem-permissao"));
            }
            return true;
        }
        return false;
    }
}