package br.com.eterniaserver.eterniaserver.modules.chat;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.interfaces.ChatAPI;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import br.com.eterniaserver.eterniaserver.modules.core.Entities.PlayerProfile;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.chat.ChatType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.Title.Times;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.*;
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

        public Chat(EterniaServer plugin) {
            this.plugin = plugin;
            this.tellChannelHashCode = TELL_CHANNEL_STRING.hashCode();
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

            UUID uuid = player.getUniqueId();
            PlayerProfile playerProfile = EterniaLib.getDatabase().get(PlayerProfile.class, uuid);

            if (channel == discordSRVChannel()) {
                filter(event, playerProfile, chatInfo, component);
                event.viewers().clear();
                return false;
            }

            if (channel == tellChannel()) {
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

        public void sendPrivateMessage(AsyncChatEvent event,
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
                messageComponent = messageComponent.append(spaced).append(getComponent(section, player, playerProfile));
            }

            if (!channelObject.hasRange()) {
                for (Player other : Bukkit.getOnlinePlayers()) {
                    if (other.hasPermission(channelObject.perm())) {
                        other.sendMessage(messageComponent);
                    }
                }
                return;
            }

            boolean sendToSomeOne = false;
            World world = player.getWorld();
            Location location = player.getLocation();
            for (Player other : Bukkit.getOnlinePlayers()) {
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

        private Component getComponent(String section, Player player, PlayerProfile playerProfile) {
            int sectionHashCode = section.toLowerCase().hashCode();
            UUID mentionPlayerUUID = getUUIDFromHash(sectionHashCode);

            if (mentionPlayerUUID != null && player.hasPermission(plugin.getString(Strings.PERM_CHAT_MENTION))) {
                Player mentionPlayer = Bukkit.getPlayer(mentionPlayerUUID);
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

                // TODO GET FROM DEFAULT
                return Component.text(section).color(TextColor.color(2, 2, 2));
            }

            if (player.hasPermission(plugin.getString(Strings.PERM_CHAT_ITEM)) && sectionHashCode == getShowItemHashCode()) {
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                if (!itemStack.getType().equals(Material.AIR)) {
                    return getItemComponent(section, itemStack);
                }
            }

            // TODO GET FROM USER
            return Component.text(section).color(TextColor.color(40, 200, 200));
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
                    Bukkit.getItemFactory().asHoverEvent(
                            itemStack,
                            UnaryOperator.identity()
                    )
            ).color(TextColor.color(3));
            // TODO SET COLOR
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
            Component component = plugin.parseColor(plugin.setPlaceholders(player, object.value()));
            if (!object.hoverText().isEmpty()) {
                component = component.hoverEvent(HoverEvent.showText(plugin.parseColor(plugin.setPlaceholders(player, object.hoverText()))));
            }
            if (!object.suggestCmd().isEmpty()) {
                component = component.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, plugin.setPlaceholders(player, object.suggestCmd())));
            }

            return component.compact();
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
            return false;
        }

        @Override
        public int secondsMutedLeft(UUID uuid) {
            return 0;
        }

        @Override
        public void mute(UUID uuid, long time) {

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
