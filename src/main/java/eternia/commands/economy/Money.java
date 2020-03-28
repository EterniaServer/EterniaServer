package eternia.commands.economy;

import eternia.api.MoneyAPI;
import eternia.configs.MVar;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class Money implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                if (player.hasPermission("eternia.money")) {
                    double money = MoneyAPI.getMoney(player.getName());
                    MVar.playerReplaceMessage("eco.money", money, player);
                }
            } else if (args.length == 1) {
                if (player.hasPermission("eternia.money.other")) {
                    double money = MoneyAPI.getMoney(args[0]);
                    MVar.playerReplaceMessage("eco.money-other", money, player);
                } else {
                    MVar.playerMessage("server.no-perm", player);
                }
            }
        } else {
            if (args.length == 1) {
                double money = MoneyAPI.getMoney(args[0]);
                MVar.consoleReplaceMessage("eco.money-other", money);
            }
        }
        return true;
    }
}