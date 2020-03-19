package eternia.commands.others;

import eternia.configs.MVar;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Name implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.name")) {
                if (args.length == 0) {
                    player.setDisplayName(player.getName());
                    MVar.playerMessage("nick-removido", player);
                } else {
                    player.setDisplayName(ChatColor.translateAlternateColorCodes('&', args[0]));
                    MVar.playerReplaceMessage("nick-novo", player.getDisplayName(), player);
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