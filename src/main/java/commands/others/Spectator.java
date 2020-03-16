package commands.others;

import center.Vars;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Spectator implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.spectator")) {
                player.setGameMode(GameMode.SPECTATOR);
                Vars.playerMessage("escondido", player);
            } else {
                Vars.playerMessage("sem-permissao", player);
            }
        } else {
            Vars.consoleMessage("somente-jogador");
        }
        return true;
    }
}