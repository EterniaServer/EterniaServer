package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;

import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import org.bukkit.entity.Player;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Mute extends BaseCommand {

    private final EterniaServer plugin;
    private final EFiles messages;

    public Mute(EterniaServer plugin) {
        this.plugin = plugin;
        this.messages = plugin.getEFiles();

        String query = "SELECT * FROM " + EterniaServer.serverConfig.getString("sql.table-muted") + ";";
        HashMap<String, String> temp = EQueries.getMapString(query, "player_name", "time");

        temp.forEach((k, v) -> Vars.playerMuted.put(k, Long.parseLong(v)));
        messages.sendConsole("server.load-data", Constants.MODULE, "Muted Players", Constants.AMOUNT, temp.size());

    }

    @CommandAlias("mutechannels|muteall")
    @CommandPermission("eternia.mute.channels")
    public void muteChannels(Player sender) {
        if (plugin.isChatMuted()) {
            plugin.setChatMuted(false);
            messages.broadcastMessage("chat.cm-d", Constants.PLAYER, sender.getDisplayName());
        } else {
            plugin.setChatMuted(true);
            messages.broadcastMessage("chat.cm-e", Constants.PLAYER, sender.getDisplayName());
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
        long time = cal.getTimeInMillis();
        messages.broadcastMessage("chat.mutebroad", Constants.PLAYER, player.getDisplayName(), Constants.MESSAGE, messageFull(message));
        EQueries.executeQuery("UPDATE " + EterniaServer.serverConfig.getString("sql.table-muted") + " SET time='" + time + "' WHERE player_name='" + target.getPlayer().getName() + "';");
        Vars.playerMuted.put(target.getPlayer().getName(), cal.getTimeInMillis());
    }

    @CommandAlias("unmute|desilenciar")
    @CommandCompletion("@players")
    @Syntax("<jogador>")
    @CommandPermission("eternia.unmute")
    public void onUnMute(OnlinePlayer target) {
        final long time = System.currentTimeMillis();
        Vars.playerMuted.put(target.getPlayer().getName(), time);
        messages.broadcastMessage("chat.unmutebroad", Constants.PLAYER, target.getPlayer().getDisplayName());
        EQueries.executeQuery("UPDATE " + EterniaServer.serverConfig.getString("sql.table-muted") + " SET time='" + time + "' WHERE player_name='" + target.getPlayer().getName() + "';");
    }

    @CommandAlias("tempmute|mutetemporario")
    @Syntax("<jogador> <tempo> <mensagem>")
    @CommandCompletion("@players 15 Mensagem")
    @CommandPermission("eternia.tempmute")
    public void onTempMute(OnlinePlayer target, Integer time, @Optional String[] message) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, time);
        final long timeInMillis = cal.getTimeInMillis();
        messages.broadcastMessage("chat.mutetbroad", Constants.PLAYER, target.getPlayer().getDisplayName(), "%time%", time, Constants.MESSAGE, messageFull(message));
        EQueries.executeQuery("UPDATE " + EterniaServer.serverConfig.getString("sql.table-muted") + " SET time='" + timeInMillis + "' WHERE player_name='" + target.getPlayer().getName() + "';");
        Vars.playerMuted.put(target.getPlayer().getName(), cal.getTimeInMillis());
    }

    private String messageFull(String[] message) {
        StringBuilder sb = new StringBuilder();
        for (String arg : message) sb.append(arg).append(" ");
        return sb.toString();
    }

}
