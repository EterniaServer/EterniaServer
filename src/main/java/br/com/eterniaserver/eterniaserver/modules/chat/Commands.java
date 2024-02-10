package br.com.eterniaserver.eterniaserver.modules.chat;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.CommandHelp;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import br.com.eterniaserver.eterniaserver.modules.core.Entities.PlayerProfile;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

final class Commands {

    private Commands() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    static class Generic extends BaseCommand {
        private final EterniaServer plugin;
        private final Services.Chat chatService;

        public Generic(EterniaServer plugin, Services.Chat chatService) {
            this.plugin = plugin;
            this.chatService = chatService;
        }

        @CommandAlias("%NICKNAME")
        @Syntax("%NICKNAME_SYNTAX")
        @CommandPermission("%NICKNAME_PERM")
        @Description("%NICKNAME_DESCRIPTION")
        public void onNickname(Player player, @Optional String nickname) {
            if (nickname == null) {
                chatService.clearPlayerName(player);
                plugin.sendMiniMessages(player, Messages.NICKNAME_REMOVED);
                return;
            }

            String nickNameColor = chatService.setPlayerDisplay(player, nickname);
            plugin.sendMiniMessages(player, Messages.NICKNAME_UPDATED, nickNameColor);
        }
    }

    @CommandAlias("%CHAT")
    static class Chat extends BaseCommand {

        private final EterniaServer plugin;
        private final Services.Chat chatService;

        public Chat(EterniaServer plugin, Services.Chat chatService) {
            this.plugin = plugin;
            this.chatService = chatService;
        }

        private int getDefaultChannel(Entities.ChatInfo chatInfo) {
            Integer defaultChannel = chatInfo.getDefaultChannel();
            int defaultServerChannel = plugin.getString(Strings.DEFAULT_CHANNEL).toLowerCase().hashCode();
            return Objects.requireNonNullElse(defaultChannel, defaultServerChannel);
        }

        @Default
        @CatchUnknown
        @HelpCommand
        @Syntax("%CHAT_SYNTAX")
        @CommandPermission("%CHAT_PERM")
        @Description("%CHAT_DESCRIPTION")
        public void onHelp(CommandHelp help) {
            help.showHelp();
        }

        @Subcommand("%CHAT_CLEAR")
        @Description("%CHAT_CLEAR_DESCRIPTION")
        @CommandPermission("%CHAT_CLEAR_PERM")
        @CommandAlias("%CHAT_CLEAR_ALIASES")
        public void onClearChat() {
            for (int i = 0; i < 150; i ++) {
                plugin.getServer().broadcast(Component.empty());
            }
        }

        @CommandAlias("%CHANNEL")
        @Syntax("%CHANNEL_SYNTAX")
        @CommandPermission("%CHANNEL_PERM")
        @Description("%CHANNEL_DESCRIPTION")
        @CommandCompletion("@channels")
        public void onChannel(Player player, @Conditions("channel") String channel, @Optional String message) {
            UUID uuid = player.getUniqueId();
            Entities.ChatInfo chatInfo = EterniaLib.getDatabase().get(Entities.ChatInfo.class, uuid);
            int channelCode = channel.toLowerCase().hashCode();

            if (message == null) {
                chatInfo.setDefaultChannel(channelCode);
                plugin.sendMiniMessages(player, Messages.CHAT_CHANNEL_CHANGED, channel);
                return;
            }

            int defaultChannel = getDefaultChannel(chatInfo);
            chatInfo.setDefaultChannel(channelCode);
            player.chat(message);
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> chatInfo.setDefaultChannel(defaultChannel), 10L);
        }

        @Subcommand("%CHAT_SPY")
        @Description("%CHAT_SPY_DESCRIPTION")
        @CommandPermission("%CHAT_SPY_PERM")
        @CommandAlias("%CHAT_SPY_ALIASES")
        public void onSpy(Player player) {
            UUID uuid = player.getUniqueId();

            if (chatService.isSpying(uuid)) {
                plugin.sendMiniMessages(player, Messages.CHAT_SPY_DISABLED);
                chatService.removeSpying(uuid);
                return;
            }

            plugin.sendMiniMessages(player, Messages.CHAT_SPY_ENABLED);
            chatService.setSpying(uuid);
        }

        @Subcommand("%CHAT_REPLY")
        @Syntax("%CHAT_REPLY_SYNTAX")
        @Description("%CHAT_REPLY_DESCRIPTION")
        @CommandPermission("%CHAT_REPLY_PERM")
        @CommandAlias("%CHAT_REPLY_ALIASES")
        public void onReply(Player player, String msg) {
            UUID uuid = player.getUniqueId();
            if (chatService.isMuted(uuid)) {
                plugin.sendMiniMessages(
                        player,
                        Messages.CHAT_ARE_MUTED,
                        String.valueOf(chatService.secondsMutedLeft(uuid))
                );
                return;
            }

            UUID targetUUID = EterniaServer.getChatAPI().getTellLink(uuid);
            if (targetUUID != null && msg != null) {
                final Player target = plugin.getServer().getPlayer(targetUUID);
                if (target != null && target.isOnline()) {
                    Entities.ChatInfo chatInfo = EterniaLib.getDatabase().get(Entities.ChatInfo.class, uuid);
                    int defaultChannel = getDefaultChannel(chatInfo);

                    chatInfo.setDefaultChannel(Services.Chat.TELL_CHANNEL_STRING.hashCode());
                    chatService.setTellLink(uuid, targetUUID);
                    player.chat(msg);

                    plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                        chatInfo.setDefaultChannel(defaultChannel);
                        chatService.removeTellLink(uuid);
                    }, 10L);
                    return;
                }
            }

            plugin.sendMiniMessages(player, Messages.CHAT_NO_ONE_TO_RESP);
        }

        @Subcommand("%CHAT_TELL")
        @Syntax("%CHAT_TELL_SYNTAX")
        @Description("%CHAT_TELL_DESCRIPTION")
        @CommandPermission("%CHAT_TELL_PERM")
        @CommandCompletion("@players")
        @CommandAlias("%CHAT_TELL_ALIASES")
        public void onTell(Player player, @Optional OnlinePlayer onlineTarget, @Optional String msg) {
            UUID playerUUID = player.getUniqueId();
            if (chatService.isMuted(playerUUID)) {
                return;
            }

            Entities.ChatInfo chatInfo = EterniaLib.getDatabase().get(Entities.ChatInfo.class, playerUUID);
            if (onlineTarget == null) {
                chatInfo.setDefaultChannel(plugin.getString(Strings.DEFAULT_CHANNEL).toLowerCase().hashCode());
                plugin.sendMiniMessages(player, Messages.CHAT_CHANNEL_CHANGED, plugin.getString(Strings.DEFAULT_CHANNEL));
                return;
            }

            Player target = onlineTarget.getPlayer();
            if (target.getUniqueId().equals(playerUUID)) {
                plugin.sendMiniMessages(player, Messages.CHAT_TELL_YOURSELF);
                return;
            }

            if (msg != null && !msg.isEmpty()) {
                int defaultChannel = getDefaultChannel(chatInfo);

                chatInfo.setDefaultChannel(Services.Chat.TELL_CHANNEL_STRING.hashCode());
                chatService.setTellLink(playerUUID, target.getUniqueId());
                player.chat(msg);

                plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                    chatInfo.setDefaultChannel(defaultChannel);
                    chatService.removeTellLink(playerUUID);
                }, 10L);
                return;
            }

            UUID targetUUID = target.getUniqueId();
            PlayerProfile targetProfile = EterniaLib.getDatabase().get(PlayerProfile.class, targetUUID);
            if (chatService.getTellLink(playerUUID) == targetUUID) {
                chatInfo.setDefaultChannel(plugin.getString(Strings.DEFAULT_CHANNEL).toLowerCase().hashCode());
                chatService.removeTellLink(playerUUID);
                plugin.sendMiniMessages(player, Messages.CHAT_TELL_UNLOCKED, targetProfile.getPlayerName(), targetProfile.getPlayerDisplay());
                return;
            }

            chatService.setTellLink(targetUUID, playerUUID);
            chatInfo.setDefaultChannel(Services.Chat.TELL_CHANNEL_STRING.hashCode());
            plugin.sendMiniMessages(player, Messages.CHAT_TELL_LOCKED, targetProfile.getPlayerName(), targetProfile.getPlayerDisplay());
        }
    }
}
