package eternia.commands.teleports;

import eternia.configs.MVar;
import eternia.configs.Vars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportDeny implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.tpa")) {
                if (Vars.tpa_requests.containsKey(player)) {
                    Player target = Vars.tpa_requests.get(player);
                    MVar.playerReplaceMessage("teleport.auto-deny", target.getName(), player);
                    MVar.playerMessage("teleport.deny", target);
                    Vars.tpa_requests.remove(player);
                } else {
                    MVar.playerMessage("teleport.noask", player);
                }
            } else {
                MVar.playerMessage("server.no-perm", player);
            }
        } else {
            MVar.consoleMessage("server.only-player");
        }
        return true;
    }
}
