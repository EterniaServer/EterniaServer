package eternia.commands.teleports;

import eternia.EterniaServer;
import eternia.configs.MVar;
import eternia.configs.Vars;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Arena implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Verifica se quem está executando o comando é um jogador
        // caso seja o comando é executado, caso não seja é enviado
        // uma mensagem ao console.
        if (sender instanceof Player) {
            Player player = (Player) sender;
            // Caso o jogador tenha permissão ele será enviado até a localização da arena.
            if (player.hasPermission("eternia.arena")) {
                if (player.hasPermission("eternia.timing.bypass")) {
                    player.teleport(Vars.getLocation("world-a", "x-a", "y-a", "z-a", "yaw-a", "pitch-a"));
                    MVar.playerMessage("warps.arena", player);
                } else {
                    MVar.playerReplaceMessage("teleport.timing", Vars.cooldown, player);
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(EterniaServer.getMain(), () ->
                    {
                        player.teleport(Vars.getLocation("world-a", "x-a", "y-a", "z-a", "yaw-a", "pitch-a"));
                        MVar.playerMessage("warps.arena", player);
                    }, 20 * Vars.cooldown);
                }
            } else {
                MVar.playerMessage("server.no-perm", player);
            }
        } else {
            MVar.consoleMessage("server.only-player");
        }
        return true;
    }
}