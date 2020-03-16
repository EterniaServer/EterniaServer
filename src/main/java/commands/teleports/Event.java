package commands.teleports;

import center.Vars;
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
                player.teleport(new Location(Vars.getWorld("world-e"), Vars.getDouble("x-e"),
                        Vars.getDouble("y-e"), Vars.getDouble("z-e"),
                        Vars.getFloat("yaw-e"), Vars.getFloat("pitch-e")));
                Vars.playerMessage("evento", player);
                return true;
            } else {
                Vars.playerMessage("sem-evento", player);
            }
        } else {
            Vars.consoleMessage("somente-jogador");
        }
        return true;
    }
}