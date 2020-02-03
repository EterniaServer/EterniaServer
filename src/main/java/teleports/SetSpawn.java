package teleports;

import center.Main;
import center.Vars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
                Vars.setWorld("world", player);
                Vars.setConfig("x", player.getLocation().getX());
                Vars.setConfig("y", player.getLocation().getY());
                Vars.setConfig("z", player.getLocation().getZ());
                Vars.setConfig("yaw", player.getLocation().getYaw());
                Vars.setConfig("pitch", player.getLocation().getPitch());
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