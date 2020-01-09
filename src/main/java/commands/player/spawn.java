package commands.player;

import center.vars;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class spawn implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            if (args.length == 0)
            {
                if (player.hasPermission("eternia.spawn"))
                {
                    if (player.hasPermission("eternia.timing.bypass"))
                    {
                        player.teleport(vars.spawn);
                        player.sendMessage(vars.getString("spawn"));
                        return true;
                    }
                    else
                    {
                        int tempo = vars.getInteiro("cooldown");
                        player.sendMessage(vars.replaceObject("teleportando-em", tempo));
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(center.main.getMain(), () ->
                        {
                            player.teleport(vars.spawn);
                            player.sendMessage(vars.getString("spawn"));
                        }, 20 * tempo);
                        return true;
                    }
                }
                else
                {
                    player.sendMessage(vars.getString("sem-permissao"));
                    return true;
                }
            }
            else if (args.length == 1)
            {
                if (player.hasPermission("eternia.spawn.other"))
                {
                    String targetS = args[0];
                    Player target = Bukkit.getPlayer(targetS);
                    assert target != null;
                    if(target.isOnline())
                    {
                        target.teleport(vars.spawn);
                        target.sendMessage(vars.getString("spawn"));
                        player.sendMessage(vars.replaceString("teleportou-ele", target.getName()));
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
        return false;
    }
}
