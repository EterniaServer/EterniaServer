package br.com.eterniaserver.eterniaserver.modules.chatmanager.commands;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Vars;
import br.com.eterniaserver.eterniaserver.player.PlayerManager;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;

import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;
import java.util.Calendar;
import java.util.Date;

public class Mute extends BaseCommand {

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

    @CommandAlias("mutechannels|muteall")
    @CommandPermission("eternia.mute.channels")
    public void muteChannels(CommandSender sender) {
        if (plugin.chatMuted) {
            plugin.chatMuted = false;
            messages.broadcastMessage("chat.cm-d", "%player_name%", sender.getName());
        } else {
            plugin.chatMuted = true;
            messages.broadcastMessage("chat.cm-e", "%player_name%", sender.getName());
        }
    }

    @CommandAlias("mute|silenciar")
    @CommandCompletion("@players Mensagem")
    @Syntax("<jogador> <mensagem>")
    @CommandPermission("eternia.mute")
    public void onMute(CommandSender player, OnlinePlayer target, @Optional String[] message) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.YEAR, 20);
        Date dataa = cal.getTime();
        final String date = plugin.sdf.format(dataa);
        StringBuilder sb = new StringBuilder();
        for (String arg : message) sb.append(arg).append(" ");
        messages.broadcastMessage("chat.mutebroad", "%player_name%", player.getName(), "%message%", sb);
        if (playerManager.registerMuted(target.getPlayer().getName())) {
            final String querie = "UPDATE " + plugin.serverConfig.getString("sql.table-muted") + " SET time='" + date + "' WHERE player_name='" + target.getPlayer().getName() + "';";
            plugin.connections.executeSQLQuery(connection -> {
                PreparedStatement putMuted = connection.prepareStatement(querie);
                putMuted.execute();
                putMuted.close();
            }, true);
        } else {
            final String querie = "INSERT INTO " + plugin.serverConfig.getString("sql.table-muted") + " (player_name, time) VALUES ('" + target.getPlayer().getName() + "', '" + date + "');";
            plugin.connections.executeSQLQuery(connection -> {
                PreparedStatement putMuted = connection.prepareStatement(querie);
                putMuted.execute();
                putMuted.close();
            }, true);
        }
        vars.player_muted.put(target.getPlayer().getName(), cal.getTimeInMillis());
    }

    @CommandAlias("unmute|desilenciar")
    @CommandCompletion("@players")
    @Syntax("<jogador>")
    @CommandPermission("eternia.unmute")
    public void onUnMute(OnlinePlayer target) {
        vars.player_muted.put(target.getPlayer().getName(), System.currentTimeMillis());
        messages.broadcastMessage("chat.unmutebroad", "%player_name%", target.getPlayer().getName());
        if (playerManager.registerMuted(target.getPlayer().getName())) {
            final String querie = "UPDATE " + plugin.serverConfig.getString("sql.table-muted") + " SET time='" + "2020/01/01 00:00" + "' WHERE player_name='" + target.getPlayer().getName() + "';";
            plugin.connections.executeSQLQuery(connection -> {
                PreparedStatement putMuted = connection.prepareStatement(querie);
                putMuted.execute();
                putMuted.close();
            }, true);
        } else {
            final String querie = "INSERT INTO " + plugin.serverConfig.getString("sql.table-muted") + " (player_name, time) VALUES ('" + target.getPlayer().getName() + "', '" + "2020/01/01 00:00" + "');";
            plugin.connections.executeSQLQuery(connection -> {
                PreparedStatement putMuted = connection.prepareStatement(querie);
                putMuted.execute();
                putMuted.close();
            }, true);
        }
    }

    @CommandAlias("tempmute|mutetemporario")
    @Syntax("<jogador> <tempo> <mensagem>")
    @CommandCompletion("@players 15 Mensagem")
    @CommandPermission("eternia.tempmute")
    public void onTempMute(OnlinePlayer target, Integer time, @Optional String[] message) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, time);
        final String date = plugin.sdf.format(cal.getTime());
        StringBuilder sb = new StringBuilder();
        for (String arg : message) sb.append(arg).append(" ");
        messages.broadcastMessage("chat.mutetbroad", "%player_name%", target.getPlayer().getName(), "%time%", time, "%message%", sb);
        if (vars.player_muted.containsKey(target.getPlayer().getName())) {
            final String querie = "UPDATE " + plugin.serverConfig.getString("sql.table-muted") + " SET time='" + date + "' WHERE player_name='" + target.getPlayer().getName() + "';";
            plugin.connections.executeSQLQuery(connection -> {
                PreparedStatement putMuted = connection.prepareStatement(querie);
                putMuted.execute();
                putMuted.close();
            }, true);
        } else {
            final String querie = "INSERT INTO " + plugin.serverConfig.getString("sql.table-muted") + " (player_name, time) VALUES ('" + target.getPlayer().getName() + "', '" + date + "');";
            plugin.connections.executeSQLQuery(connection -> {
                PreparedStatement putMuted = connection.prepareStatement(querie);
                putMuted.execute();
                putMuted.close();
            }, true);
        }
        vars.player_muted.put(target.getPlayer().getName(), cal.getTimeInMillis());
    }

}
