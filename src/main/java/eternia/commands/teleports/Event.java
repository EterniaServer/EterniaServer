package eternia.commands.teleports;

import eternia.configs.MVar;
import eternia.configs.Vars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("ALL")
public class Event implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (sender.hasPermission("eternia.event")) {
                player.teleport(Vars.getLocation("world-e", "x-e", "y-e", "z-e", "yaw-e", "pitch-e"));
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