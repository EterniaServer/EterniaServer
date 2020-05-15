package br.com.eterniaserver.eterniaserver.modules.genericmanager.commands.others;

import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Vars;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class God implements CommandExecutor {

    private final Messages messages;
    private final Vars vars;

    public God(Messages messages, Vars vars) {
        this.messages = messages;
        this.vars = vars;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.god")) {
                if (vars.god.contains(player.getName())) {
                    messages.PlayerMessage("simp.godd", player);
                    vars.god.remove(player.getName());
                } else {
                    messages.PlayerMessage("simp.gode", player);
                    vars.god.add(player.getName());
                }
            }
        } else {
            messages.sendConsole("server.only-player");
        }
        return true;
    }
}
