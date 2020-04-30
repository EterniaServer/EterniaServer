package br.com.eterniaserver.modules.chatmanager.commands;

import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Vars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Spy implements CommandExecutor {

    private final Messages messages;
    private final Vars vars;

    public Spy(Messages messages, Vars vars) {
        this.messages = messages;
        this.vars = vars;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.spy")) {
                if (vars.spy.getOrDefault(player, false)) {
                    vars.spy.put(player, false);
                    messages.PlayerMessage("chat.spye", player);
                } else {
                    vars.spy.put(player, true);
                    messages.PlayerMessage("chat.spyd", player);
                }
            } else {
                messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            messages.ConsoleMessage("server.only-player");
        }
        return true;
    }

}
