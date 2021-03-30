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
import br.com.eterniaserver.eternialib.CmdConfirmationManager;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.objects.RunCommand;
import br.com.eterniaserver.eterniaserver.objects.User;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.enums.Doubles;
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
            plugin.sendMessage(player, Messages.CHAT_CHANNEL_CHANGED, channel);
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
        Bukkit.broadcastMessage(plugin.getMessage(Messages.CHAT_BROADCAST, true, plugin.translateHex(plugin.getColor(message))));
    }

    @Subcommand("%chat_vanish")
    @Description("%chat_vanish_description")
    @CommandPermission("%chat_vanish_perm")
    @CommandAlias("%chat_vanish_aliases")
    public void onVanish(Player player) {
        User user = new User(player);
        if (user.isVanished()) {
            Bukkit.broadcastMessage(plugin.getMessage(Messages.SERVER_LOGIN, true, user.getName(), user.getDisplayName()));
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.showPlayer(plugin, player);
            }
        } else {
            Bukkit.broadcastMessage(plugin.getMessage(Messages.SERVER_LOGOUT, true, user.getName(), user.getDisplayName()));
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
            plugin.sendMessage(player, Messages.CHAT_SPY_ENABLED);
            return;
        }
        plugin.sendMessage(player, Messages.CHAT_SPY_DISABLED);
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
        plugin.sendMessage(sender, Messages.CHAT_NO_ONE_TO_RESP);
    }

    @Subcommand("%chat_tell")
    @Syntax("%chat_tell_syntax")
    @Description("%chat_tell_description")
    @CommandPermission("%chat_tell_perm")
    @CommandCompletion("@players")
    @CommandAlias("%chat_tell_aliases")
    public void onTell(Player player, @Optional OnlinePlayer targets, @Optional String msg) {
        User user = new User(player);

        if (isMuted(user)) return;

        if (targets == null) {
            user.setChannel(0);
            plugin.sendMessage(player, Messages.CHAT_CHANNEL_CHANGED, plugin.getString(Strings.CONS_LOCAL));
            return;
        }

        User target = new User(targets.getPlayer());

        if (user.isTell()) {
            user.removeTelling();
            plugin.sendMessage(player, Messages.CHAT_TELL_UNLOCKED, target.getName(), target.getDisplayName());
            return;
        }

        if (msg == null || msg.length() == 0) {
            user.setTelling(target.getUUID());
            user.setChannel("tell".hashCode());
            plugin.sendMessage(player, Messages.CHAT_TELL_LOCKED, target.getName(), target.getDisplayName());
            return;
        }

        user.sendPrivate(target.getPlayer(), msg);
    }

    @CommandAlias("%nick")
    @CommandCompletion("@players")
    @Syntax("%nick_syntax")
    @Description("%nick_description")
    @CommandPermission("%nick_perm")
    public void onNick(Player player, String newName, @Optional OnlinePlayer targets) {
        User user = new User(player);
        final String nick = plugin.translateHex(newName);
        
        if (targets == null) {
            if (EterniaServer.getEconomyAPI().hasMoney(user.getUUID(), plugin.getDouble(Doubles.NICK_COST))) {
                plugin.sendMessage(player, Messages.ECO_NO_MONEY, String.valueOf(plugin.getDouble(Doubles.NICK_COST)));
                return;
            }

            plugin.sendMessage(player, Messages.COMMAND_COST, String.valueOf(plugin.getDouble(Doubles.NICK_COST)));
            final RunCommand runCommand = new RunCommand(() -> {
                user.changeNick(nick);
                EterniaServer.getEconomyAPI().removeMoney(user.getUUID(), plugin.getDouble(Doubles.NICK_COST));
            });
            CmdConfirmationManager.scheduleCommand(player, runCommand);
            return;
        }

        if (!user.hasPermission(plugin.getString(Strings.PERM_NICK_OTHER))) {
            plugin.sendMessage(player, Messages.SERVER_NO_PERM);
            return;
        }

        User target = new User(targets.getPlayer());

        target.changeNick(nick);
        plugin.sendMessage(player, Messages.CHAT_NICK_CHANGE_TO, nick, target.getName(), target.getDisplayName());

    }

    private boolean isMuted(User user) {
        final long time = user.getMuteTime();
        if (plugin.isInFutureCooldown(time)) {
            plugin.sendMessage(user.getPlayer(), Messages.CHAT_ARE_MUTED, plugin.getTimeLeftOfCooldown(time));
            return true;
        }
        return false;
    }

}
