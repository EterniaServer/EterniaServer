package eternia.commands.experience;

import eternia.api.ExpAPI;
import eternia.configs.MVar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CheckLevel implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.checklevel")) {
                try {
                    MVar.playerReplaceMessage("tem-xp", ExpAPI.getExp(player.getUniqueId()), player);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                MVar.playerMessage("sem-permissão", player);
            }
        } else {
            MVar.consoleMessage("somente-jogador");
        }
        return true;
    }
}