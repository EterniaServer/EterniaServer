package eternia.commands.teleports;

import eternia.EterniaServer;
import eternia.configs.CVar;
import eternia.configs.MVar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawn implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            EterniaServer main = EterniaServer.getPlugin(EterniaServer.class);
            Player player = (Player) sender;
            if (player.hasPermission("eternia.setspawn")) {
                CVar.setWorld("world", player);
                CVar.setConfig("x", player.getLocation().getX());
                CVar.setConfig("y", player.getLocation().getY());
                CVar.setConfig("z", player.getLocation().getZ());
                CVar.setConfig("yaw", player.getLocation().getYaw());
                CVar.setConfig("pitch", player.getLocation().getPitch());
                main.saveConfig();
                MVar.playerMessage("spawn-definido", player);
            } else {
                MVar.playerMessage("sem-permissao", player);
            }
        } else {
            MVar.consoleMessage("somente-jogador");
        }
        return true;
    }
}