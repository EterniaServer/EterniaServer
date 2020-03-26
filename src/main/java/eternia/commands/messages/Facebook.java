package eternia.commands.messages;

import eternia.configs.MVar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Facebook implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.facebook")) {
                MVar.playerMessage("facebook", player);
            } else {
                MVar.playerMessage("sem-permissao", player);
            }
        } else {
            MVar.consoleMessage("facebook");
        }
        return true;
    }
}