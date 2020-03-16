package commands.others;

import center.Vars;
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
                    Vars.playerMessage("nick-removido", player);
                } else {
                    player.setDisplayName(ChatColor.translateAlternateColorCodes('&', args[0]));
                    Vars.playerReplaceMessage("nick-novo", player.getDisplayName(), player);
                }
            } else {
                Vars.playerMessage("sem-permissao", player);
            }
        } else {
            Vars.consoleMessage("somente-jogador");
        }
        return true;
    }
}