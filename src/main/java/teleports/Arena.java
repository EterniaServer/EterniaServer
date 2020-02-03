package teleports;

import center.Vars;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.Objects;

public class Arena implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        // Verifica se quem está executando o comando é um jogador
        // caso seja o comando é executado, caso não seja é enviado
        // uma mensagem ao console.
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            // Caso o jogador tenha permissão ele será enviado até a localização da arena.
            if (player.hasPermission("eternia.arena"))
            {
                World world = Bukkit.getWorld(Objects.requireNonNull(Vars.getString("world-a")));
                player.teleport(new Location(world, Vars.getDouble("x-a"),
                        Vars.getDouble("y-a"), Vars.getDouble("z-a"),
                        Float.parseFloat(Objects.requireNonNull(Vars.getString("yaw-a"))),
                        Float.parseFloat(Objects.requireNonNull(Vars.getString("pitch-a")))));
                player.sendMessage(Vars.getMessage("arena"));
                return true;
            }
            else
            {
                player.sendMessage(Vars.getMessage("sem-permissao"));
                return true;
            }
        }
        else
        {
            Bukkit.getConsoleSender().sendMessage(Vars.getMessage("somente-jogador"));
            return true;
        }
    }
}