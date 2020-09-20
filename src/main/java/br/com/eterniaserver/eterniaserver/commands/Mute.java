package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.acf.annotation.Optional;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;

import br.com.eterniaserver.eterniaserver.generics.APIPlayer;
import br.com.eterniaserver.eterniaserver.generics.APIServer;
import br.com.eterniaserver.eterniaserver.generics.PluginConfigs;
import br.com.eterniaserver.eterniaserver.generics.PluginConstants;
import br.com.eterniaserver.eterniaserver.generics.PluginMSGs;
import br.com.eterniaserver.eterniaserver.generics.PluginVars;
import br.com.eterniaserver.eterniaserver.generics.UtilInternMethods;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class Mute extends BaseCommand {

    @CommandAlias("mutechannels|muteall")
    @CommandPermission("eternia.mute.channels")
    public void muteChannels(Player sender) {
        if (APIServer.isChatMuted()) {
            UtilInternMethods.setChatMuted(false);
            Bukkit.broadcastMessage(UtilInternMethods.putName(sender, PluginMSGs.M_CHAT_D));
        } else {
            UtilInternMethods.setChatMuted(true);
            Bukkit.broadcastMessage(UtilInternMethods.putName(sender, PluginMSGs.M_CHAT_E));
        }
    }

    @CommandAlias("mute|silenciar")
    @CommandCompletion("@players Mensagem")
    @Syntax("<jogador> <mensagem>")
    @CommandPermission("eternia.mute")
    public void onMute(Player player, OnlinePlayer target, @Optional String message) {
        final Player targetP = target.getPlayer();
        final String targetName = targetP.getName();
        final UUID uuid = UUIDFetcher.getUUIDOf(targetName);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.YEAR, 20);
        long time = cal.getTimeInMillis();
        Bukkit.broadcastMessage(UtilInternMethods.putName(targetP, PluginMSGs.M_CHAT_MUTEBROAD).replace(PluginConstants.MESSAGE, message));
        EQueries.executeQuery(PluginConstants.getQueryUpdate(PluginConfigs.TABLE_PLAYER, PluginConstants.TIME_STR, time, PluginConstants.UUID_STR, uuid.toString()));
        APIPlayer.putMutedTime(uuid, time);
    }

    @CommandAlias("unmute|desilenciar")
    @CommandCompletion("@players")
    @Syntax("<jogador>")
    @CommandPermission("eternia.unmute")
    public void onUnMute(OnlinePlayer target) {
        final long time = System.currentTimeMillis();
        final String playerName = target.getPlayer().getName();
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        APIPlayer.putMutedTime(uuid, time);
        Bukkit.broadcastMessage(UtilInternMethods.putName(target.getPlayer(), PluginMSGs.M_CHAT_UNMUTEBROAD));
        EQueries.executeQuery(PluginConstants.getQueryUpdate(PluginConfigs.TABLE_PLAYER, PluginConstants.TIME_STR, time, PluginConstants.UUID_STR, uuid.toString()));
    }

    @CommandAlias("tempmute|mutetemporario")
    @Syntax("<jogador> <tempo> <mensagem>")
    @CommandCompletion("@players 15 Mensagem")
    @CommandPermission("eternia.tempmute")
    public void onTempMute(OnlinePlayer target, Integer time, @Optional String message) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, time);
        final long timeInMillis = cal.getTimeInMillis();
        final String targetName = target.getPlayer().getName();
        final UUID uuid = UUIDFetcher.getUUIDOf(targetName);
        Bukkit.broadcastMessage(UtilInternMethods.putName(target.getPlayer(), PluginMSGs.M_CHAT_MUTET.replace(PluginConstants.TIME, String.valueOf(time)).replace(PluginConstants.MESSAGE, message)));
        EQueries.executeQuery(PluginConstants.getQueryUpdate(PluginConfigs.TABLE_PLAYER, PluginConstants.TIME_STR, timeInMillis, PluginConstants.UUID_STR, uuid.toString()));
        APIPlayer.putMutedTime(uuid, timeInMillis);
    }

}
