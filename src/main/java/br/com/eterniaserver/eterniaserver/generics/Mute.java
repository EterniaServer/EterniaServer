package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;

import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
    public void muteChannels(Player sender) {
        if (plugin.isChatMuted()) {
            plugin.setChatMuted(false);
            messages.broadcastMessage("chat.cm-d", Constants.PLAYER.get(), sender.getDisplayName());
        } else {
            plugin.setChatMuted(true);
            messages.broadcastMessage("chat.cm-e", Constants.PLAYER.get(), sender.getDisplayName());
        }
    }

    @CommandAlias("mute|silenciar")
    @CommandCompletion("@players Mensagem")
    @Syntax("<jogador> <mensagem>")
    @CommandPermission("eternia.mute")
    public void onMute(Player player, OnlinePlayer target, @Optional String[] message) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.YEAR, 20);
        Date dataa = cal.getTime();
        final String date = plugin.sdf.format(dataa);
        messages.broadcastMessage("chat.mutebroad", Constants.PLAYER.get(), player.getDisplayName(), Constants.MESSAGE.get(), messageFull(message));
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
        messages.broadcastMessage("chat.unmutebroad", Constants.PLAYER.get(), target.getPlayer().getDisplayName());
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
        messages.broadcastMessage("chat.mutetbroad", Constants.PLAYER.get(), target.getPlayer().getDisplayName(), "%time%", time, Constants.MESSAGE.get(), messageFull(message));
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
