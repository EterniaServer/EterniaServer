package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.acf.CommandHelp;
import br.com.eterniaserver.acf.annotation.CommandAlias;
import br.com.eterniaserver.acf.annotation.CommandCompletion;
import br.com.eterniaserver.acf.annotation.CommandPermission;
import br.com.eterniaserver.acf.annotation.Default;
import br.com.eterniaserver.acf.annotation.Description;
import br.com.eterniaserver.acf.annotation.Optional;
import br.com.eterniaserver.acf.annotation.Subcommand;
import br.com.eterniaserver.acf.annotation.Syntax;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.core.APIPlayer;
import br.com.eterniaserver.eterniaserver.core.APIServer;
import br.com.eterniaserver.eterniaserver.Constants;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@CommandAlias("mute")
@CommandPermission("eternia.mute")
public class Mute extends BaseCommand {

    @Default
    @Syntax("<página>")
    @Description(" Ajuda para os comandos de mute")
    public void onDefault(CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("channels|muteall")
    @CommandPermission("eternia.mute.channels")
    @Description(" Muta todos os canais")
    public void muteChannels(Player sender) {
        if (APIServer.isChatMuted()) {
            APIServer.setChatMuted(false);
            Bukkit.broadcastMessage(EterniaServer.msg.getMessage(Messages.CHAT_CHANNELS_DISABLED, true, sender.getName(), sender.getDisplayName()));
        } else {
            APIServer.setChatMuted(true);
            Bukkit.broadcastMessage(EterniaServer.msg.getMessage(Messages.CHAT_CHANNELS_ENABLED, true, sender.getName(), sender.getDisplayName()));
        }
    }

    @CommandAlias("perma")
    @CommandCompletion("@players Mensagem")
    @Syntax("<jogador> <mensagem>")
    @CommandPermission("eternia.mute.perma")
    @Description(" Muta um jogador permanentemente")
    public void onMute(Player player, OnlinePlayer target, @Optional String message) {
        final Player targetP = target.getPlayer();
        final String targetName = targetP.getName();
        final UUID uuid = UUIDFetcher.getUUIDOf(targetName);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.YEAR, 20);
        long time = cal.getTimeInMillis();
        Bukkit.broadcastMessage(EterniaServer.msg.getMessage(Messages.CHAT_BROADCAST_MUTE, true, targetName, targetP.getDisplayName(), player.getName(), player.getDisplayName(), message));
        EQueries.executeQuery(Constants.getQueryUpdate(EterniaServer.configs.tablePlayer, "time", time, "uuid", uuid.toString()));
        APIPlayer.putMutedTime(uuid, time);
    }

    @CommandAlias("unmute|desilenciar")
    @CommandCompletion("@players")
    @Syntax("<jogador>")
    @Description(" Desmuta um jogador")
    public void onUnMute(Player player, OnlinePlayer target) {
        final long time = System.currentTimeMillis();
        final String playerName = target.getPlayer().getName();
        final UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        APIPlayer.putMutedTime(uuid, time);
        Bukkit.broadcastMessage(EterniaServer.msg.getMessage(Messages.CHAT_BROADCAST_UNMUTE, true, playerName, target.getPlayer().getDisplayName(), player.getName(), player.getDisplayName()));
        EQueries.executeQuery(Constants.getQueryUpdate(EterniaServer.configs.tablePlayer, "time", time, "uuid", uuid.toString()));
    }

    @CommandAlias("temp|temporario")
    @Syntax("<jogador> <tempo> <mensagem>")
    @CommandCompletion("@players 15 Mensagem")
    @Description(" Muta temporariamente um jogador")
    public void onTempMute(Player player, OnlinePlayer target, Integer time, @Optional String message) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, time);
        final long timeInMillis = cal.getTimeInMillis();
        final String targetName = target.getPlayer().getName();
        final UUID uuid = UUIDFetcher.getUUIDOf(targetName);
        Bukkit.broadcastMessage(EterniaServer.msg.getMessage(Messages.CHAT_BROADCAST_TEMP_MUTE, true, targetName, target.getPlayer().getDisplayName(), String.valueOf(time), player.getName(), player.getDisplayName(), message));
        EQueries.executeQuery(Constants.getQueryUpdate(EterniaServer.configs.tablePlayer, "time", timeInMillis, "uuid", uuid.toString()));
        APIPlayer.putMutedTime(uuid, timeInMillis);
    }

}
