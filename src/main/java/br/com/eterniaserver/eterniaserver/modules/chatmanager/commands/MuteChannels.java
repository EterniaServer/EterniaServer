package br.com.eterniaserver.eterniaserver.modules.chatmanager.commands;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MuteChannels implements CommandExecutor {

    private final EterniaServer plugin;
    private final Messages messages;

    public MuteChannels(EterniaServer plugin, Messages messages) {
        this.messages = messages;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.mute.channels")) {
                if (plugin.chatMuted) {
                    plugin.chatMuted = false;
                    messages.BroadcastMessage("chat.cm-d", "%player_name%", player.getName());
                } else {
                    plugin.chatMuted = true;
                    messages.BroadcastMessage("chat.cm-e", "%player_name%", player.getName());
                }
            } else {
                messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            if (plugin.chatMuted) {
                plugin.chatMuted = false;
                messages.BroadcastMessage("chat.cm-d", "%player_name%", "Console");
            } else {
                plugin.chatMuted = true;
                messages.BroadcastMessage("chat.cm-e", "%player_name%", "Console");
            }
        }
        return true;
    }

}
