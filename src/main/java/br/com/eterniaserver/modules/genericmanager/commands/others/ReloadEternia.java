package br.com.eterniaserver.modules.genericmanager.commands.others;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.modules.genericmanager.Reload;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadEternia implements CommandExecutor {

    private final EterniaServer plugin;

    public ReloadEternia(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.reload")) {
                new Reload(plugin);
                Messages.PlayerMessage("server.reload", player);
            }
        } else {
            new Reload(plugin);
            Messages.ConsoleMessage("server.reload");
        }
        return true;
    }
}
