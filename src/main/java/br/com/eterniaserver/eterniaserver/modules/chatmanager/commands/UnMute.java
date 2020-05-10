package br.com.eterniaserver.eterniaserver.modules.chatmanager.commands;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Vars;
import br.com.eterniaserver.eterniaserver.player.PlayerManager;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;

public class UnMute implements CommandExecutor {

    private final EterniaServer plugin;
    private final Messages messages;
    private final Vars vars;
    private final PlayerManager playerManager;

    public UnMute(EterniaServer plugin, Messages messages, Vars vars, PlayerManager playerManager) {
        this.plugin = plugin;
        this.messages = messages;
        this.vars = vars;
        this.playerManager = playerManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, java.lang.String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.unmute")) {
                if (args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null && target.isOnline()) {
                        vars.player_muted.put(target.getName(), System.currentTimeMillis());
                        messages.BroadcastMessage("chat.unmutebroad", "%player_name%", target.getName());
                        if (playerManager.registerMuted(target.getName())) {
                            final String querie = "UPDATE " + plugin.serverConfig.getString("sql.table-muted") + " SET time='" + "2020/01/01 00:00" + "' WHERE player_name='" + target.getName() + "';";
                            plugin.connections.executeSQLQuery(connection -> {
                                PreparedStatement putMuted = connection.prepareStatement(querie);
                                putMuted.execute();
                                putMuted.close();
                            }, true);
                        } else {
                            final String querie = "INSERT INTO " + plugin.serverConfig.getString("sql.table-muted") + " (player_name, time) VALUES ('" + target.getName() + "', '" + "2020/01/01 00:00" + "');";
                            plugin.connections.executeSQLQuery(connection -> {
                                PreparedStatement putMuted = connection.prepareStatement(querie);
                                putMuted.execute();
                                putMuted.close();
                            }, true);
                        }
                    } else {
                        messages.PlayerMessage("server.player-offline", player);
                    }
                } else {
                    messages.PlayerMessage("chat.useunm", player);
                }
            } else {
                messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            if (args.length == 1) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null && target.isOnline()) {
                    vars.player_muted.put(target.getName(), System.currentTimeMillis());
                    messages.BroadcastMessage("chat.unmutebroad", "%player_name%", target.getName());
                    if (playerManager.registerMuted(target.getName())) {
                        final String querie = "UPDATE " + plugin.serverConfig.getString("sql.table-muted") + " SET time='" + "2020/01/01 00:00" + "' WHERE player_name='" + target.getName() + "';";
                        plugin.connections.executeSQLQuery(connection -> {
                            PreparedStatement putMuted = connection.prepareStatement(querie);
                            putMuted.execute();
                            putMuted.close();
                        }, true);
                    } else {
                        final String querie = "INSERT INTO " + plugin.serverConfig.getString("sql.table-muted") + " (player_name, time) VALUES ('" + target.getName() + "', '" + "2020/01/01 00:00" + "');";
                        plugin.connections.executeSQLQuery(connection -> {
                            PreparedStatement putMuted = connection.prepareStatement(querie);
                            putMuted.execute();
                            putMuted.close();
                        }, true);
                    }
                } else {
                    messages.ConsoleMessage("server.player-offline");
                }
            } else {
                messages.ConsoleMessage("chat.useunm");
            }
        }
        return true;
    }

}
