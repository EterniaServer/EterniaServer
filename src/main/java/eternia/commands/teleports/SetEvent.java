package eternia.commands.teleports;

import eternia.EterniaServer;
import eternia.configs.CVar;
import eternia.configs.MVar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetEvent implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            EterniaServer main = EterniaServer.getPlugin(EterniaServer.class);
            Player player = (Player) sender;
            if (player.hasPermission("eternia.setevent")) {
                CVar.setWorld("world-e", player);
                CVar.setConfig("x-e", player.getLocation().getX());
                CVar.setConfig("y-e", player.getLocation().getY());
                CVar.setConfig("z-e", player.getLocation().getZ());
                CVar.setConfig("yaw-e", player.getLocation().getYaw());
                CVar.setConfig("pitch-e", player.getLocation().getPitch());
                main.saveConfig();
                MVar.playerMessage("evento-definido", player);
            } else {
                MVar.playerMessage("sem-permissao", player);
            }
        } else {
            MVar.consoleMessage("somente-jogador");
        }
        return true;
    }
}