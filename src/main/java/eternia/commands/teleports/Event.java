package eternia.commands.teleports;

import eternia.configs.CVar;
import eternia.configs.MVar;
import org.bukkit.Location;
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
                player.teleport(new Location(CVar.getWorld("world-e"), CVar.getDouble("x-e"),
                        CVar.getDouble("y-e"), CVar.getDouble("z-e"),
                        CVar.getFloat("yaw-e"), CVar.getFloat("pitch-e")));
                MVar.playerMessage("evento", player);
                return true;
            } else {
                MVar.playerMessage("sem-evento", player);
            }
        } else {
            MVar.consoleMessage("somente-jogador");
        }
        return true;
    }
}