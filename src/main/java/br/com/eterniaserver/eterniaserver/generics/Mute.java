package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.acf.annotation.Optional;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.configs.Configs;
import br.com.eterniaserver.eterniaserver.configs.Constants;
import br.com.eterniaserver.eterniaserver.configs.Strings;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class Mute extends BaseCommand {

    public Mute() {
        final Map<String, String> temp = EQueries.getMapString(Constants.getQuerySelectAll(Configs.tableMuted), Constants.UUID_STR, Constants.TIME_STR);
        temp.forEach((k, v) -> Vars.playerMuted.put(UUID.fromString(k), Long.parseLong(v)));
        Bukkit.getConsoleSender().sendMessage(Strings.MSG_LOAD_DATA.replace(Constants.MODULE, "Muted Players").replace(Constants.AMOUNT, String.valueOf(temp.size())));
    }

    @CommandAlias("mutechannels|muteall")
    @CommandPermission("eternia.mute.channels")
    public void muteChannels(Player sender) {
        if (Vars.chatMuted) {
            InternMethods.setChatMuted(false);
            Bukkit.broadcastMessage(Strings.M_CHAT_D.replace(Constants.PLAYER, sender.getDisplayName()));
        } else {
            InternMethods.setChatMuted(true);
            Bukkit.broadcastMessage(Strings.M_CHAT_E.replace(Constants.PLAYER, sender.getDisplayName()));
        }
    }

    @CommandAlias("mute|silenciar")
    @CommandCompletion("@players Mensagem")
    @Syntax("<jogador> <mensagem>")
    @CommandPermission("eternia.mute")
    public void onMute(Player player, OnlinePlayer target, @Optional String[] message) {
        final Player targetP = target.getPlayer();
        final String targetName = targetP.getName();

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.YEAR, 20);
        long time = cal.getTimeInMillis();
        Bukkit.broadcastMessage(Strings.M_CHAT_MUTEBROAD.replace(Constants.PLAYER, targetP.getDisplayName()).replace(Constants.MESSAGE, messageFull(message)));
        EQueries.executeQuery(Constants.getQueryUpdate(Configs.tableMuted, Constants.TIME_STR, time, Constants.UUID_STR, UUIDFetcher.getUUIDOf(targetName).toString()));
        Vars.playerMuted.put(UUIDFetcher.getUUIDOf(targetName), cal.getTimeInMillis());
    }

    @CommandAlias("unmute|desilenciar")
    @CommandCompletion("@players")
    @Syntax("<jogador>")
    @CommandPermission("eternia.unmute")
    public void onUnMute(OnlinePlayer target) {
        final long time = System.currentTimeMillis();
        final String playerName = target.getPlayer().getName();
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        Vars.playerMuted.put(uuid, time);
        Bukkit.broadcastMessage(Strings.M_CHAT_UNMUTEBROAD.replace(Constants.PLAYER, target.getPlayer().getDisplayName()));
        EQueries.executeQuery(Constants.getQueryUpdate(Configs.tableMuted, Constants.TIME_STR, time, Constants.UUID_STR, uuid.toString()));
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
        final UUID uuid = UUIDFetcher.getUUIDOf(targetName);
        Bukkit.broadcastMessage(Strings.M_CHAT_MUTET.replace(Constants.PLAYER, target.getPlayer().getDisplayName()).replace(Constants.TIME, String.valueOf(time)).replace(Constants.MESSAGE, messageFull(message)));
        EQueries.executeQuery(Constants.getQueryUpdate(Configs.tableMuted, Constants.TIME_STR, timeInMillis, Constants.UUID_STR, uuid.toString()));
        Vars.playerMuted.put(uuid, cal.getTimeInMillis());
    }

    private String messageFull(String[] message) {
        StringBuilder sb = new StringBuilder();
        for (String arg : message) sb.append(arg).append(" ");
        return sb.toString();
    }

}
