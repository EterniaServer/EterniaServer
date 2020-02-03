package teleports;

import center.Main;
import center.Vars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class SetSpawn implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Main main = Main.getPlugin(Main.class);
            Player player = (Player) sender;
            if (player.hasPermission("eternia.setspawn"))
            {
                Vars.file.set("world", Objects.requireNonNull(player.getLocation().getWorld()).getName());
                Vars.file.set("x", player.getLocation().getX());
                Vars.file.set("y", player.getLocation().getY());
                Vars.file.set("z", player.getLocation().getZ());
                Vars.file.set("yaw", player.getLocation().getYaw());
                Vars.file.set("pitch", player.getLocation().getPitch());
                main.saveConfig();
                Vars.playerMessage("spawn-definido", player);
            }
            else
            {
                Vars.playerMessage("sem-permissao", player);
            }
        }
        else
        {
            Vars.consoleMessage("somente-jogador");
        }
        return true;
    }
}