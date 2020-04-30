package br.com.eterniaserver.modules.genericmanager.commands.others;

import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.modules.genericmanager.Reload;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadEternia implements CommandExecutor {

    private final Messages messages;
    private final Reload reload;

    public ReloadEternia(Messages messages, Reload reload) {
        this.messages = messages;
        this.reload = reload;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.reload")) {
                reload.reload();
                messages.PlayerMessage("server.reload", player);
            }
        } else {
            reload.reload();
            messages.ConsoleMessage("server.reload");
        }
        return true;
    }
}
