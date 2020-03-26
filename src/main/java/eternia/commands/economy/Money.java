package eternia.commands.economy;

import eternia.api.MoneyAPI;
import eternia.configs.MVar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class Money implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                if (player.hasPermission("eternia.money")) {
                    double money = MoneyAPI.getMoney(player.getUniqueId());
                    MVar.playerReplaceMessage("money", money, player);
                }
            } else if (args.length == 1) {
                if (player.hasPermission("eternia.money.other")) {
                    final Player target = Bukkit.getPlayer(args[0]);
                    assert target != null;
                    double money = MoneyAPI.getMoney(target.getUniqueId());
                    MVar.playerReplaceMessage("money-tem", money, player);
                } else {
                    MVar.playerMessage("sem-permissao", player);
                }
            }
        } else {
            if (args.length == 1) {
                final Player target = Bukkit.getPlayer(args[0]);
                assert target != null;
                double money = MoneyAPI.getMoney(target.getUniqueId());
                MVar.consoleReplaceMessage("money-tem", money);
            }
        }
        return true;
    }
}