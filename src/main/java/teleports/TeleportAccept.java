package teleports;

import center.Main;
import center.Vars;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportAccept implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.tpa"))
            {
                if (Vars.tpa_requests.containsKey(player))
                {
                    Player target = Vars.tpa_requests.get(player);
                    if (player.hasPermission( "eternia.timing.bypass"))
                    {
                        target.teleport(player.getLocation());
                        Vars.playerReplaceMessage("teleportado-ate", target.getName(), player);
                        Vars.playerReplaceMessage("aceitou-tpa", player.getName(), target);
                        Vars.tpa_requests.remove(player);
                    }
                    else
                    {
                        int tempo = Vars.getInt("cooldown");
                        Vars.playerReplaceMessage("teleportando-em", tempo, player);
                        Vars.playerReplaceMessage("aceitou-tpa", player.getName(), target);
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getMain(), () ->
                        {
                            target.teleport(player.getLocation());
                            Vars.playerReplaceMessage("teleportado-ate", target.getName(), player);
                            Vars.tpa_requests.remove(player);
                        }, 20 * tempo);
                    }
                }
                else
                {
                    Vars.playerMessage("sem-pedido", player);
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
