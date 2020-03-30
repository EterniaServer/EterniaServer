package eternia.commands.teleports;

import eternia.api.WarpAPI;
import eternia.configs.MVar;
import eternia.configs.Vars;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Warp implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                if (player.hasPermission("eternia.warp." + args[0].toLowerCase())) {
                    final Location location = WarpAPI.getWarp(args[0].toLowerCase());
                    if (location != Vars.error) {
                        player.teleport(location);
                        MVar.playerReplaceMessage("warps.warp", args[0], player);
                    } else {
                        MVar.playerReplaceMessage("warps.noexist", args[0], player);
                    }
                } else {
                    MVar.playerMessage("server.no-perm", player);
                }
            } else {
                MVar.playerMessage("warps.use", player);
            }
        } else {
            MVar.consoleMessage("server.only-player");
        }
        return true;
    }
}
