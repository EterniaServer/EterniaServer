package eternia.commands.teleports;

import eternia.configs.CVar;
import eternia.configs.MVar;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Crates implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (sender.hasPermission("eternia.arena")) {
                player.teleport(new Location(CVar.getWorld("world-c"), CVar.getDouble("x-c"),
                        CVar.getDouble("y-c"), CVar.getDouble("z-c"),
                        CVar.getFloat("yaw-c"), CVar.getFloat("pitch-c")));
                MVar.playerMessage("caixa", player);
                return true;
            } else {
                MVar.playerMessage("sem-permissao", player);
            }
        } else {
            MVar.consoleMessage("somente-jogador");
        }
        return true;
    }
}