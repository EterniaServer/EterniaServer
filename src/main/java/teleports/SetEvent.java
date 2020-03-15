package teleports;

import center.Main;
import center.Vars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetEvent implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Main main = Main.getPlugin(Main.class);
            Player player = (Player) sender;
            if (player.hasPermission("eternia.setevent")) {
                Vars.setWorld("world-e", player);
                Vars.setConfig("x-e", player.getLocation().getX());
                Vars.setConfig("y-e", player.getLocation().getY());
                Vars.setConfig("z-e", player.getLocation().getZ());
                Vars.setConfig("yaw-e", player.getLocation().getYaw());
                Vars.setConfig("pitch-e", player.getLocation().getPitch());
                main.saveConfig();
                Vars.playerMessage("evento-definido", player);
            } else {
                Vars.playerMessage("sem-permissao", player);
            }
        } else {
            Vars.consoleMessage("somente-jogador");
        }
        return true;
    }
}