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
import java.util.Calendar;
import java.util.Date;

public class Mute implements CommandExecutor {

    private final EterniaServer plugin;
    private final Messages messages;
    private final Vars vars;
    private final PlayerManager playerManager;

    public Mute(EterniaServer plugin, Messages messages, Vars vars, PlayerManager playerManager) {
        this.plugin = plugin;
        this.messages = messages;
        this.vars = vars;
        this.playerManager = playerManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, java.lang.String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.mute")) {
                if (args.length >= 2) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null && target.isOnline()) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(new Date());
                        cal.add(Calendar.YEAR, 20);
                        Date dataa = cal.getTime();
                        final String date = plugin.sdf.format(dataa);
                        StringBuilder sb = new StringBuilder();
                        for (String arg : args) {
                            sb.append(arg).append(" ");
                        }
                        messages.BroadcastMessage("chat.mutebroad", "%player_name%", target.getName(), "%message%", sb.substring(2, sb.length() - 1));
                        if (playerManager.registerMuted(target.getName())) {
                            final String querie = "UPDATE " + plugin.serverConfig.getString("sql.table-muted") + " SET time='" + date + "' WHERE player_name='" + target.getName() + "';";
                            plugin.connections.executeSQLQuery(connection -> {
                                PreparedStatement putMuted = connection.prepareStatement(querie);
                                putMuted.execute();
                                putMuted.close();
                            }, true);
                        } else {
                            final String querie = "INSERT INTO " + plugin.serverConfig.getString("sql.table-muted") + " (player_name, time) VALUES ('" + target.getName() + "', '" + date + "');";
                            plugin.connections.executeSQLQuery(connection -> {
                                PreparedStatement putMuted = connection.prepareStatement(querie);
                                putMuted.execute();
                                putMuted.close();
                            }, true);
                        }
                        vars.player_muted.put(target.getName(), cal.getTimeInMillis());
                    } else {
                        messages.PlayerMessage("server.player-offline", player);
                    }
                } else {
                    messages.PlayerMessage("chat.muteuse", player);
                }
            } else {
                messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            if (args.length >= 2) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null && target.isOnline()) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Date());
                    cal.add(Calendar.YEAR, 10);
                    Date dataa = cal.getTime();
                    final String date = plugin.sdf.format(dataa);
                    vars.player_muted.put(target.getName(), cal.getTimeInMillis());
                    StringBuilder sb = new StringBuilder();
                    for (String arg : args) {
                        sb.append(arg).append(" ");
                    }
                    messages.BroadcastMessage("chat.mutebroad", "%player_name%", target.getName(), "%message%", sb.substring(2, sb.length() - 1));
                    if (playerManager.registerMuted(target.getName())) {
                        final String querie = "UPDATE " + plugin.serverConfig.getString("sql.table-muted") + " SET time='" + date + "' WHERE player_name='" + target.getName() + "';";
                        plugin.connections.executeSQLQuery(connection -> {
                            PreparedStatement putMuted = connection.prepareStatement(querie);
                            putMuted.execute();
                            putMuted.close();
                        }, true);
                    } else {
                        final String querie = "INSERT INTO " + plugin.serverConfig.getString("sql.table-muted") + " (player_name, time) VALUES ('" + target.getName() + "', '" + date + "');";
                        plugin.connections.executeSQLQuery(connection -> {
                            PreparedStatement putMuted = connection.prepareStatement(querie);
                            putMuted.execute();
                            putMuted.close();
                        }, true);
                    }
                } else {
                    messages.sendConsole("server.player-offline");
                }
            } else {
                messages.sendConsole("chat.muteuse");
            }
        }
        return true;
    }
}
