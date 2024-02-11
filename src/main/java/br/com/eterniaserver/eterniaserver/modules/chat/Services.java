package br.com.eterniaserver.eterniaserver.modules.chat;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.interfaces.ChatAPI;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import br.com.eterniaserver.eterniaserver.modules.core.Entities.PlayerProfile;

import io.papermc.paper.event.player.AsyncChatEvent;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.Title.Times;

import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.Timestamp;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

final class Services {

    private Services() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    static class Chat implements ChatAPI {
        protected final Map<String, Utils.CustomPlaceholder> customPlaceholdersObjectsMap = new HashMap<>();
        protected final Map<Integer, Utils.ChannelObject> channelObjectsMap = new HashMap<>();
        protected final List<String> channels = new ArrayList<>();

        protected static final String TELL_CHANNEL_STRING = "tellchannel";

        private final static String NICKNAME_COLOR_REGEX = "[^\\w#<>]";
        private final static String NICKNAME_CLEAR_REGEX = "<#[a-f\\\\d]{6}>";

        private final Map<UUID, UUID> tellMap = new HashMap<>();
        private final Map<Integer, UUID> playerHashToUUID = new HashMap<>();
        private final Map<String, Component> staticComponents = new HashMap<>();
        private final Times mentionTimes = Times.times(Duration.ofMillis(100), Duration.ofSeconds(1), Duration.ofMillis(100));
        private final Set<UUID> spySet = new HashSet<>();

        private final EterniaServer plugin;
        private final int tellChannelHashCode;

        private int showItemHashCode;
        private int defaultChannel;
        private int discordSRVChannel;
        private TextReplacementConfig replaceText;
        private boolean muteAllChannels = false;
        private int muteChannelTaskId = 0;
        private TextColor tagColor;
        private TextColor playerColor;

        public Chat(EterniaServer plugin) {
            this.plugin = plugin;
            this.tellChannelHashCode = TELL_CHANNEL_STRING.hashCode();
        }

        protected void updateTextColor() {
            String tagHex = plugin.getString(Strings.CHAT_DEFAULT_TAG_COLOR);
            String playerHex = plugin.getString(Strings.CHAT_DEFAULT_PLAYER_COLOR);

            this.tagColor = TextColor.fromHexString(tagHex);
            this.playerColor = TextColor.fromHexString(playerHex);
        }

        protected TextColor getPlayerDefaultColor(Entities.ChatInfo chatInfo) {
            TextColor color = chatInfo.getColor();
            if (color == null) {
                return playerColor;
            }

            return color;
        }

        protected void addHashToUUID(UUID uuid, String name) {
            playerHashToUUID.put(name.toLowerCase().hashCode(), uuid);
        }

        protected void removeHashToUUID(String name) {
            playerHashToUUID.remove(name.toLowerCase().hashCode());
        }

        private UUID getUUIDFromHash(int hash) {
            return playerHashToUUID.get(hash);
        }

        private int defaultChannel() {
            if (this.defaultChannel != 0) {
                return this.defaultChannel;
            }
            this.defaultChannel = plugin.getString(Strings.DEFAULT_CHANNEL).toLowerCase().hashCode();
            return this.defaultChannel;
        }

        private int getShowItemHashCode() {
            if (showItemHashCode != 0) {
                return showItemHashCode;
            }
            this.showItemHashCode = plugin.getString(Strings.SHOW_ITEM_PLACEHOLDER).toLowerCase().hashCode();
            return showItemHashCode;
        }

        private int discordSRVChannel() {
            if (this.discordSRVChannel != 0) {
                return this.discordSRVChannel;
            }
            this.discordSRVChannel = plugin.getString(Strings.CHAT_DISCORD_SRV_CHANNEL).toLowerCase().hashCode();
            return this.discordSRVChannel;
        }

        private int tellChannel() {
            return tellChannelHashCode;
        }

        protected boolean handleChannel(AsyncChatEvent event) {
            Player player = event.getPlayer();
            Component component = event.message();

            if (!player.hasPermission(plugin.getString(Strings.PERM_CHAT_BYPASS_PROTECTION))) {
                component = component.replaceText(getFilter());
            }

            Entities.ChatInfo chatInfo = EterniaLib.getDatabase().get(Entities.ChatInfo.class, player.getUniqueId());
            Integer channel = chatInfo.getDefaultChannel();
            if (channel == null) {
                channel = defaultChannel();
            }

            boolean isTellChannel = channel == tellChannel();
            boolean isDiscordSRVChannel = channel == discordSRVChannel();

            if (muteAllChannels && !isTellChannel) {
                plugin.sendMiniMessages(player, Messages.CHAT_CHANNELS_MUTED);
                return true;
            }

            UUID uuid = player.getUniqueId();

            if (isMuted(chatInfo)) {
                plugin.sendMiniMessages(player, Messages.CHAT_ARE_MUTED, String.valueOf(secondsMutedLeft(uuid)));
                return true;
            }

            PlayerProfile playerProfile = EterniaLib.getDatabase().get(PlayerProfile.class, uuid);

            if (isDiscordSRVChannel) {
                filter(event, playerProfile, chatInfo, component);
                event.viewers().clear();
                return false;
            }

            if (isTellChannel) {
                UUID targetUUID = getTellLink(uuid);
                if (targetUUID == null) {
                    removeTellLink(uuid);
                    plugin.sendMiniMessages(player, Messages.CHAT_TELL_NO_PLAYER);
                    return true;
                }

                Player target = plugin.getServer().getPlayer(targetUUID);
                if (target == null || !target.isOnline()) {
                    removeTellLink(uuid);
                    plugin.sendMiniMessages(player, Messages.CHAT_TELL_NO_PLAYER);
                    return true;
                }

                sendPrivateMessage(event, component, playerProfile, target);
                return true;
            }

            filter(event, playerProfile, chatInfo, component);
            return true;
        }

        protected void sendPrivateMessage(AsyncChatEvent event,
                                       Component component,
                                       PlayerProfile playerProfile,
                                       Player target) {

            Player player = event.getPlayer();

            UUID playerUUID = player.getUniqueId();
            UUID targetUUID = target.getUniqueId();

            EterniaServer.getChatAPI().setTellLink(playerUUID, targetUUID);

            PlayerProfile targetProfile = EterniaLib.getDatabase().get(PlayerProfile.class, targetUUID);

            String msg = PlainTextComponentSerializer.plainText().serialize(component);
            Component msgComponent = plugin.getMiniMessage(
                    Messages.CHAT_TELL,
                    false,
                    msg,
                    playerProfile.getPlayerName(),
                    playerProfile.getPlayerDisplay(),
                    targetProfile.getPlayerName(),
                    targetProfile.getPlayerDisplay()
            );

            event.message(msgComponent);

            target.sendMessage(msgComponent);
            player.sendMessage(msgComponent);

            Component spyMsgComponent = plugin.getMiniMessage(
                    Messages.CHAT_SPY_TELL,
                    false,
                    msg,
                    playerProfile.getPlayerName(),
                    playerProfile.getPlayerDisplay(),
                    targetProfile.getPlayerName(),
                    targetProfile.getPlayerDisplay()
            );

            for (Player other : plugin.getServer().getOnlinePlayers()) {
                if (other.hasPermission(plugin.getString(Strings.PERM_SPY)) || isSpying(other.getUniqueId())) {
                    if (other.getUniqueId().equals(playerUUID) || other.getUniqueId().equals(targetUUID)) {
                        continue;
                    }

                    other.sendMessage(spyMsgComponent);
                }
            }
        }

        private void filter(AsyncChatEvent event, PlayerProfile playerProfile, Entities.ChatInfo chatInfo, Component component) {
            Player player = event.getPlayer();

            Utils.ChannelObject channelObject = channelObjectsMap.get(chatInfo.getDefaultChannel());
            if (channelObject == null) {
                channelObject = channelObjectsMap.get(defaultChannel());
            }

            if (!player.hasPermission(channelObject.perm())) {
                plugin.sendMiniMessages(player, Messages.SERVER_NO_PERM);
                return;
            }

            Component messageComponent = getChatComponentFormat(player, channelObject.format());
            String message = PlainTextComponentSerializer.plainText().serialize(component);

            Component spaced = Component.text(" ");
            for (String section : message.split(" ")) {
                messageComponent = messageComponent.append(spaced).append(getComponent(section, player, playerProfile, chatInfo));
            }

            if (!channelObject.hasRange()) {
                for (Player other : plugin.getServer().getOnlinePlayers()) {
                    if (other.hasPermission(channelObject.perm())) {
                        other.sendMessage(messageComponent);
                    }
                }
                return;
            }

            boolean sendToSomeOne = false;
            World world = player.getWorld();
            Location location = player.getLocation();
            for (Player other : plugin.getServer().getOnlinePlayers()) {
                if (channelObject.range() <= 0 || (world.equals(other.getWorld()) && other.getLocation().distanceSquared(location) <= Math.pow(channelObject.range(), 2))) {
                    sendToSomeOne = true;
                    other.sendMessage(messageComponent);
                }
                else if (other.hasPermission(plugin.getString(Strings.PERM_SPY)) && isSpying(other.getUniqueId())) {
                    plugin.sendMiniMessages(other, Messages.CHAT_SPY_LOCAL, playerProfile.getPlayerName(), playerProfile.getPlayerDisplay());
                }
            }

            if (!sendToSomeOne) {
                plugin.sendMiniMessages(player, Messages.CHAT_NO_ONE_NEAR);
            }
        }

        private Component getComponent(String section, Player player, PlayerProfile playerProfile, Entities.ChatInfo chatInfo) {
            int sectionHashCode = section.toLowerCase().hashCode();
            UUID mentionPlayerUUID = getUUIDFromHash(sectionHashCode);

            if (mentionPlayerUUID != null && player.hasPermission(plugin.getString(Strings.PERM_CHAT_MENTION))) {
                Player mentionPlayer = plugin.getServer().getPlayer(mentionPlayerUUID);
                if (mentionPlayer != null) {
                    mentionPlayer.playNote(mentionPlayer.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.F));
                    mentionPlayer.showTitle(Title.title(
                            plugin.parseColor(plugin.getString(Strings.CONS_MENTION_TITLE)
                                    .replace("{0}", playerProfile.getPlayerName())
                                    .replace("{1}", playerProfile.getPlayerDisplay())
                            ),
                            plugin.parseColor(plugin.getString(Strings.CONS_MENTION_SUBTITLE)
                                    .replace("{0}", playerProfile.getPlayerName())
                                    .replace("{1}", playerProfile.getPlayerDisplay())
                            ), mentionTimes
                    ));
                }

                return Component.text(section).color(tagColor);
            }

            if (player.hasPermission(plugin.getString(Strings.PERM_CHAT_ITEM)) && sectionHashCode == getShowItemHashCode()) {
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                if (!itemStack.getType().equals(Material.AIR)) {
                    return getItemComponent(section, itemStack);
                }
            }

            return Component.text(section).color(getPlayerDefaultColor(chatInfo));
        }

        private	Component getItemComponent(String string, ItemStack itemStack) {
            final Component component = Component.text(
                    string.replace(
                            plugin.getString(Strings.SHOW_ITEM_PLACEHOLDER),
                            plugin.getString(Strings.CONS_SHOW_ITEM)
                                    .replace("{0}", String.valueOf(itemStack.getAmount()))
                                    .replace("{1}", itemStack.getType().toString())
                    )
            );
            return component.hoverEvent(
                    plugin.getServer().getItemFactory().asHoverEvent(
                            itemStack,
                            UnaryOperator.identity()
                    )
            ).color(tagColor);
        }

        private Component getChatComponentFormat(Player player, String format) {
            Map<Integer, Component> componentMap = new TreeMap<>();
            for (Map.Entry<String, Utils.CustomPlaceholder> entry : customPlaceholdersObjectsMap.entrySet()) {
                if (format.contains("{" + entry.getKey() + "}") && player.hasPermission(entry.getValue().permission())) {
                    componentMap.put(entry.getValue().priority(), getJsonTagText(player, entry.getValue()));
                }
            }

            Component chatComponentFormat = Component.empty();
            for (Component component : componentMap.values()) {
                chatComponentFormat = chatComponentFormat.append(component);
            }

            return chatComponentFormat;
        }

        private Component getJsonTagText(Player player, Utils.CustomPlaceholder object) {
            if (!object.isStatic()) {
                return loadComponent(player, object);
            }

            String value = object.value();
            if (!staticComponents.containsKey(value)) {
                staticComponents.put(value, loadComponent(player, object));
            }

            return staticComponents.get(value);
        }

        private Component loadComponent(Player player, Utils.CustomPlaceholder object) {
            Component component = object.value().equals("%player_displayname%") ?
                    player.displayName() :
                    plugin.parseColor(plugin.setPlaceholders(player, object.value()));

            if (!object.hoverText().isEmpty()) {
                component = component.hoverEvent(HoverEvent.showText(plugin.parseColor(plugin.setPlaceholders(player, object.hoverText()))));
            }
            if (!object.suggestCmd().isEmpty()) {
                component = component.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, plugin.setPlaceholders(player, object.suggestCmd())));
            }

            return component.compact();
        }

        protected void clearPlayerName(Player player) {
            PlayerProfile playerProfile = EterniaLib.getDatabase().get(PlayerProfile.class, player.getUniqueId());

            playerProfile.setPlayerDisplay(player.getName());
            playerProfile.setPlayerDisplayColor(null);

            player.displayName(Component.text(player.getName()));

            EterniaLib.getDatabase().update(PlayerProfile.class, playerProfile);
        }

        protected String setPlayerDisplay(Player player, String nickname) {
            PlayerProfile playerProfile = EterniaLib.getDatabase().get(PlayerProfile.class, player.getUniqueId());

            String nicknameWithColor = nickname.replaceAll(NICKNAME_COLOR_REGEX, "");
            String nicknameClear = nicknameWithColor.replaceAll(NICKNAME_CLEAR_REGEX, "");

            Component nicknameComponent = plugin.parseColor(nicknameWithColor);

            playerProfile.setPlayerDisplay(nicknameClear);
            playerProfile.setPlayerDisplayColor(nicknameWithColor);

            player.displayName(nicknameComponent);

            EterniaLib.getDatabase().update(PlayerProfile.class, playerProfile);

            return nicknameWithColor;
        }

        @Override
        public TextReplacementConfig getFilter() {
            return replaceText;
        }

        @Override
        public void setFilter(Pattern filter) {
            this.replaceText = TextReplacementConfig.builder().match(filter).replacement("").build();
        }

        @Override
        public void setTellLink(UUID sender, UUID target) {
            tellMap.put(target, sender);
        }

        @Override
        public void removeTellLink(UUID uuid) {
            tellMap.remove(uuid);
        }

        @Override
        public UUID getTellLink(UUID uuid) {
            return tellMap.get(uuid);
        }

        @Override
        public void tempMuteAllChannels(long time) {
            this.muteChannelTaskId = plugin.getServer().getScheduler().scheduleSyncDelayedTask(
                    plugin, () -> this.muteAllChannels = false, time
            );
            this.muteAllChannels = true;
        }

        @Override
        public void muteAllChannels() {
            this.muteAllChannels = true;
        }

        @Override
        public void unMuteAllChannels() {
            this.muteAllChannels = false;
            if (muteChannelTaskId != 0) {
                plugin.getServer().getScheduler().cancelTask(muteChannelTaskId);
                muteChannelTaskId = 0;
            }
        }

        @Override
        public boolean isChannelsMute() {
            return muteAllChannels;
        }

        @Override
        public boolean isMuted(UUID uuid) {
            Entities.ChatInfo chatInfo = EterniaLib.getDatabase().get(Entities.ChatInfo.class, uuid);
            return isMuted(chatInfo);
        }

        private boolean isMuted(Entities.ChatInfo chatInfo) {
            Timestamp mutedUntil = chatInfo.getMutedUntil();
            if (mutedUntil == null) {
                return false;
            }

            return mutedUntil.after(new Timestamp(System.currentTimeMillis()));
        }

        @Override
        public int secondsMutedLeft(UUID uuid) {
            Entities.ChatInfo chatInfo = EterniaLib.getDatabase().get(Entities.ChatInfo.class, uuid);
            return secondsMutedLeft(chatInfo);
        }

        private int secondsMutedLeft(Entities.ChatInfo chatInfo) {
            Timestamp mutedUntil = chatInfo.getMutedUntil();
            if (mutedUntil == null) {
                return 0;
            }

            return (int) TimeUnit.MILLISECONDS.toSeconds(mutedUntil.getTime() - System.currentTimeMillis());
        }

        @Override
        public void mute(UUID uuid, long time) {
            Entities.ChatInfo chatInfo = EterniaLib.getDatabase().get(Entities.ChatInfo.class, uuid);

            chatInfo.setMutedUntil(new Timestamp(time));

            EterniaLib.getDatabase().update(Entities.ChatInfo.class, chatInfo);
        }

        @Override
        public boolean isSpying(UUID uuid) {
            return spySet.contains(uuid);
        }

        @Override
        public void setSpying(UUID uuid) {
            spySet.add(uuid);
        }

        @Override
        public void removeSpying(UUID uuid) {
            spySet.remove(uuid);
        }
    }

}
