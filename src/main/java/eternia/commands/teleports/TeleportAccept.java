package eternia.commands.teleports;

import eternia.EterniaServer;
import eternia.configs.MVar;
import eternia.configs.Vars;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportAccept implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.tpa")) {
                if (Vars.tpa_requests.containsKey(player)) {
                    Player target = Vars.tpa_requests.get(player);
                    if (target.hasPermission("eternia.timing.bypass")) {
                        target.teleport(player.getLocation());
                        MVar.playerReplaceMessage("teleport.tpto", player.getName(), target);
                        MVar.playerReplaceMessage("teleport.accept", player.getName(), target);
                        Vars.tpa_requests.remove(player);
                    } else {
                        MVar.playerReplaceMessage("teleport.timing", Vars.cooldown, target);
                        MVar.playerReplaceMessage("teleport.accept", player.getName(), target);
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(EterniaServer.getMain(), () ->
                        {
                            target.teleport(player.getLocation());
                            MVar.playerReplaceMessage("teleport.tpto", player.getName(), target);
                            Vars.tpa_requests.remove(player);
                        }, 20 * Vars.cooldown);
                    }
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
