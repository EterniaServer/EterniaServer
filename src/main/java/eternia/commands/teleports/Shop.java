package eternia.commands.teleports;

import eternia.EterniaServer;
import eternia.configs.MVar;
import eternia.configs.Vars;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Shop implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (sender.hasPermission("eternia.shop")) {
                if (sender.hasPermission("eternia.timing.bypass")) {
                    player.teleport(Vars.getLocation("world-s", "x-s", "y-s", "z-s", "yaw-s", "pitch-s"));
                    MVar.playerMessage("warps.shop", player);
                } else {
                    MVar.playerReplaceMessage("teleport.timing", Vars.cooldown, player);
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(EterniaServer.getMain(), () ->
                    {
                        player.teleport(Vars.getLocation("world-s", "x-s", "y-s", "z-s", "yaw-s", "pitch-s"));
                        MVar.playerMessage("warps.shop", player);
                    }, 20 * Vars.cooldown);
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