package eternia.commands.experience;

import eternia.api.ExpAPI;
import eternia.configs.MVar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WithdrawLevel implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.withdrawlvl")) {
                try {
                    player.giveExp(ExpAPI.takeExp(player.getUniqueId()));
                } catch (Exception e) {
                    e.printStackTrace();
                    MVar.playerMessage("tirar-xp-errou", player);
                }
                MVar.playerReplaceMessage("xp-tirar", player.getLevel(), player);
            } else {
                MVar.playerMessage("sem-permissão", player);
            }
        } else {
            MVar.consoleMessage("somente-jogador");
        }
        return true;
    }
}