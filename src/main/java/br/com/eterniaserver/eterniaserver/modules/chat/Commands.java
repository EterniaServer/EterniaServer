package br.com.eterniaserver.eterniaserver.modules.chat;

import br.com.eterniaserver.acf.BaseCommand;
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
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;

import net.kyori.adventure.text.Component;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

final class Commands {

    @CommandAlias("%chat")
    static class Chat extends BaseCommand {

        private final EterniaServer plugin;
        private final Services.ChatService chatService;

        public Chat(EterniaServer plugin, Services.ChatService chatService) {
            this.plugin = plugin;
            this.chatService = chatService;
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
            for (int i = 0; i < 150; i ++) {
                Bukkit.broadcast(Component.empty());
            }
        }

        @CommandAlias("%channel")
        @Syntax("%channel_syntax")
        @CommandPermission("%channel_perm")
        @Description("%channel_description")
        @CommandCompletion("@channels")
        public void onChannel(Player player, @Conditions("channel") String channel, @Optional String message) {
            final UUID uuid = player.getUniqueId();
            final PlayerProfile playerProfile = plugin.userManager().get(uuid);
            final int channelCode = channel.hashCode();

            if (message == null) {
                playerProfile.setChatChannel(channelCode);
                plugin.sendMiniMessages(player, Messages.CHAT_CHANNEL_CHANGED, channel);
                return;
            }

            final int defaultChannel = playerProfile.getChatChannel();
            playerProfile.setChatChannel(channelCode);
            player.chat(message);
            playerProfile.setChatChannel(defaultChannel);
        }

        @Subcommand("%chat_broadcast")
        @Syntax("%chat_broadcast_syntax")
        @Description("%chat_broadcast_description")
        @CommandPermission("%chat_broadcast_perm")
        @CommandAlias("%chat_broadcast_aliases")
        public void onBroadcast(String message) {
            Bukkit.broadcast(plugin.getMiniMessage(Messages.CHAT_BROADCAST, true, message));
        }

        @Subcommand("%chat_spy")
        @Description("%chat_spy_description")
        @CommandPermission("%chat_spy_perm")
        @CommandAlias("%chat_spy_aliases")
        public void onSpy(Player player) {
            final UUID uuid = player.getUniqueId();
            final PlayerProfile playerProfile = plugin.userManager().get(uuid);

            if (playerProfile.getSpy()) {
                plugin.sendMiniMessages(player, Messages.CHAT_SPY_DISABLED);
                playerProfile.setSpy(false);
                return;
            }

            plugin.sendMiniMessages(player, Messages.CHAT_SPY_ENABLED);
            playerProfile.setSpy(true);
        }

        @Subcommand("%chat_reply")
        @Syntax("%chat_reply_syntax")
        @Description("%chat_reply_description")
        @CommandPermission("%chat_reply_perm")
        @CommandAlias("%chat_reply_aliases")
        public void onReply(Player player, String msg) {
            final UUID uuid = player.getUniqueId();
            if (EterniaServer.getChatAPI().isMuted(uuid)) {
                plugin.sendMiniMessages(
                        player,
                        Messages.CHAT_ARE_MUTED,
                        String.valueOf(EterniaServer.getChatAPI().secondsMutedLeft(uuid))
                );
                return;
            }

            final UUID targetUUID = EterniaServer.getChatAPI().getTellLink(uuid);
            if (targetUUID != null && msg != null) {
                final Player target = Bukkit.getPlayer(targetUUID);
                if (target != null && target.isOnline()) {
                    chatService.sendPrivateMessage(player, target, msg);
                    return;
                }
            }

            plugin.sendMiniMessages(player, Messages.CHAT_NO_ONE_TO_RESP);
        }

        @Subcommand("%chat_tell")
        @Syntax("%chat_tell_syntax")
        @Description("%chat_tell_description")
        @CommandPermission("%chat_tell_perm")
        @CommandCompletion("@players")
        @CommandAlias("%chat_tell_aliases")
        public void onTell(Player player, @Optional OnlinePlayer onlineTarget, @Optional String msg) {
            final UUID playerUUID = player.getUniqueId();
            if (EterniaServer.getChatAPI().isMuted(playerUUID)) {
                return;
            }

            final PlayerProfile playerProfile = plugin.userManager().get(playerUUID);
            if (onlineTarget == null) {
                playerProfile.setChatChannel(plugin.getString(Strings.DEFAULT_CHANNEL).hashCode());
                plugin.sendMiniMessages(player, Messages.CHAT_CHANNEL_CHANGED, plugin.getString(Strings.DEFAULT_CHANNEL));
                return;
            }

            final Player target = onlineTarget.getPlayer();
            if (msg != null && msg.length() != 0) {
                chatService.sendPrivateMessage(player, target, msg);
                return;
            }

            final UUID targetUUID = target.getUniqueId();
            final PlayerProfile targetProfile = plugin.userManager().get(targetUUID);
            if (EterniaServer.getChatAPI().getTellLink(playerUUID) == targetUUID) {
                playerProfile.setChatChannel(plugin.getString(Strings.DEFAULT_CHANNEL).hashCode());
                EterniaServer.getChatAPI().removeTellLink(playerUUID);
                plugin.sendMiniMessages(player, Messages.CHAT_TELL_UNLOCKED, targetProfile.getName(), targetProfile.getDisplayName());
                return;
            }

            EterniaServer.getChatAPI().setTellLink(targetUUID, playerUUID);
            playerProfile.setChatChannel("tell".hashCode());
            plugin.sendMiniMessages(player, Messages.CHAT_TELL_LOCKED, targetProfile.getName(), targetProfile.getDisplayName());
        }
    }

}
