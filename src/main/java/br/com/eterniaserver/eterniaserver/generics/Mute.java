package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;

import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Calendar;
import java.util.Date;

public class Mute extends BaseCommand {

    private final EterniaServer plugin;
    private final EFiles messages;

    public Mute(EterniaServer plugin) {
        this.plugin = plugin;
        this.messages = plugin.getEFiles();
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
        messages.broadcastMessage("chat.mutebroad", "%player_name%", player.getName(), "%message%", messageFull(message));
        if (registerMuted(target.getPlayer().getName())) {
            EQueries.executeQuery("UPDATE " + plugin.serverConfig.getString("sql.table-muted") + " SET time='" + date + "' WHERE player_name='" + target.getPlayer().getName() + "';");
        } else {
            EQueries.executeQuery("INSERT INTO " + plugin.serverConfig.getString("sql.table-muted") + " (player_name, time) VALUES ('" + target.getPlayer().getName() + "', '" + date + "');");
        }
        Vars.playerMuted.put(target.getPlayer().getName(), cal.getTimeInMillis());
    }

    @CommandAlias("unmute|desilenciar")
    @CommandCompletion("@players")
    @Syntax("<jogador>")
    @CommandPermission("eternia.unmute")
    public void onUnMute(OnlinePlayer target) {
        Vars.playerMuted.put(target.getPlayer().getName(), System.currentTimeMillis());
        messages.broadcastMessage("chat.unmutebroad", "%player_name%", target.getPlayer().getName());
        if (registerMuted(target.getPlayer().getName())) {
            EQueries.executeQuery("UPDATE " + plugin.serverConfig.getString("sql.table-muted") + " SET time='" + "2020/01/01 00:00" + "' WHERE player_name='" + target.getPlayer().getName() + "';");
        } else {
            EQueries.executeQuery("INSERT INTO " + plugin.serverConfig.getString("sql.table-muted") + " (player_name, time) VALUES ('" + target.getPlayer().getName() + "', '" + "2020/01/01 00:00" + "');");
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
        messages.broadcastMessage("chat.mutetbroad", "%player_name%", target.getPlayer().getName(), "%time%", time, "%message%", messageFull(message));
        if (Vars.playerMuted.containsKey(target.getPlayer().getName())) {
            EQueries.executeQuery("UPDATE " + plugin.serverConfig.getString("sql.table-muted") + " SET time='" + date + "' WHERE player_name='" + target.getPlayer().getName() + "';");
        } else {
            EQueries.executeQuery("INSERT INTO " + plugin.serverConfig.getString("sql.table-muted") + " (player_name, time) VALUES ('" + target.getPlayer().getName() + "', '" + date + "');");
        }
        Vars.playerMuted.put(target.getPlayer().getName(), cal.getTimeInMillis());
    }

    private String messageFull(String[] message) {
        StringBuilder sb = new StringBuilder();
        for (String arg : message) sb.append(arg).append(" ");
        return sb.toString();
    }

    private boolean registerMuted(String playerName) {
        return EQueries.queryBoolean("SELECT * FROM " + plugin.serverConfig.getString("sql.table-muted") + " WHERE player_name='" + playerName + "';", "player_name");
    }

}
