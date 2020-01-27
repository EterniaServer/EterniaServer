package commands.player;

import center.NetherTrapCheck;
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
                World world = Bukkit.getWorld((String) Objects.requireNonNull(NetherTrapCheck.file.get("world-a")));
                Location spawn = new Location(world, NetherTrapCheck.file.getDouble("x-a"),
                        NetherTrapCheck.file.getDouble("y-a"), NetherTrapCheck.file.getDouble("z-a"),
                        Float.parseFloat(Objects.requireNonNull(NetherTrapCheck.file.getString("yaw-a"))),
                        Float.parseFloat(Objects.requireNonNull(NetherTrapCheck.file.getString("pitch-a"))));
                player.teleport(spawn);
                player.sendMessage(Vars.getString("arena"));
                return true;
            }
            else
            {
                player.sendMessage(Vars.getString("sem-permissao"));
                return true;
            }
        }
        else
        {
            Bukkit.getConsoleSender().sendMessage(Vars.getString("somente-jogador"));
            return true;
        }
    }
}