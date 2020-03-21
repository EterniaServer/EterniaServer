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

public class Spawn implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                if (player.hasPermission("eternia.spawn")) {
                    if (player.hasPermission("eternia.timing.bypass")) {
                        player.teleport(Vars.getLocation("world-n", "x-n", "y-n", "z-n", "yaw-n", "pitch-n"));
                        MVar.playerMessage("spawn", player);
                    } else {
                        int tempo = CVar.getInt("cooldown");
                        MVar.playerReplaceMessage("teleportando-em", tempo, player);
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(EterniaServer.getMain(), () ->
                        {
                            player.teleport(Vars.getLocation("world-n", "x-n", "y-n", "z-n", "yaw-n", "pitch-n"));
                            MVar.playerMessage("spawn", player);
                        }, 20 * tempo);
                    }
                } else {
                    MVar.playerMessage("sem-permissao", player);
                }
            } else if (args.length == 1) {
                if (player.hasPermission("eternia.spawn.other")) {
                    Player target = Vars.findPlayer(args[0]);
                    if (target.isOnline()) {
                        player.teleport(Vars.getLocation("world-n", "x-n", "y-n", "z-n", "yaw-n", "pitch-n"));
                        MVar.playerMessage("spawn", target);
                        MVar.playerReplaceMessage("teleportou-ele", target.getName(), player);
                    } else {
                        MVar.playerMessage("jogador-offline", player);
                    }
                } else {
                    MVar.playerMessage("sem-permissao", player);
                }
            }
        } else {
            MVar.consoleMessage("somente-jogador");
        }
        return true;
    }
}