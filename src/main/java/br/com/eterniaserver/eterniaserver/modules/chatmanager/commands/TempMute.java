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

public class TempMute implements CommandExecutor {

    private final EterniaServer plugin;
    private final Messages messages;
    private final Vars vars;
    private final PlayerManager playerManager;

    public TempMute(EterniaServer plugin, Messages messages, Vars vars, PlayerManager playerManager) {
        this.plugin = plugin;
        this.messages = messages;
        this.vars = vars;
        this.playerManager = playerManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("eternia.tempmute")) {
                if (args.length >= 3) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null && target.isOnline()) {
                        try {
                            final int time = Integer.parseInt(args[1]);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(new Date());
                            cal.add(Calendar.MINUTE, time);
                            Date dataa = cal.getTime();
                            final String date = plugin.sdf.format(dataa);
                            vars.player_muted.put(target.getName(), cal.getTimeInMillis());
                            StringBuilder sb = new StringBuilder();
                            for (String arg : args) {
                                sb.append(arg).append(" ");
                            }
                            messages.BroadcastMessage("chat.mutetbroad", "%player_name%", target.getName(), "%time%", time, "%message%", sb.substring(4, sb.length() - 1));
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
                        } catch (NumberFormatException e) {
                            messages.PlayerMessage("no-number", player);
                        }
                    } else {
                        messages.PlayerMessage("server.player-offline", player);
                    }
                } else {
                    messages.PlayerMessage("chat.mutetuse", player);
                }
            } else {
                messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            if (args.length >= 3) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null && target.isOnline()) {
                    try {
                        final int time = Integer.parseInt(args[1]);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(new Date());
                        cal.add(Calendar.MINUTE, time);
                        final String date = plugin.sdf.format(cal);
                        StringBuilder sb = new StringBuilder();
                        for (String arg : args) {
                            sb.append(arg).append(" ");
                        }
                        messages.BroadcastMessage("chat.mutetbroad", "%player_name%", target.getName(), "%time%", time, "%message%", sb.substring(4, sb.length() - 1));
                        if (vars.player_muted.containsKey(target.getName())) {
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
                    } catch (NumberFormatException e) {
                        messages.ConsoleMessage("no-number");
                    }
                } else {
                    messages.ConsoleMessage("server.player-offline");
                }
            } else {
                messages.ConsoleMessage("chat.mutetuse");
            }
        }
        return true;
    }
}
