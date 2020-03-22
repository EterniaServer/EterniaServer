package eternia.commands.others;

import eternia.configs.MVar;
import eternia.configs.Vars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("NullableProblems")
public class Feed implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.feed")) {
                if (args.length == 0) {
                    if (command.getName().equalsIgnoreCase("comandos.staff.feed")) {
                        player.setFoodLevel(20);
                        MVar.playerMessage("me-enchi", player);
                    }
                } else if (args.length == 1) {
                    if (player.hasPermission("eternia.feed.other")) {
                        Player target = Vars.findPlayer(args[0]);
                        if (target.isOnline()) {
                            target.setFoodLevel(20);
                            MVar.playerReplaceMessage("encheu-barra", target.getName(), player);
                            MVar.playerReplaceMessage("recebeu-barra", player.getName(), target);
                        } else {
                            MVar.playerMessage("jogador-offline", player);
                        }
                    } else {
                        MVar.playerMessage("sem-permissao", player);
                    }
                }
            } else {
                MVar.playerMessage("sem-permissao", player);
            }
        } else if (args.length == 1) {
            Player target = Vars.findPlayer(args[0]);
            if (target.isOnline()) {
                target.setFoodLevel(20);
                MVar.consoleReplaceMessage("encheu-barra", target.getName());
                MVar.playerReplaceMessage("recebeu-barra", "console", target);
            } else {
                MVar.consoleMessage("jogador-offline");
            }
        }
        return true;
    }
}