package br.com.eterniaserver.eterniaserver.modules.chat;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.chat.MessageOptions;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.ItemsKeys;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.modules.chat.Entities.ChatInfo;
import br.com.eterniaserver.eterniaserver.modules.core.Entities;

import github.scarsz.discordsrv.api.ListenerPriority;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.GameChatMessagePreProcessEvent;

import io.papermc.paper.event.player.AsyncChatEvent;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;
import java.util.UUID;

final class Handlers implements Listener {

    private final EterniaServer plugin;
    private final Services.CraftChat craftChat;

    public Handlers(EterniaServer plugin, Services.CraftChat craftChat) {
        this.plugin = plugin;
        this.craftChat = craftChat;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLogin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        Entities.PlayerProfile playerProfile = EterniaLib.getDatabase().get(Entities.PlayerProfile.class, uuid);
        if (playerProfile.getPlayerDisplay() != null) {
            Component nicknameComponent = EterniaLib.getChatCommons().parseColor(playerProfile.getPlayerDisplay());
            player.displayName(nicknameComponent);
        }

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            ChatInfo chatInfo = EterniaLib.getDatabase().get(ChatInfo.class, uuid);
            if (chatInfo.getUuid() == null) {
                chatInfo.setUuid(uuid);
                EterniaLib.getDatabase().insert(ChatInfo.class, chatInfo);
            } else {
                EterniaLib.getDatabase().update(ChatInfo.class, chatInfo);
            }
        });

        craftChat.addHashToUUID(player.getUniqueId(), player.getName());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        craftChat.removeHashToUUID(event.getPlayer().getName());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            String chatGuiName = plugin.getString(Strings.CHAT_GUI_NAME);
            Component title = EterniaServer.getGuiAPI().getTitle(chatGuiName);

            if (event.getView().title() == title) {
                ItemStack currentItem = event.getCurrentItem();
                if (currentItem == null) {
                    return;
                }
                final ItemMeta itemMeta = currentItem.getItemMeta();
                if (itemMeta == null) {
                    return;
                }

                PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
                if (dataContainer.has(plugin.getKey(ItemsKeys.CHAT_COLOR), PersistentDataType.STRING)) {
                    UUID uuid = player.getUniqueId();
                    String chatColor = dataContainer.get(plugin.getKey(ItemsKeys.CHAT_COLOR), PersistentDataType.STRING);
                    if (chatColor != null) {
                        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                            ChatInfo chatInfo = EterniaLib.getDatabase().get(ChatInfo.class, uuid);
                            chatInfo.setChatColor(chatColor);
                            chatInfo.setColor(TextColor.fromHexString(chatColor));
                            EterniaLib.getDatabase().update(ChatInfo.class, chatInfo);
                            MessageOptions options = new MessageOptions(chatColor);
                            EterniaLib.getChatCommons().sendMessage(player, Messages.CHAT_COLOR_UPDATED_TO, options);
                        });
                    }
                }

                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncChat(AsyncChatEvent event) {
        if (event.isCancelled()) {
            return;
        }

        event.setCancelled(craftChat.handleChannel(event));
    }

    static class DiscordSRVHandler {

        private final String discordSrvHoverEventStr;

        public DiscordSRVHandler() {
            this.discordSrvHoverEventStr = PlainTextComponentSerializer.plainText().serialize(
                    Services.CraftChat.DISCORD_SRV_HOVER_EVENT.value()
            );
        }

        @Subscribe(priority = ListenerPriority.HIGHEST)
        public void onGameChatMessagePreProcessEvent(GameChatMessagePreProcessEvent event) {
            if (event.isCancelled()) {
                return;
            }

            boolean cancelEvent = true;
            if (event.getTriggeringBukkitEvent() instanceof AsyncChatEvent chatEvent) {
                Optional<? extends HoverEvent<?>> hover = Optional.ofNullable(chatEvent.message().hoverEvent());

                if (hover.isPresent() && hover.get().value() instanceof Component component) {
                    String hoverStr = PlainTextComponentSerializer.plainText().serialize(component);
                    if (hoverStr.equals(discordSrvHoverEventStr)) {
                        cancelEvent = false;
                    }
                }
            }

            event.setCancelled(cancelEvent);
        }

    }

}
