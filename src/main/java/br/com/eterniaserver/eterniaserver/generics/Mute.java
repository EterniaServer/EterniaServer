package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import br.com.eterniaserver.eterniaserver.Strings;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;

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

        HashMap<String, String> temp = EQueries.getMapString(Constants.getQuerySelectAll(Constants.TABLE_MUTED), Strings.PNAME, Strings.TIME);
        temp.forEach((k, v) -> Vars.playerMuted.put(k, Long.parseLong(v)));
        messages.sendConsole(Strings.M_LOAD_DATA, Constants.MODULE, "Muted Players", Constants.AMOUNT, temp.size());

    }

    @CommandAlias("mutechannels|muteall")
    @CommandPermission("eternia.mute.channels")
    public void muteChannels(Player sender) {
        if (plugin.isChatMuted()) {
            plugin.setChatMuted(false);
            messages.broadcastMessage(Strings.M_CHAT_D, Constants.PLAYER, sender.getDisplayName());
        } else {
            plugin.setChatMuted(true);
            messages.broadcastMessage(Strings.M_CHAT_E, Constants.PLAYER, sender.getDisplayName());
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
        final String targetName = target.getPlayer().getName();
        messages.broadcastMessage(Strings.M_CHAT_MUTEBROAD, Constants.PLAYER, targetName, Constants.MESSAGE, messageFull(message));
        EQueries.executeQuery(Constants.getQueryUpdate(Constants.TABLE_MUTED, Strings.TIME, time, Strings.PNAME, targetName));
        Vars.playerMuted.put(targetName, cal.getTimeInMillis());
    }

    @CommandAlias("unmute|desilenciar")
    @CommandCompletion("@players")
    @Syntax("<jogador>")
    @CommandPermission("eternia.unmute")
    public void onUnMute(OnlinePlayer target) {
        final long time = System.currentTimeMillis();
        final String playerName = target.getPlayer().getName();
        Vars.playerMuted.put(playerName, time);
        messages.broadcastMessage(Strings.M_CHAT_UNMUTEBROAD, Constants.PLAYER, target.getPlayer().getDisplayName());
        EQueries.executeQuery(Constants.getQueryUpdate(Constants.TABLE_MUTED, Strings.TIME, time, Strings.PNAME, playerName));
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
        final String targetName = target.getPlayer().getName();
        messages.broadcastMessage(Strings.M_CHAT_MUTET, Constants.PLAYER, target.getPlayer().getDisplayName(), Constants.TIME, time, Constants.MESSAGE, messageFull(message));
        EQueries.executeQuery(Constants.getQueryUpdate(Constants.TABLE_MUTED, Strings.TIME, timeInMillis, Strings.PNAME, targetName));
        Vars.playerMuted.put(targetName, cal.getTimeInMillis());
    }

    private String messageFull(String[] message) {
        StringBuilder sb = new StringBuilder();
        for (String arg : message) sb.append(arg).append(" ");
        return sb.toString();
    }

}
