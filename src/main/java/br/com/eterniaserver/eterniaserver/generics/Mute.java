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

    @CommandAlias("mutechannels|muteall")
    @CommandPermission("eternia.mute.channels")
    public void muteChannels(Player sender) {
        if (Vars.chatMuted) {
            InternMethods.setChatMuted(false);
            Bukkit.broadcastMessage(InternMethods.putName(sender, Strings.M_CHAT_D));
        } else {
            InternMethods.setChatMuted(true);
            Bukkit.broadcastMessage(InternMethods.putName(sender, Strings.M_CHAT_E));
        }
    }

    @CommandAlias("mute|silenciar")
    @CommandCompletion("@players Mensagem")
    @Syntax("<jogador> <mensagem>")
    @CommandPermission("eternia.mute")
    public void onMute(Player player, OnlinePlayer target, @Optional String[] message) {
        final Player targetP = target.getPlayer();
        final String targetName = targetP.getName();
        final UUID uuid = UUIDFetcher.getUUIDOf(targetName);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.YEAR, 20);
        long time = cal.getTimeInMillis();
        Bukkit.broadcastMessage(InternMethods.putName(targetP, Strings.M_CHAT_MUTEBROAD).replace(Constants.MESSAGE, messageFull(message)));
        EQueries.executeQuery(Constants.getQueryUpdate(Configs.TABLE_PLAYER, Constants.TIME_STR, time, Constants.UUID_STR, uuid.toString()));
        Vars.playerProfile.get(uuid).muted = time;
    }

    @CommandAlias("unmute|desilenciar")
    @CommandCompletion("@players")
    @Syntax("<jogador>")
    @CommandPermission("eternia.unmute")
    public void onUnMute(OnlinePlayer target) {
        final long time = System.currentTimeMillis();
        final String playerName = target.getPlayer().getName();
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        Vars.playerProfile.get(uuid).muted = time;
        Bukkit.broadcastMessage(InternMethods.putName(target.getPlayer(), Strings.M_CHAT_UNMUTEBROAD));
        EQueries.executeQuery(Constants.getQueryUpdate(Configs.TABLE_PLAYER, Constants.TIME_STR, time, Constants.UUID_STR, uuid.toString()));
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
        Bukkit.broadcastMessage(InternMethods.putName(target.getPlayer(), Strings.M_CHAT_MUTET.replace(Constants.TIME, String.valueOf(time)).replace(Constants.MESSAGE, messageFull(message))));
        EQueries.executeQuery(Constants.getQueryUpdate(Configs.TABLE_PLAYER, Constants.TIME_STR, timeInMillis, Constants.UUID_STR, uuid.toString()));
        Vars.playerProfile.get(uuid).muted = timeInMillis;
    }

    private String messageFull(String[] message) {
        StringBuilder sb = new StringBuilder();
        for (String arg : message) sb.append(arg).append(" ");
        return sb.toString();
    }

}
