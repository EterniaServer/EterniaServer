package eternia.commands.teleports;

import eternia.EterniaServer;
import eternia.configs.MVar;
import eternia.configs.Vars;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Event implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (sender.hasPermission("eternia.event")) {
                if (sender.hasPermission("eternia.timing.bypass")) {
                    player.teleport(Vars.getLocation("world-e", "x-e", "y-e", "z-e", "yaw-e", "pitch-e"));
                    MVar.playerMessage("warps.event", player);
                } else {
                    MVar.playerReplaceMessage("teleport.timing", Vars.cooldown, player);
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(EterniaServer.getMain(), () ->
                    {
                        player.teleport(Vars.getLocation("world-e", "x-e", "y-e", "z-e", "yaw-e", "pitch-e"));
                        MVar.playerMessage("warps.event", player);
                    }, 20 * Vars.cooldown);
                }
            } else {
                MVar.playerMessage("warps.no-event", player);
            }
        } else {
            MVar.consoleMessage("server.only-player");
        }
        return true;
    }
}