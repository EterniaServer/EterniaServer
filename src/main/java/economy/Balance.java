package economy;

import center.Main;
import center.Vars;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Balance implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            final Main plugin = Main.getMain();
            Player player = (Player) sender;
            if (args.length == 0)
            {
                Vars.playerReplaceMessage("balance", plugin.economyImplementer.getBalance(player.getName()), player);
            }
            else
            {
                Player target = Bukkit.getPlayer(args[0]);
                assert target != null;
                if (target.isOnline())
                {
                    Vars.playerReplaceMessage("balance", plugin.economyImplementer.getBalance(target.getName()), player);
                }
                else
                {
                    Vars.playerMessage("jogador-offline", player);
                }
            }
        }
        else
        {
            Vars.consoleMessage("somente-jogador");
        }
        return true;
    }
}
