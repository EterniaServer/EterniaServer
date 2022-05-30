package br.com.eterniaserver.eterniaserver.modules.chat;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.interfaces.ChatAPI;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.modules.Constants;

import br.com.eterniaserver.eterniaserver.objects.ChannelObject;
import br.com.eterniaserver.eterniaserver.objects.CustomPlaceholder;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;

import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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

    static class CraftChat implements ChatAPI {

        private final Map<UUID, UUID> tellMap = new HashMap<>();
        private final Map<UUID, Long> tellTimeMap = new HashMap<>();
        private final Map<UUID, Long> mutedMap = new HashMap<>();
        private final Set<UUID> spySet = new HashSet<>();

        private final EterniaServer plugin;

        protected CraftChat(final EterniaServer plugin) {
            this.plugin = plugin;
        }

        private boolean muteAllChannels = false;
        private int muteChannelTaskId = 0;
        private TextReplacementConfig replaceText;

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
            tellTimeMap.put(target, System.currentTimeMillis());
        }

        @Override
        public Set<UUID> getSpySet() {
            return spySet;
        }

        @Override
        public void removeTellLink(UUID uuid) {
            tellMap.remove(uuid);
            tellTimeMap.remove(uuid);
        }

        @Override
        public UUID getTellLink(UUID uuid) {
            return tellMap.get(uuid);
        }

        @Override
        public Map<UUID, Long> getTellLinkLTMap() {
            return tellTimeMap;
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
            return mutedMap.containsKey(uuid);
        }

        @Override
        public int secondsMutedLeft(UUID uuid) {
            final long secondsLeft = this.mutedMap.getOrDefault(uuid, 0L);
            return (int) TimeUnit.MILLISECONDS.toSeconds(secondsLeft);
        }

        @Override
        public void mute(UUID uuid, long time) {
            this.mutedMap.put(uuid, time);
        }

    }

    static class ChatService {

        protected final Map<String, CustomPlaceholder> customPlaceholdersObjectsMap = new HashMap<>();
        protected final Map<Integer, ChannelObject> channelObjectsMap = new HashMap<>();
        protected final List<String> channels = new ArrayList<>();
        protected final Map<UUID, Integer> channelsMap = new HashMap<>();

        private final Title.Times mentionTimes = Title.Times.times(Duration.ofMillis(100), Duration.ofSeconds(1), Duration.ofMillis(100));
        private final Map<String, Component> staticComponents = new HashMap<>();
        private final EterniaServer plugin;
        private final int tellChannelHashCode;
        private final int showItemHashCode;

        private int defaultChannel;
        private int discordSRVChannel;

        protected ChatService(final EterniaServer plugin) {
            this.plugin = plugin;
            this.tellChannelHashCode = Constants.TELL_CHANNEL_STRING.hashCode();
            this.showItemHashCode = plugin.getString(Strings.SHOW_ITEM_PLACEHOLDER).hashCode();
        }

        private int defaultChannel() {
            if (this.defaultChannel != 0) {
                return this.defaultChannel;
            }
            this.defaultChannel = plugin.getString(Strings.DEFAULT_CHANNEL).hashCode();
            return this.defaultChannel;
        }

        private int discordSRVChannel() {
            if (this.discordSRVChannel != 0) {
                return this.discordSRVChannel;
            }
            this.discordSRVChannel = plugin.getString(Strings.CHAT_DISCORD_SRV_CHANNEL).hashCode();
            return this.discordSRVChannel;
        }

        private int tellChannel() {
            return tellChannelHashCode;
        }

        protected boolean handleChannel(Component component, Player player) {
            if (!player.hasPermission(plugin.getString(Strings.PERM_CHAT_BYPASS_PROTECTION))) {
                component = component.replaceText(EterniaServer.getChatAPI().getFilter());
            }

            final int channel = channelsMap.getOrDefault(player.getUniqueId(), defaultChannel());
            final UUID uuid = player.getUniqueId();
            final PlayerProfile playerProfile = EterniaServer.getUserAPI().getProfile(uuid);
            if (channel == discordSRVChannel()) {
                this.filter(player, playerProfile, component);
                return false;
            }

            if (channel == tellChannel()) {
                final UUID targetUUID = EterniaServer.getChatAPI().getTellLink(uuid);
                if (targetUUID != null) {
                    final Player target = Bukkit.getPlayer(targetUUID);
                    if (target != null && target.isOnline()) {
                        sendPrivateMessage(player, target, PlainTextComponentSerializer.plainText().serialize(component));
                    }
                }
                return true;
            }

            this.filter(player, playerProfile, component);
            return true;
        }

        public void sendPrivateMessage(Player player, Player target, String msg) {
            final UUID playerUUID = player.getUniqueId();
            final PlayerProfile playerProfile = plugin.userManager().get(playerUUID);
            final UUID targetUUID = target.getUniqueId();
            final PlayerProfile targetProfile = plugin.userManager().get(targetUUID);

            EterniaServer.getChatAPI().setTellLink(playerUUID, targetUUID);
            player.sendMessage(Identity.identity(targetUUID),
                    plugin.getMiniMessage(
                            Messages.CHAT_TELL_TO,
                            false,
                            msg,
                            playerProfile.getName(),
                            playerProfile.getDisplayName(),
                            targetProfile.getName(),
                            targetProfile.getDisplayName()
                    )
            );
            target.sendMessage(Identity.identity(playerUUID),
                    plugin.getMiniMessage(
                            Messages.CHAT_TELL_FROM,
                            false,
                            msg,
                            targetProfile.getName(),
                            targetProfile.getDisplayName(),
                            playerProfile.getName(),
                            playerProfile.getDisplayName()
                    )
            );

            for (UUID spyUUID : EterniaServer.getChatAPI().getSpySet()) {
                final PlayerProfile spyProfile = plugin.userManager().get(spyUUID);
                if (spyProfile.getSpy() && !spyUUID.equals(playerUUID) && !spyUUID.equals(targetUUID)) {
                    final Player spyPlayer = Bukkit.getPlayer(spyUUID);
                    if (spyPlayer != null && spyPlayer.isOnline()) {
                        spyPlayer.sendMessage(Identity.identity(playerUUID), plugin.parseColor(
                                plugin.getString(Strings.CONS_SPY)
                                        .replace("{0}", playerProfile.getName())
                                        .replace("{1}", playerProfile.getDisplayName())
                                        .replace("{2}", targetProfile.getName())
                                        .replace("{3}", targetProfile.getDisplayName())
                                        .replace("{4}", msg)));
                    } else {
                        EterniaServer.getChatAPI().getSpySet().remove(spyUUID);
                    }
                }
            }
        }


        private void filter(Player player, PlayerProfile playerProfile, Component component) {
            ChannelObject channelObject = channelObjectsMap.get(playerProfile.getChatChannel());
            if (channelObject == null) {
                channelObject = channelObjectsMap.get(defaultChannel());
            }

            if (!player.hasPermission(channelObject.getPerm())) {
                plugin.sendMiniMessages(player, Messages.SERVER_NO_PERM);
                return;
            }

            Component messageComponent = getChatComponentFormat(player, channelObject.getFormat());
            final String message = PlainTextComponentSerializer.plainText().serialize(component);

            for (String section : message.split(" ")) {
                messageComponent = messageComponent.append(getComponent(section, player));
            }

            if (!channelObject.isHasRange()) {
                for (Player other : Bukkit.getOnlinePlayers()) {
                    if (other.hasPermission(channelObject.getPerm())) {
                        other.sendMessage(Identity.identity(playerProfile.getUuid()), component);
                    }
                }
                return;
            }

            boolean sendToSomeOne = false;
            World world = player.getWorld();
            Location location = player.getLocation();
            for (Player other : Bukkit.getOnlinePlayers()) {
                if ((world.equals(other.getWorld()) && other.getLocation().distanceSquared(location) <= Math.pow(channelObject.getRange(), 2)) || channelObject.getRange() <= 0) {
                    sendToSomeOne = true;
                    other.sendMessage(Identity.identity(playerProfile.getUuid()), component);
                }
                else if (other.hasPermission(plugin.getString(Strings.PERM_SPY)) && EterniaServer.getUserAPI().isSpying(other.getUniqueId())) {
                    other.sendMessage(playerProfile.getUuid(),
                            plugin.getColor(plugin.getString(Strings.CONS_SPY_LOCAL)
                                    .replace("{0}", playerProfile.getName())
                                    .replace("{1}", playerProfile.getDisplayName())
                                    .replace("{2}", message)
                            )
                    );
                }
            }
            if (!sendToSomeOne) {
                plugin.sendMiniMessages(player, Messages.CHAT_NO_ONE_NEAR);
            }
        }

        private Component getComponent(String section, Player player) {
            final int sectionHashCode = section.hashCode();
            final PlayerProfile mentionPlayerProfile = EterniaServer.getUserAPI().getPlayerProfileFromHashCode(sectionHashCode);

            if (player.hasPermission(plugin.getString(Strings.PERM_CHAT_MENTION)) && mentionPlayerProfile != null) {
                final Player mentionPlayer = Bukkit.getPlayer(mentionPlayerProfile.getUuid());
                if (mentionPlayer != null) {
                    mentionPlayer.playNote(mentionPlayer.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.F));
                    mentionPlayer.showTitle(Title.title(
                            plugin.parseColor(plugin.getString(Strings.CONS_MENTION_TITLE)
                                    .replace("{0}", mentionPlayerProfile.getName())
                                    .replace("{1}", mentionPlayerProfile.getDisplayName())
                            ),
                            plugin.parseColor(plugin.getString(Strings.CONS_MENTION_SUBTITLE)
                                    .replace("{0}", mentionPlayerProfile.getName())
                                    .replace("{1}", mentionPlayerProfile.getDisplayName())
                            ), mentionTimes
                    ));
                }

                // TODO GET FROM DEFAULT
                return Component.text(section).color(TextColor.color(2, 2, 2));
            }

            if (player.hasPermission(plugin.getString(Strings.PERM_CHAT_ITEM)) && sectionHashCode == showItemHashCode) {
                final ItemStack itemStack = player.getInventory().getItemInMainHand();
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
            final Map<Integer, Component> componentMap = new TreeMap<>();
            for (final Map.Entry<String, CustomPlaceholder> entry : customPlaceholdersObjectsMap.entrySet()) {
                if (format.contains("{" + entry.getKey() + "}") && player.hasPermission(entry.getValue().getPermission())) {
                    componentMap.put(entry.getValue().getPriority(), getJsonTagText(player, entry.getValue()));
                }
            }

            Component chatComponentFormat = Component.empty();
            for (Component component : componentMap.values()) {
                chatComponentFormat = chatComponentFormat.append(component);
            }
            return chatComponentFormat;
        }

        private Component getJsonTagText(Player player, CustomPlaceholder object) {
            if (!object.getIsStatic()) {
                return loadComponent(player, object);
            }

            final String value = object.getValue();
            if (!staticComponents.containsKey(value)) {
                return staticComponents.put(value, loadComponent(player, object));
            }

            return staticComponents.get(value);
        }

        private Component loadComponent(Player player, CustomPlaceholder object) {
            Component component = plugin.parseColor(plugin.setPlaceholders(player, object.getValue()));
            if (!object.getHoverText().equals("")) {
                component = component.hoverEvent(HoverEvent.showText(plugin.parseColor(plugin.setPlaceholders(player, object.getHoverText()))));
            }
            if (!object.getSuggestCmd().equals("")) {
                component = component.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, plugin.setPlaceholders(player, object.getSuggestCmd())));
            }

            return component.compact();
        }
    }

}
