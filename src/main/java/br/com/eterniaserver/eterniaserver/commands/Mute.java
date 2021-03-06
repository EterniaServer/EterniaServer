package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.acf.CommandHelp;
import br.com.eterniaserver.acf.annotation.CatchUnknown;
import br.com.eterniaserver.acf.annotation.CommandAlias;
import br.com.eterniaserver.acf.annotation.CommandCompletion;
import br.com.eterniaserver.acf.annotation.CommandPermission;
import br.com.eterniaserver.acf.annotation.Default;
import br.com.eterniaserver.acf.annotation.Description;
import br.com.eterniaserver.acf.annotation.HelpCommand;
import br.com.eterniaserver.acf.annotation.Optional;
import br.com.eterniaserver.acf.annotation.Subcommand;
import br.com.eterniaserver.acf.annotation.Syntax;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.eternialib.SQL;
import br.com.eterniaserver.eternialib.core.queries.Update;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.objects.User;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.enums.Messages;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Calendar;
import java.util.Date;

@CommandAlias("%mute")
public class Mute extends BaseCommand {

    private final EterniaServer plugin;

    public Mute(final EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Default
    @CatchUnknown
    @HelpCommand
    @Syntax("%mute_syntax")
    @CommandPermission("%mute_perm")
    @Description("%mute_description")
    public void onDefault(CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("%mute_channels")
    @CommandPermission("%mute_channels_perm")
    @Description("%mute_channels_description")
    public void muteChannels(Player sender) {
        if (plugin.isChatMuted()) {
            Bukkit.broadcastMessage(plugin.getMessage(Messages.CHAT_CHANNELS_DISABLED, true, sender.getName(), sender.getDisplayName()));
        } else {
            Bukkit.broadcastMessage(plugin.getMessage(Messages.CHAT_CHANNELS_ENABLED, true, sender.getName(), sender.getDisplayName()));
        }
        plugin.changeChatLockState();
    }

    @CommandCompletion("@players Mensagem")
    @Syntax("%mute_perma_syntax")
    @Subcommand("%mute_perma")
    @CommandPermission("%mute_perma_perm")
    @Description("%mute_perma_description")
    public void onMute(Player player, OnlinePlayer targets, @Optional String message) {
        User target = new User(targets.getPlayer());

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.YEAR, 20);
        long time = cal.getTimeInMillis();
        Bukkit.broadcastMessage(plugin.getMessage(Messages.CHAT_BROADCAST_MUTE, true, target.getName(), target.getDisplayName(), player.getName(), player.getDisplayName(), message));

        Update update = new Update(plugin.getString(Strings.TABLE_PLAYER));
        update.set.set("time", time);
        update.where.set("uuid", target.getUUID().toString());
        SQL.executeAsync(update);

        target.putMutedTime(time);
    }

    @CommandCompletion("@players")
    @Syntax("%mute_undo_syntax")
    @Subcommand("%mute_undo")
    @CommandPermission("%mute_undo_perm")
    @Description("%mute_undo_description")
    public void onUnMute(Player player, OnlinePlayer targets) {
        User target = new User(targets.getPlayer());
        final long time = System.currentTimeMillis();

        target.putMutedTime(time);
        Bukkit.broadcastMessage(plugin.getMessage(Messages.CHAT_BROADCAST_UNMUTE, true, target.getName(), target.getDisplayName(), player.getName(), player.getDisplayName()));

        Update update = new Update(plugin.getString(Strings.TABLE_PLAYER));
        update.set.set("time", time);
        update.where.set("uuid", target.getUUID().toString());
        SQL.executeAsync(update);
    }

    @CommandCompletion("@players 15 Mensagem")
    @Syntax("%mute_temp_syntax")
    @Subcommand("%mute_temp")
    @CommandPermission("%mute_temp_perm")
    @Description("%mute_temp_description")
    public void onTempMute(Player player, OnlinePlayer targets, Integer time, @Optional String message) {
        User target = new User(targets.getPlayer());
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, time);
        final long timeInMillis = cal.getTimeInMillis();

        Bukkit.broadcastMessage(plugin.getMessage(Messages.CHAT_BROADCAST_TEMP_MUTE, true, target.getName(), target.getDisplayName(), String.valueOf(time), player.getName(), player.getDisplayName(), message));

        Update update = new Update(plugin.getString(Strings.TABLE_PLAYER));
        update.set.set("time", time);
        update.where.set("uuid", target.getUUID().toString());
        SQL.executeAsync(update);

        target.putMutedTime(timeInMillis);
    }

}
