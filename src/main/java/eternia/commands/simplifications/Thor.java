package eternia.commands.simplifications;

import eternia.configs.MVar;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Thor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.thor")) {
                if (args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null && target.isOnline()) {
                        target.getWorld().strikeLightning(target.getLocation());
                        MVar.playerReplaceMessage("simp.thor-other", target.getName(), player);
                        MVar.playerReplaceMessage("simp.other-thor", player.getName(), target);
                    } else {
                        MVar.playerMessage("server.player-offline", player);
                    }
                } else {
                    player.getWorld().strikeLightning(player.getTargetBlock(null, 100).getLocation());
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
