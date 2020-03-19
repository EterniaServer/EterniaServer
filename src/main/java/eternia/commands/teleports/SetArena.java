package eternia.commands.teleports;

import eternia.EterniaServer;
import eternia.configs.CVar;
import eternia.configs.MVar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetArena implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            EterniaServer main = EterniaServer.getPlugin(EterniaServer.class);
            Player player = (Player) sender;
            if (player.hasPermission("eternia.setarena")) {
                CVar.setWorld("world-a", player);
                CVar.setConfig("x-a", player.getLocation().getX());
                CVar.setConfig("y-a", player.getLocation().getY());
                CVar.setConfig("z-a", player.getLocation().getZ());
                CVar.setConfig("yaw-a", player.getLocation().getYaw());
                CVar.setConfig("pitch-a", player.getLocation().getPitch());
                main.saveConfig();
                MVar.playerMessage("arena-definida", player);
                return true;
            } else {
                MVar.playerMessage("sem-permissao", player);
            }
        } else {
            MVar.consoleMessage("somente-jogador");
        }
        return true;
    }
}