package commands.teleports;

import center.Main;
import center.Vars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetArena implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Main main = Main.getPlugin(Main.class);
            Player player = (Player) sender;
            if (player.hasPermission("eternia.setarena")) {
                Vars.setWorld("world-a", player);
                Vars.setConfig("x-a", player.getLocation().getX());
                Vars.setConfig("y-a", player.getLocation().getY());
                Vars.setConfig("z-a", player.getLocation().getZ());
                Vars.setConfig("yaw-a", player.getLocation().getYaw());
                Vars.setConfig("pitch-a", player.getLocation().getPitch());
                main.saveConfig();
                Vars.playerMessage("arena-definida", player);
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