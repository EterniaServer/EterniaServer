package br.com.eterniaserver.modules.genericmanager.commands.others;

import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.configs.methods.ConsoleMessage;
import br.com.eterniaserver.configs.methods.PlayerMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class God implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.god")) {
                if (Vars.god.contains(player.getName())) {
                    new PlayerMessage("simp.godd", player);
                    Vars.god.remove(player.getName());
                } else {
                    new PlayerMessage("simp.gode", player);
                    Vars.god.add(player.getName());
                }
            }
        } else {
            new ConsoleMessage("server.only-player");
        }
        return true;
    }
}
