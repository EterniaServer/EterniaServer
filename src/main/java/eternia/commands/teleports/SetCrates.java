package eternia.commands.teleports;

import eternia.EterniaServer;
import eternia.configs.CVar;
import eternia.configs.MVar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetCrates implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            EterniaServer main = EterniaServer.getPlugin(EterniaServer.class);
            Player player = (Player) sender;
            if (player.hasPermission("eternia.setcaixas")) {
                CVar.setWorld("world-c", player);
                CVar.setConfig("x-c", player.getLocation().getX());
                CVar.setConfig("y-c", player.getLocation().getY());
                CVar.setConfig("z-c", player.getLocation().getZ());
                CVar.setConfig("yaw-c", player.getLocation().getYaw());
                CVar.setConfig("pitch-c", player.getLocation().getPitch());
                main.saveConfig();
                MVar.playerMessage("caixa-definida", player);
            } else {
                MVar.playerMessage("sem-permissao", player);
            }
        } else {
            MVar.consoleMessage("somente-jogador");
        }
        return true;
    }
}