package eternia.commands.teleports;

import eternia.configs.MVar;
import eternia.configs.Vars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetEvent implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.setevent")) {
                Vars.setLocation("world-e", "x-e", "y-e", "z-e", "yaw-e", "pitch-e", player);
                MVar.playerMessage("evento-definido", player);
            } else {
                MVar.playerMessage("sem-permissao", player);
            }
        } else {
            MVar.consoleMessage("somente-jogador");
        }
        return true;
    }
}