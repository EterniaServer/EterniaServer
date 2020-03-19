package eternia.commands.teleports;

import eternia.configs.CVar;
import eternia.configs.MVar;
import org.bukkit.Location;
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
                player.teleport(new Location(CVar.getWorld("world-a"), CVar.getDouble("x-a"),
                        CVar.getDouble("y-a"), CVar.getDouble("z-a"),
                        CVar.getFloat("yaw-a"), CVar.getFloat("pitch-a")));
                MVar.playerMessage("arena", player);
            } else {
                MVar.playerMessage("sem-permissao", player);
            }
        } else {
            MVar.consoleMessage("somente-jogador");
        }
        return true;
    }
}