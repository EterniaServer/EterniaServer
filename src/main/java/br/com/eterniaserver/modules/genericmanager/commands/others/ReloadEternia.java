package br.com.eterniaserver.modules.genericmanager.commands.others;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.methods.ConsoleMessage;
import br.com.eterniaserver.configs.methods.PlayerMessage;
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
                new PlayerMessage("server.reload", player);
            }
        } else {
            new Reload(plugin);
            new ConsoleMessage("server.reload");
        }
        return true;
    }
}
