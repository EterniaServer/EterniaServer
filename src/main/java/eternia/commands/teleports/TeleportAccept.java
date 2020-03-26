package eternia.commands.teleports;

import eternia.EterniaServer;
import eternia.configs.CVar;
import eternia.configs.MVar;
import eternia.configs.Vars;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportAccept implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.tpa")) {
                if (Vars.tpa_requests.containsKey(player)) {
                    Player target = Vars.tpa_requests.get(player);
                    if (target.hasPermission("eternia.timing.bypass")) {
                        target.teleport(player.getLocation());
                        MVar.playerReplaceMessage("teleportado-ate", player.getName(), target);
                        MVar.playerReplaceMessage("aceitou-tpa", player.getName(), target);
                        Vars.tpa_requests.remove(player);
                    } else {
                        int tempo = CVar.getInt("cooldown");
                        MVar.playerReplaceMessage("teleportando-em", tempo, target);
                        MVar.playerReplaceMessage("aceitou-tpa", player.getName(), target);
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(EterniaServer.getMain(), () ->
                        {
                            target.teleport(player.getLocation());
                            MVar.playerReplaceMessage("teleportado-ate", player.getName(), target);
                            Vars.tpa_requests.remove(player);
                        }, 20 * tempo);
                    }
                } else {
                    MVar.playerMessage("sem-pedido", player);
                }
            } else {
                MVar.playerMessage("sem-permissao", player);
            }
        } else {
            MVar.consoleMessage("somente-jogador");
        }
        return true;
    }
}
