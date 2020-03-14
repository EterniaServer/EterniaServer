package economy;

import center.Main;
import center.Vars;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Eco implements CommandExecutor
{
    private final Main plugin = Main.getMain();
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            if (args.length == 3)
            {
                Player target = Bukkit.getPlayer(args[1]);
                assert target != null;
                double money = Double.parseDouble(args[2]);
                switch (args[0])
                {
                    case "give":
                    case "dar":
                        if (target.isOnline()) { plugin.economyImplementer.depositPlayer(target.getName(), plugin.economyImplementer.getBalance(target.getName()) + money); }
                        break;
                    case "remove":
                    case "tirar":
                        if (target.isOnline()) { plugin.economyImplementer.withdrawPlayer(target.getName(), plugin.economyImplementer.getBalance(target.getName()) - money); }
                        break;
                    case "set":
                    case "definir":
                        if (target.isOnline()) { plugin.economyImplementer.depositPlayer(target.getName(), money); }
                        break;
                    default:
                        Vars.consoleMessage("eco-use");
                        break;
                }
            }
            else
            {
                Vars.playerMessage("eco-use", player);
            }
        }
        else
        {
            if (args.length == 3)
            {
                Player target = Bukkit.getPlayer(args[1]);
                assert target != null;
                double money = Double.parseDouble(args[2]);
                switch (args[0])
                {
                    case "give":
                    case "dar":
                        if (target.isOnline()) { plugin.economyImplementer.depositPlayer(target.getName(), plugin.economyImplementer.getBalance(target.getName()) + money); }
                        break;
                    case "remove":
                    case "tirar":
                        if (target.isOnline()) { plugin.economyImplementer.withdrawPlayer(target.getName(), plugin.economyImplementer.getBalance(target.getName()) - money); }
                        break;
                    case "set":
                    case "definir":
                        if (target.isOnline()) { plugin.economyImplementer.depositPlayer(target.getName(), money); }
                        break;
                    default:
                        Vars.consoleMessage("eco-use");
                        break;
                }
            }
            else
            {
                Vars.consoleMessage("eco-use");
            }
        }
        return true;
    }
}
