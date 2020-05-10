package br.com.eterniaserver.eterniaserver.modules.chatmanager.commands;

import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Strings;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Nickname implements CommandExecutor {

    private final Messages messages;
    private final Strings strings;

    public Nickname(Messages messages, Strings strings) {
        this.messages = messages;
        this.strings = strings;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, java.lang.String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.nickname")) {
                if (args.length == 2) {
                    if (player.hasPermission("eternia.nickname.other")) {
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target != null && target.isOnline()) {
                            if (args[1].equals("clear")) {
                                target.setDisplayName(target.getName());
                                messages.PlayerMessage("chat.remove-nick", target);
                                messages.PlayerMessage("chat.remove-nick", player);
                            } else {
                                target.setDisplayName(strings.getColor(args[1]));
                                messages.PlayerMessage("chat.newnick", "%player_display_name%", strings.getColor(args[1]), player);
                                messages.PlayerMessage("chat.newnick", "%player_display_name%", strings.getColor(args[1]), target);
                            }
                        } else {
                            messages.PlayerMessage("server.player-offline", player);
                        }
                    } else {
                        messages.PlayerMessage("server.no-perm", player);
                    }
                } else if (args.length == 1) {
                    if (args[0].equals("clear")) {
                        player.setDisplayName(player.getName());
                    } else {
                        player.setDisplayName(strings.getColor(args[0]));
                        messages.PlayerMessage("chat.newnick", "%player_display_name%", strings.getColor(args[0]), player);
                    }
                } else {
                    messages.PlayerMessage("chat.usenick", player);
                }
            } else {
                messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            if (args.length == 2) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null && target.isOnline()) {
                    if (args[1].equals("clear")) {
                        target.setDisplayName(target.getName());
                        messages.PlayerMessage("chat.remove-nick", target);
                        messages.ConsoleMessage("chat.remove-nick");
                    } else {
                        target.setDisplayName(strings.getColor(args[1]));
                        messages.ConsoleMessage("chat.newnick", "%player_display_name%", strings.getColor(args[1]));
                        messages.PlayerMessage("chat.newnick", "%player_display_name%", strings.getColor(args[1]), target);
                    }
                } else {
                    messages.ConsoleMessage("server.player-offline");
                }
            } else {
                messages.ConsoleMessage("chat.usenick");
            }
        }
        return true;
    }

}