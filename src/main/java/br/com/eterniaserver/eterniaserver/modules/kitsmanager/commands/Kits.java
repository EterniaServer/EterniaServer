package br.com.eterniaserver.eterniaserver.modules.kitsmanager.commands;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Strings;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Kits implements CommandExecutor {

    private final EterniaServer plugin;
    private final Messages messages;
    private final Strings strings;

    public Kits(EterniaServer plugin, Messages messages, Strings strings) {
        this.plugin = plugin;
        this.messages = messages;
        this.strings = strings;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.kits")) {
                messages.PlayerMessage("kits.kits", "%kits%", strings.getColor(plugin.kitConfig.getString("kits.nameofkits")), player);
            } else {
                messages.PlayerMessage("server.no-perm", player);
            }
        }
        return true;
    }
}
