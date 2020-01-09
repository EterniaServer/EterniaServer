package commands.player;

import center.Main;
import center.Vars;
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
                        player.teleport(Vars.spawn);
                        player.sendMessage(Vars.getString("spawn"));
                        return true;
                    }
                    else
                    {
                        int tempo = Vars.getInteiro("cooldown");
                        player.sendMessage(Vars.replaceObject("teleportando-em", tempo));
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getMain(), () ->
                        {
                            player.teleport(Vars.spawn);
                            player.sendMessage(Vars.getString("spawn"));
                        }, 20 * tempo);
                        return true;
                    }
                }
                else
                {
                    player.sendMessage(Vars.getString("sem-permissao"));
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
                        target.teleport(Vars.spawn);
                        target.sendMessage(Vars.getString("spawn"));
                        player.sendMessage(Vars.replaceString("teleportou-ele", target.getName()));
                    }
                    else
                    {
                        player.sendMessage(Vars.getString("jogador-offline"));
                        return true;
                    }
                }
                else
                {
                    player.sendMessage(Vars.getString("sem-permissao"));
                    return true;
                }
            }
        }
        return false;
    }
}
