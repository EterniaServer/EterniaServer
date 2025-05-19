package br.com.eterniaserver.eterniaserver.modules.chat;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.chat.MessageOptions;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.interfaces.ChatAPI;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import br.com.eterniaserver.eterniaserver.modules.core.Entities.PlayerProfile;
import br.com.eterniaserver.eterniaserver.modules.chat.Entities.ChatInfo;
import br.com.eterniaserver.eterniaserver.modules.chat.Utils.ChannelObject;
import br.com.eterniaserver.eterniaserver.modules.chat.Utils.CustomPlaceholder;

import io.papermc.paper.event.player.AsyncChatEvent;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
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
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

final class Services {

    private Services() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    static class CraftChat implements ChatAPI {

        protected static final HoverEvent<Component> DISCORD_SRV_HOVER_EVENT = HoverEvent.showText(Component.text("discordsrv"));

        protected final Map<String, CustomPlaceholder> customPlaceholdersObjectsMap = new HashMap<>();
        protected final Map<Integer, ChannelObject> channelObjectsMap = new HashMap<>();
        protected final List<String> channels = new ArrayList<>();

        protected static final String TELL_CHANNEL_STRING = "tellchannel";

        private final static String NICKNAME_COLOR_REGEX = "[^\\w#<>]";
        private final static String NICKNAME_CLEAR_REGEX = "<#[a-f\\\\d]{6}>";

        private final Map<UUID, UUID> tellMap = new ConcurrentHashMap<>();
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

        public CraftChat(EterniaServer plugin) {
            this.plugin = plugin;
            this.tellChannelHashCode = TELL_CHANNEL_STRING.hashCode();
        }

        protected void updateTextColor() {
            String tagHex = plugin.getString(Strings.CHAT_DEFAULT_TAG_COLOR);

            this.tagColor = TextColor.fromHexString(tagHex);
        }

        protected TextColor getPlayerDefaultColor(ChatInfo chatInfo, ChannelObject channelObject) {
            TextColor color = chatInfo.getColor();
            if (color == null) {
                return TextColor.fromHexString(channelObject.channelColor());
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

            ChatInfo chatInfo = EterniaLib.getDatabase().get(ChatInfo.class, player.getUniqueId());
            Integer channel = chatInfo.getDefaultChannel();
            if (channel == null) {
                channel = defaultChannel();
            }

            boolean isTellChannel = channel == tellChannel();
            boolean isDiscordSRVChannel = channel == discordSRVChannel();

            if (muteAllChannels && !isTellChannel) {
                EterniaLib.getChatCommons().sendMessage(player, Messages.CHAT_CHANNELS_MUTED);
                return true;
            }

            UUID uuid = player.getUniqueId();

            if (isMuted(chatInfo)) {
                MessageOptions messageOptions = new MessageOptions(String.valueOf(secondsMutedLeft(uuid)));
                EterniaLib.getChatCommons().sendMessage(player, Messages.CHAT_ARE_MUTED, messageOptions);
                return true;
            }

            PlayerProfile playerProfile = EterniaLib.getDatabase().get(PlayerProfile.class, uuid);

            if (isDiscordSRVChannel) {
                event.message(component.hoverEvent(DISCORD_SRV_HOVER_EVENT));
            }

            if (isTellChannel) {
                UUID targetUUID = getTellLink(uuid);
                if (targetUUID == null) {
                    removeTellLink(uuid);
                    EterniaLib.getChatCommons().sendMessage(player, Messages.CHAT_TELL_NO_PLAYER);
                    return true;
                }

                Player target = plugin.getServer().getPlayer(targetUUID);
                if (target == null || !target.isOnline()) {
                    removeTellLink(uuid);
                    EterniaLib.getChatCommons().sendMessage(player, Messages.CHAT_TELL_NO_PLAYER);
                    return true;
                }

                sendPrivateMessage(event, component, playerProfile, target);
                return false;
            }

            filter(event, playerProfile, chatInfo, component);
            return false;
        }

        protected void sendPrivateMessage(AsyncChatEvent event,
                                       Component component,
                                       PlayerProfile playerProfile,
                                       Player target) {

            Player player = event.getPlayer();
            Set<Audience> viewers = event.viewers();

            UUID playerUUID = player.getUniqueId();
            UUID targetUUID = target.getUniqueId();

            EterniaServer.getChatAPI().setTellLink(playerUUID, targetUUID);

            PlayerProfile targetProfile = EterniaLib.getDatabase().get(PlayerProfile.class, targetUUID);

            viewers.clear();
            viewers.add(player);
            viewers.add(target);

            String msg = PlainTextComponentSerializer.plainText().serialize(component);

            MessageOptions msgOptions = new MessageOptions(
                    false,
                    msg,
                    playerProfile.getPlayerName(),
                    playerProfile.getPlayerDisplay(),
                    targetProfile.getPlayerName(),
                    targetProfile.getPlayerDisplay()
            );
            Component spyMsgComponent = EterniaLib.getChatCommons().parseMessage(Messages.CHAT_SPY_TELL, msgOptions);

            String spyPerm = plugin.getString(Strings.PERM_SPY);
            for (Player other : plugin.getServer().getOnlinePlayers()) {
                boolean inTell = other.getUniqueId().equals(playerUUID) || other.getUniqueId().equals(targetUUID);
                if (!inTell && (other.hasPermission(spyPerm) || isSpying(other.getUniqueId()))) {
                    other.sendMessage(spyMsgComponent);
                }
            }

            Component msgComponent = EterniaLib.getChatCommons().parseMessage(Messages.CHAT_TELL, msgOptions);

            event.renderer((source, sourceDisplayName, message, viewer) -> {
                Optional<UUID> viewerUUID = viewer.get(Identity.UUID);
                if (viewerUUID.isEmpty()) {
                    return message;
                }

                return msgComponent;
            });
        }

        private void filter(AsyncChatEvent event, PlayerProfile playerProfile, ChatInfo chatInfo, Component component) {
            Player player = event.getPlayer();

            ChannelObject channelObject = channelObjectsMap.get(chatInfo.getDefaultChannel());
            if (channelObject == null) {
                channelObject = channelObjectsMap.get(defaultChannel());
            }

            if (!player.hasPermission(channelObject.perm())) {
                EterniaLib.getChatCommons().sendMessage(player, Messages.SERVER_NO_PERM);
                return;
            }

            Component messageComponent = getChatComponentFormat(player, channelObject.format());
            String messageStr = PlainTextComponentSerializer.plainText().serialize(component);

            for (String section : messageStr.split(" ")) {
                messageComponent = messageComponent.appendSpace().append(getComponent(
                        section, player, playerProfile, chatInfo, channelObject
                ));
            }

            Set<Audience> viewers = event.viewers();

            World world = player.getWorld();
            Location location = player.getLocation();
            String spyPerm = plugin.getString(Strings.PERM_SPY);

            for (Player receiver : plugin.getServer().getOnlinePlayers()) {
                boolean hasPermission = receiver.hasPermission(channelObject.perm());
                if (!channelObject.hasRange() && !hasPermission) {
                    viewers.remove(receiver);
                }
                else if (hasPermission) {
                    int range = channelObject.range();
                    boolean isInRange = range <= 0 || (
                            world.equals(receiver.getWorld())
                                    &&
                            receiver.getLocation().distanceSquared(location) <= Math.pow(range, 2)
                    );

                    if (!isInRange) {
                        viewers.remove(receiver);
                    }
                    if (!isInRange && receiver.hasPermission(spyPerm) && isSpying(receiver.getUniqueId())) {
                        MessageOptions msgOptions = new MessageOptions(
                                messageStr,
                                playerProfile.getPlayerName(),
                                playerProfile.getPlayerDisplay()
                        );
                        EterniaLib.getChatCommons().sendMessage(receiver, Messages.CHAT_SPY_LOCAL, msgOptions);
                    }
                }
            }

            Component sourceMessage = messageComponent.compact();

            event.renderer((source, sourceDisplayName, message, viewer) -> {
                Optional<UUID> viewerUUID = viewer.get(Identity.UUID);
                if (viewerUUID.isEmpty()) {
                    return message;
                }

                return sourceMessage;
            });

            if (viewers.isEmpty()) {
                EterniaLib.getChatCommons().sendMessage(player, Messages.CHAT_NO_ONE_NEAR);
            }
        }

        private Component getComponent(String section,
                                       Player player,
                                       PlayerProfile playerProfile,
                                       ChatInfo chatInfo,
                                       ChannelObject channelObject) {
            int sectionHashCode = section.toLowerCase().hashCode();
            UUID mentionPlayerUUID = getUUIDFromHash(sectionHashCode);

            if (mentionPlayerUUID != null && player.hasPermission(plugin.getString(Strings.PERM_CHAT_MENTION))) {
                Player mentionPlayer = plugin.getServer().getPlayer(mentionPlayerUUID);
                if (mentionPlayer != null) {
                    mentionPlayer.playNote(mentionPlayer.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.F));
                    Component title = EterniaLib.getChatCommons().parseColor(
                            plugin.getString(Strings.CONS_MENTION_TITLE)
                                    .replace("{0}", playerProfile.getPlayerName())
                                    .replace("{1}", playerProfile.getPlayerDisplay())
                    );
                    Component subtitle = EterniaLib.getChatCommons().parseColor(
                            plugin.getString(Strings.CONS_MENTION_SUBTITLE)
                                    .replace("{0}", playerProfile.getPlayerName())
                                    .replace("{1}", playerProfile.getPlayerDisplay())
                    );

                    mentionPlayer.showTitle(Title.title(title, subtitle, mentionTimes));
                }

                return Component.text(section).color(tagColor);
            }

            if (player.hasPermission(plugin.getString(Strings.PERM_CHAT_ITEM)) && sectionHashCode == getShowItemHashCode()) {
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                if (!itemStack.getType().equals(Material.AIR)) {
                    return getItemComponent(section, itemStack);
                }
            }

            return Component.text(section).color(getPlayerDefaultColor(chatInfo, channelObject));
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
            for (Map.Entry<String, CustomPlaceholder> entry : customPlaceholdersObjectsMap.entrySet()) {
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

        private Component getJsonTagText(Player player, CustomPlaceholder object) {
            if (!object.isStatic()) {
                return loadComponent(player, object);
            }

            String value = object.value();
            if (!staticComponents.containsKey(value)) {
                staticComponents.put(value, loadComponent(player, object));
            }

            return staticComponents.get(value);
        }

        private Component loadComponent(Player player, CustomPlaceholder object) {
            Component component = object.value().equals("%player_displayname%") ?
                    player.displayName() :
                    EterniaLib.getChatCommons().parseColor(plugin.setPlaceholders(player, object.value()));

            if (!object.hoverText().isEmpty()) {
                component = component.hoverEvent(HoverEvent.showText(EterniaLib.getChatCommons().parseColor(plugin.setPlaceholders(player, object.hoverText()))));
            }
            if (!object.suggestCmd().isEmpty()) {
                component = component.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, plugin.setPlaceholders(player, object.suggestCmd())));
            }

            return component.compact();
        }

        protected void clearPlayerName(Player player) {
            PlayerProfile playerProfile = EterniaLib.getDatabase().get(PlayerProfile.class, player.getUniqueId());

            playerProfile.setPlayerDisplay(player.getName());

            player.displayName(Component.text(player.getName()));

            EterniaLib.getDatabase().update(PlayerProfile.class, playerProfile);
        }

        protected String setPlayerDisplay(Player player, String nickname) {
            PlayerProfile playerProfile = EterniaLib.getDatabase().get(PlayerProfile.class, player.getUniqueId());

            String nicknameWithColor = nickname.replaceAll(NICKNAME_COLOR_REGEX, "");
            String nicknameClear = nicknameWithColor.replaceAll(NICKNAME_CLEAR_REGEX, "");

            Component nicknameComponent = EterniaLib.getChatCommons().parseColor(nicknameWithColor);

            playerProfile.setPlayerDisplay(nicknameClear);

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
            ChatInfo chatInfo = EterniaLib.getDatabase().get(ChatInfo.class, uuid);
            return isMuted(chatInfo);
        }

        private boolean isMuted(ChatInfo chatInfo) {
            Timestamp mutedUntil = chatInfo.getMutedUntil();
            if (mutedUntil == null) {
                return false;
            }

            return mutedUntil.after(new Timestamp(System.currentTimeMillis()));
        }

        @Override
        public int secondsMutedLeft(UUID uuid) {
            ChatInfo chatInfo = EterniaLib.getDatabase().get(ChatInfo.class, uuid);
            return secondsMutedLeft(chatInfo);
        }

        private int secondsMutedLeft(ChatInfo chatInfo) {
            Timestamp mutedUntil = chatInfo.getMutedUntil();
            if (mutedUntil == null) {
                return 0;
            }

            return (int) TimeUnit.MILLISECONDS.toSeconds(mutedUntil.getTime() - System.currentTimeMillis());
        }

        @Override
        public void mute(UUID uuid, long time) {
            ChatInfo chatInfo = EterniaLib.getDatabase().get(ChatInfo.class, uuid);

            chatInfo.setMutedUntil(new Timestamp(time));

            EterniaLib.getDatabase().update(ChatInfo.class, chatInfo);
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
