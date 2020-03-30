package eternia.commands.teleports;

import eternia.api.ShopAPI;
import eternia.api.WarpAPI;
import eternia.configs.MVar;
import eternia.configs.Vars;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Shop implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                final Location location = WarpAPI.getWarp("shop");
                if (player.hasPermission("eternia.warp.shop")) {
                    if (location != Vars.error) {
                        player.teleport(location);
                        MVar.playerReplaceMessage("warps.warp", "Loja", player);
                    } else {
                        MVar.playerReplaceMessage("warps.noexist", "Loja", player);
                    }
                } else {
                    MVar.playerMessage("server.no-perm", player);
                }
            } else if (args.length == 1) {
                final Location location = ShopAPI.getShop(args[0].toLowerCase());
                if (player.hasPermission("eternia.shop.player")) {
                    if (location != Vars.error) {
                        player.teleport(location);
                        MVar.playerReplaceMessage("warps.shopp", args[0], player);
                    } else {
                        MVar.playerReplaceMessage("warps.shopno", "Loja", player);
                    }
                } else {
                    MVar.playerMessage("server.no-perm", player);
                }
            } else {
                MVar.playerMessage("warps.shopuse", player);
            }
        } else {
            MVar.consoleMessage("server.only-player");
        }
        return true;
    }
}