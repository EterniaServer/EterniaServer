package economy;

import center.Main;
import center.Vars;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Pay implements CommandExecutor
{
    private final Main plugin = Main.getMain();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            if (args.length == 2)
            {
                Player target = Bukkit.getPlayer(args[0]);
                double money = Double.parseDouble(args[1]);
                assert target != null;
                if (target.isOnline())
                {
                    if (plugin.economyImplementer.has(player.getName(), money))
                    {
                        plugin.economyImplementer.depositPlayer(target, plugin.economyImplementer.getBalance(target) + money);
                        plugin.economyImplementer.withdrawPlayer(player, plugin.economyImplementer.getBalance(player) - money);
                        Vars.playerReplaceMessage("pay-tu", money, player);
                        Vars.playerReplaceMessage("pay-alvo", money, target);
                    }
                    else
                    {
                        Vars.playerMessage("pay-no", player);
                    }
                }
                else
                {
                    Vars.playerMessage("jogador-offline", player);
                }
            }
            else
            {
                Vars.playerMessage("pay-use", player);
            }
        }
        else
        {
            Vars.consoleMessage("somente-jogador");
        }
        return true;
    }
}
