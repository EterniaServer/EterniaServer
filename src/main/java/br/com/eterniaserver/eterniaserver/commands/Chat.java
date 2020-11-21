package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.acf.CommandHelp;
import br.com.eterniaserver.acf.annotation.CatchUnknown;
import br.com.eterniaserver.acf.annotation.CommandAlias;
import br.com.eterniaserver.acf.annotation.CommandCompletion;
import br.com.eterniaserver.acf.annotation.CommandPermission;
import br.com.eterniaserver.acf.annotation.Conditions;
import br.com.eterniaserver.acf.annotation.Default;
import br.com.eterniaserver.acf.annotation.Description;
import br.com.eterniaserver.acf.annotation.HelpCommand;
import br.com.eterniaserver.acf.annotation.Optional;
import br.com.eterniaserver.acf.annotation.Subcommand;
import br.com.eterniaserver.acf.annotation.Syntax;
import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.core.APIServer;
import br.com.eterniaserver.eterniaserver.core.User;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.enums.Messages;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("%chat")
public class Chat extends BaseCommand {

    private final EterniaServer plugin;

    public Chat(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Default
    @CatchUnknown
    @HelpCommand
    @Syntax("%chat_syntax")
    @CommandPermission("%chat_perm")
    @Description("%chat_description")
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("%chat_clear")
    @Description("%chat_clear_description")
    @CommandPermission("%chat_clear_perm")
    @CommandAlias("%chat_clear_aliases")
    public void onClearChat() {
        for (int i = 0; i < 150; i ++) Bukkit.broadcastMessage("");
    }

    @CommandAlias("%channel")
    @Syntax("%channel_syntax")
    @CommandPermission("%channel_perm")
    @Description("%channel_description")
    @CommandCompletion("@channels")
    public void onChannel(Player player, @Conditions("channel") String channel, @Optional String message) {
        User user = new User(player);

        if (message == null) {
            user.setChannel(channel.hashCode());
            EterniaServer.sendMessage(player, Messages.CHAT_CHANNEL_CHANGED, channel);
            return;
        }

        int defaultChannel = user.getChannel();
        user.setChannel(channel.hashCode());
        player.chat(message);
        user.setChannel(defaultChannel);

    }

    @Subcommand("%chat_broadcast")
    @Syntax("%chat_broadcast_syntax")
    @Description("%chat_broadcast_description")
    @CommandPermission("%chat_broadcast_perm")
    @CommandAlias("%chat_broadcast_aliases")
    public void onBroadcast(String message) {
        Bukkit.broadcastMessage(EterniaServer.getMessage(Messages.CHAT_BROADCAST, true, APIServer.getColor(message)));
    }

    @Subcommand("%chat_vanish")
    @Description("%chat_vanish_description")
    @CommandPermission("%chat_vanish_perm")
    @CommandAlias("%chat_vanish_aliases")
    public void onVanish(Player player) {
        User user = new User(player);
        if (user.isVanished()) {
            Bukkit.broadcastMessage(EterniaServer.getMessage(Messages.SERVER_LOGIN, true, user.getName(), user.getDisplayName()));
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.showPlayer(plugin, player);
            }
        } else {
            Bukkit.broadcastMessage(EterniaServer.getMessage(Messages.SERVER_LOGOUT, true, user.getName(), user.getDisplayName()));
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.hidePlayer(plugin, player);
            }
        }
        user.changeVanishState();
    }

    @Subcommand("%chat_spy")
    @Description("%chat_spy_description")
    @CommandPermission("%chat_spy_perm")
    @CommandAlias("%chat_spy_aliases")
    public void onSpy(Player player) {
        User user = new User(player);
        user.changeSpyState();
        if (user.isSpying()) {
            user.sendMessage(Messages.CHAT_SPY_ENABLED);
            return;
        }
        user.sendMessage(Messages.CHAT_SPY_DISABLED);
    }

    @Subcommand("%chat_reply")
    @Syntax("%chat_reply_syntax")
    @Description("%chat_reply_description")
    @CommandPermission("%chat_reply_perm")
    @CommandAlias("%chat_reply_aliases")
    public void onResp(Player sender, String msg) {
        User user = new User(sender);
        if (isMuted(user)) return;

        if (user.receivedTell() && msg != null) {
            final Player target = Bukkit.getPlayer(user.getTellSender());
            if (target != null && target.isOnline()) {
                user.sendPrivate(target, msg);
                return;
            }
        }
        user.sendMessage(Messages.CHAT_NO_ONE_TO_RESP);
    }

    @Subcommand("%chat_tell")
    @Syntax("%chat_tell_syntax")
    @Description("%chat_tell_description")
    @CommandPermission("%chat_tell_perm")
    @CommandAlias("%chat_tell_aliases")
    public void onTell(Player player, @Optional OnlinePlayer targets, @Optional String msg) {
        User user = new User(player);

        if (isMuted(user)) return;

        if (targets == null) {
            user.setChannel(0);
            user.sendMessage(Messages.CHAT_CHANNEL_CHANGED, EterniaServer.getString(Strings.CONS_LOCAL));
            return;
        }

        User target = new User(targets.getPlayer());

        if (user.isTell()) {
            user.removeTelling();
            user.sendMessage(Messages.CHAT_TELL_UNLOCKED, target.getName(), target.getDisplayName());
            return;
        }

        if (msg == null || msg.length() == 0) {
            user.setTelling(target.getUUID());
            user.setChannel("tell".hashCode());
            user.sendMessage(Messages.CHAT_TELL_LOCKED, target.getName(), target.getDisplayName());
            return;
        }

        user.sendPrivate(target.getPlayer(), msg);
    }

    private boolean isMuted(User user) {
        final long time = user.getMuteTime();
        if (APIServer.isInFutureCooldown(time)) {
            user.sendMessage(Messages.CHAT_ARE_MUTED, APIServer.getTimeLeftOfCooldown(time));
            return true;
        }
        return false;
    }

}
