package teleports;

import center.Main;
import center.Vars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class SetEvent implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Main main = Main.getPlugin(Main.class);
            Player player = (Player) sender;
            if (player.hasPermission("eternia.setevent"))
            {
                Vars.file.set("world-e", Objects.requireNonNull(player.getLocation().getWorld()).getName());
                Vars.file.set("x-e", player.getLocation().getX());
                Vars.file.set("y-e", player.getLocation().getY());
                Vars.file.set("z-e", player.getLocation().getZ());
                Vars.file.set("yaw-e", player.getLocation().getYaw());
                Vars.file.set("pitch-e", player.getLocation().getPitch());
                main.saveConfig();
                Vars.playerMessage("evento-definido", player);
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