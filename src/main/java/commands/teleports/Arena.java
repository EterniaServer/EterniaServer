package commands.teleports;

import center.Vars;
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
                player.teleport(new Location(Vars.getWorld("world-a"), Vars.getDouble("x-a"),
                        Vars.getDouble("y-a"), Vars.getDouble("z-a"),
                        Vars.getFloat("yaw-a"), Vars.getFloat("pitch-a")));
                Vars.playerMessage("arena", player);
            } else {
                Vars.playerMessage("sem-permissao", player);
            }
        } else {
            Vars.consoleMessage("somente-jogador");
        }
        return true;
    }
}