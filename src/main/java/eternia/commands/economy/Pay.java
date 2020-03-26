package eternia.commands.economy;

import eternia.api.MoneyAPI;
import eternia.configs.MVar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

public class Pay implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (sender.hasPermission("eternia.pay")) {
                if (args.length == 2) {
                    double coins;
                    try {
                        coins = Double.parseDouble(args[1]);
                        if (coins > -1) {
                            final Player target = Bukkit.getPlayer(args[0]);
                            if (target != null) {
                                if (target.equals(player)) {
                                    MVar.playerMessage("pay-voce", player);
                                    return true;
                                }
                                if (MoneyAPI.getMoney(player.getUniqueId()) >= coins) {
                                    MoneyAPI.addMoney(target.getUniqueId(), coins);
                                    MoneyAPI.removeMoney(player.getUniqueId(), coins);
                                    MVar.playerReplaceMessage("pay-pagou", coins, player);
                                    MVar.playerReplaceMessage("pay-recebeu", coins, target);
                                } else {
                                    MVar.playerMessage("pay-voce-nao-tem", player);
                                }
                            } else {
                                MVar.playerMessage("jogador-nao", player);
                            }
                        } else {
                            MVar.playerMessage("nao-pode-negativo", player);
                        }
                    } catch (Exception e) {
                        MVar.playerMessage("precisa-numero", player);
                        return true;
                    }
                } else {
                    MVar.playerMessage("pay-use", player);
                }
            } else {
                MVar.playerMessage("sem-permissao", player);
            }
        } else {
            MVar.consoleMessage("somente-jogador");
        }
        return true;
    }
}