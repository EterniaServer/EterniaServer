package teleports;

import center.Vars;
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
                player.teleport(new Location(Vars.getWorld("world-c"), Vars.getDouble("x-c"),
                        Vars.getDouble("y-c"), Vars.getDouble("z-c"),
                        Vars.getFloat("yaw-c"), Vars.getFloat("pitch-c")));
                Vars.playerMessage("caixa", player);
                return true;
            } else {
                Vars.playerMessage("sem-permissao", player);
            }
        } else {
            Vars.consoleMessage("somente-jogador");
        }
        return true;
    }
}