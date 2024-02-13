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

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

final class Commands {

    private Commands() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    @CommandAlias("%MUTE")
    static class Mute extends BaseCommand {

        private final Services.CraftChat craftChatService;
        private final EterniaServer plugin;

        public Mute(EterniaServer plugin, Services.CraftChat craftChatService) {
            this.plugin = plugin;
            this.craftChatService = craftChatService;
        }

        @Default
        @CatchUnknown
        @HelpCommand
        @Syntax("%MUTE_SYNTAX")
        @CommandPermission("%MUTE_PERM")
        @Description("%MUTE_DESCRIPTION")
        public void onDefault(CommandHelp help) {
            help.showHelp();
        }

        @Subcommand("%MUTE_CHANNELS")
        @Syntax("%MUTE_CHANNELS_SYNTAX")
        @CommandPermission("%MUTE_CHANNELS_PERM")
        @Description("%MUTE_CHANNELS_DESCRIPTION")
        public void muteChannels(Player sender, @Optional Integer temp) {
            if (craftChatService.isChannelsMute()) {
                plugin.getServer().broadcast(plugin.getMiniMessage(
                        Messages.CHAT_CHANNELS_ENABLED,
                        true
                ));
                craftChatService.unMuteAllChannels();
                return;
            }

            PlayerProfile playerProfile = EterniaLib.getDatabase().get(PlayerProfile.class, sender.getUniqueId());
            if (temp == null) {
                plugin.getServer().broadcast(plugin.getMiniMessage(
                        Messages.CHAT_CHANNELS_DISABLED,
                        true,
                        playerProfile.getPlayerName(),
                        playerProfile.getPlayerDisplay()
                ));
                craftChatService.muteAllChannels();
                return;
            }

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.MINUTE, temp);

            craftChatService.tempMuteAllChannels(cal.getTimeInMillis());

            plugin.getServer().broadcast(plugin.getMiniMessage(
                    Messages.CHAT_CHANNELS_MUTED_TEMP,
                    true,
                    String.valueOf(temp),
                    playerProfile.getPlayerName(),
                    playerProfile.getPlayerDisplay()
            ));
        }

        @CommandCompletion("@players Mensagem")
        @Syntax("%MUTE_PERMA_SYNTAX")
        @Subcommand("%MUTE_PERMA")
        @CommandPermission("%MUTE_PERMA_PERM")
        @Description("%MUTE_PERMA_DESCRIPTION")
        public void onMute(Player player, OnlinePlayer onlinePlayer, String message) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.YEAR, 100);

            Player target = onlinePlayer.getPlayer();

            craftChatService.mute(target.getUniqueId(), cal.getTimeInMillis());

            PlayerProfile playerProfile = EterniaLib.getDatabase().get(PlayerProfile.class, player.getUniqueId());
            PlayerProfile targetProfile = EterniaLib.getDatabase().get(PlayerProfile.class, target.getUniqueId());

            plugin.getServer().broadcast(plugin.getMiniMessage(
                    Messages.CHAT_BROADCAST_MUTE,
                    true,
                    targetProfile.getPlayerName(),
                    targetProfile.getPlayerDisplay(),
                    playerProfile.getPlayerName(),
                    playerProfile.getPlayerDisplay(),
                    message
            ));
        }

        @CommandCompletion("@players")
        @Syntax("%MUTE_UNDO_SYNTAX")
        @Subcommand("%MUTE_UNDO")
        @CommandPermission("%MUTE_UNDO_PERM")
        @Description("%MUTE_UNDO_DESCRIPTION")
        public void onUnMute(Player player, OnlinePlayer onlinePlayer) {
            Player target = onlinePlayer.getPlayer();

            craftChatService.mute(target.getUniqueId(), System.currentTimeMillis());

            PlayerProfile playerProfile = EterniaLib.getDatabase().get(PlayerProfile.class, player.getUniqueId());
            PlayerProfile targetProfile = EterniaLib.getDatabase().get(PlayerProfile.class, target.getUniqueId());

            plugin.getServer().broadcast(plugin.getMiniMessage(
                    Messages.CHAT_BROADCAST_UNMUTE,
                    true,
                    targetProfile.getPlayerName(),
                    targetProfile.getPlayerDisplay(),
                    playerProfile.getPlayerName(),
                    playerProfile.getPlayerDisplay()
            ));
        }

        @CommandCompletion("@players 15 Mensagem")
        @Syntax("%MUTE_TEMP_SYNTAX")
        @Subcommand("%MUTE_TEMP")
        @CommandPermission("%MUTE_TEMP_PERM")
        @Description("%MUTE_TEMP_DESCRIPTION")
        public void onTempMute(Player player, OnlinePlayer onlinePlayer, Integer time, String message) {
            Player target = onlinePlayer.getPlayer();

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.MINUTE, time);

            craftChatService.mute(target.getUniqueId(), cal.getTimeInMillis());

            PlayerProfile playerProfile = EterniaLib.getDatabase().get(PlayerProfile.class, player.getUniqueId());
            PlayerProfile targetProfile = EterniaLib.getDatabase().get(PlayerProfile.class, target.getUniqueId());

            plugin.getServer().broadcast(plugin.getMiniMessage(
                    Messages.CHAT_BROADCAST_TEMP_MUTE,
                    true,
                    targetProfile.getPlayerName(),
                    targetProfile.getPlayerDisplay(),
                    playerProfile.getPlayerName(),
                    playerProfile.getPlayerDisplay(),
                    String.valueOf(time),
                    message
            ));
        }
    }

    static class Generic extends BaseCommand {
        private final EterniaServer plugin;
        private final Services.CraftChat craftChatService;

        public Generic(EterniaServer plugin, Services.CraftChat craftChatService) {
            this.plugin = plugin;
            this.craftChatService = craftChatService;
        }

        @CommandAlias("%NICKNAME")
        @Syntax("%NICKNAME_SYNTAX")
        @CommandPermission("%NICKNAME_PERM")
        @Description("%NICKNAME_DESCRIPTION")
        @CommandCompletion("@players")
        public void onNickname(Player player, @Optional OnlinePlayer onlinePlayer, @Optional String nickname) {
            if (onlinePlayer == null) {
                if (nickname == null) {
                    craftChatService.clearPlayerName(player);
                    plugin.sendMiniMessages(player, Messages.NICKNAME_REMOVED);
                    return;
                }

                String nickNameWithColor = craftChatService.setPlayerDisplay(player, nickname);
                plugin.sendMiniMessages(player, Messages.NICKNAME_UPDATED, nickNameWithColor);
                return;
            }

            Player target = onlinePlayer.getPlayer();

            PlayerProfile playerProfile = EterniaLib.getDatabase().get(PlayerProfile.class, target.getUniqueId());
            PlayerProfile targetProfile = EterniaLib.getDatabase().get(PlayerProfile.class, target.getUniqueId());

            if (nickname == null) {
                craftChatService.clearPlayerName(target);
                plugin.sendMiniMessages(
                        target,
                        Messages.NICKNAME_REMOVED_BY,
                        playerProfile.getPlayerName(),
                        playerProfile.getPlayerDisplay()
                );
                plugin.sendMiniMessages(
                        player,
                        Messages.NICKNAME_REMOVED_FOR,
                        targetProfile.getPlayerName(),
                        targetProfile.getPlayerDisplay()
                );
                return;
            }

            String nickNameWithColor = craftChatService.setPlayerDisplay(target, nickname);
            plugin.sendMiniMessages(
                    target,
                    Messages.NICKNAME_UPDATED_BY,
                    nickNameWithColor,
                    playerProfile.getPlayerName(),
                    playerProfile.getPlayerDisplay()
            );
            plugin.sendMiniMessages(
                    player,
                    Messages.NICKNAME_UPDATED_FOR,
                    nickNameWithColor,
                    targetProfile.getPlayerName(),
                    targetProfile.getPlayerDisplay()
            );
        }
    }

    @CommandAlias("%CHAT")
    static class Chat extends BaseCommand {

        private final EterniaServer plugin;
        private final Services.CraftChat craftChatService;

        public Chat(EterniaServer plugin, Services.CraftChat craftChatService) {
            this.plugin = plugin;
            this.craftChatService = craftChatService;
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

            if (craftChatService.isSpying(uuid)) {
                plugin.sendMiniMessages(player, Messages.CHAT_SPY_DISABLED);
                craftChatService.removeSpying(uuid);
                return;
            }

            plugin.sendMiniMessages(player, Messages.CHAT_SPY_ENABLED);
            craftChatService.setSpying(uuid);
        }

        @Subcommand("%CHAT_REPLY")
        @Syntax("%CHAT_REPLY_SYNTAX")
        @Description("%CHAT_REPLY_DESCRIPTION")
        @CommandPermission("%CHAT_REPLY_PERM")
        @CommandAlias("%CHAT_REPLY_ALIASES")
        public void onReply(Player player, String msg) {
            UUID uuid = player.getUniqueId();
            if (craftChatService.isMuted(uuid)) {
                plugin.sendMiniMessages(
                        player,
                        Messages.CHAT_ARE_MUTED,
                        String.valueOf(craftChatService.secondsMutedLeft(uuid))
                );
                return;
            }

            UUID targetUUID = EterniaServer.getChatAPI().getTellLink(uuid);
            if (targetUUID != null && msg != null) {
                final Player target = plugin.getServer().getPlayer(targetUUID);
                if (target != null && target.isOnline()) {
                    Entities.ChatInfo chatInfo = EterniaLib.getDatabase().get(Entities.ChatInfo.class, uuid);
                    int defaultChannel = getDefaultChannel(chatInfo);

                    chatInfo.setDefaultChannel(Services.CraftChat.TELL_CHANNEL_STRING.hashCode());
                    craftChatService.setTellLink(uuid, targetUUID);
                    player.chat(msg);

                    plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                        chatInfo.setDefaultChannel(defaultChannel);
                        craftChatService.removeTellLink(uuid);
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
            if (craftChatService.isMuted(playerUUID)) {
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

            UUID targetUUID = target.getUniqueId();
            if (msg != null && !msg.isEmpty()) {
                int defaultChannel = getDefaultChannel(chatInfo);

                chatInfo.setDefaultChannel(Services.CraftChat.TELL_CHANNEL_STRING.hashCode());
                craftChatService.setTellLink(targetUUID, playerUUID);
                player.chat(msg);

                plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                    chatInfo.setDefaultChannel(defaultChannel);
                    craftChatService.removeTellLink(playerUUID);
                }, 10L);
                return;
            }

            PlayerProfile targetProfile = EterniaLib.getDatabase().get(PlayerProfile.class, targetUUID);
            if (craftChatService.getTellLink(playerUUID) == targetUUID) {
                chatInfo.setDefaultChannel(plugin.getString(Strings.DEFAULT_CHANNEL).toLowerCase().hashCode());
                craftChatService.removeTellLink(playerUUID);
                plugin.sendMiniMessages(player, Messages.CHAT_TELL_UNLOCKED, targetProfile.getPlayerName(), targetProfile.getPlayerDisplay());
                return;
            }

            craftChatService.setTellLink(targetUUID, playerUUID);
            chatInfo.setDefaultChannel(Services.CraftChat.TELL_CHANNEL_STRING.hashCode());
            plugin.sendMiniMessages(player, Messages.CHAT_TELL_LOCKED, targetProfile.getPlayerName(), targetProfile.getPlayerDisplay());
        }
    }
}
