package br.com.eterniaserver.modules.genericmanager.commands.others;

import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Vars;
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
                    Messages.PlayerMessage("simp.godd", player);
                    Vars.god.remove(player.getName());
                } else {
                    Messages.PlayerMessage("simp.gode", player);
                    Vars.god.add(player.getName());
                }
            }
        } else {
            Messages.ConsoleMessage("server.only-player");
        }
        return true;
    }
}
