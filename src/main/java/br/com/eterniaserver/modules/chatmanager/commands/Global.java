package br.com.eterniaserver.modules.chatmanager.commands;

import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Vars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Global implements CommandExecutor {

    private final Messages messages;
    private final Vars vars;

    public Global(Messages messages, Vars vars) {
        this.messages = messages;
        this.vars = vars;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.chat.global")) {
                if (args.length == 0) {
                    vars.global.put(player.getName(), 1);
                    messages.PlayerMessage("chat.channelc", "%channel_name%", "Global", player);
                } else {
                    int o = vars.global.get(player.getName());
                    vars.global.put(player.getName(), 1);
                    StringBuilder sb = new StringBuilder();
                    for (String arg : args) {
                        sb.append(arg).append(" ");
                    }
                    sb.substring(0, sb.length() - 1);
                    player.chat(sb.toString());
                    vars.global.put(player.getName(), o);
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
