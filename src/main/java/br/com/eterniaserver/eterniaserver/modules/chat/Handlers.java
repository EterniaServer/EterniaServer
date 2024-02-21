package br.com.eterniaserver.eterniaserver.modules.chat;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.modules.chat.Entities.ChatInfo;
import br.com.eterniaserver.eterniaserver.modules.core.Entities;

import github.scarsz.discordsrv.api.ListenerPriority;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.GameChatMessagePreProcessEvent;

import io.papermc.paper.event.player.AsyncChatEvent;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
            Component nicknameComponent = plugin.parseColor(playerProfile.getPlayerDisplay());
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

    @EventHandler(priority = EventPriority.MONITOR)
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

        @Subscribe(priority = ListenerPriority.MONITOR)
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
