package teleports;

import center.Main;
import center.Vars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetCrates implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Main main = Main.getPlugin(Main.class);
            Player player = (Player) sender;
            if (player.hasPermission("eternia.setcaixas")) {
                Vars.setWorld("world-c", player);
                Vars.setConfig("x-c", player.getLocation().getX());
                Vars.setConfig("y-c", player.getLocation().getY());
                Vars.setConfig("z-c", player.getLocation().getZ());
                Vars.setConfig("yaw-c", player.getLocation().getYaw());
                Vars.setConfig("pitch-c", player.getLocation().getPitch());
                main.saveConfig();
                Vars.playerMessage("caixa-definida", player);
            } else {
                Vars.playerMessage("sem-permissao", player);
            }
        } else {
            Vars.consoleMessage("somente-jogador");
        }
        return true;
    }
}