package br.com.eterniaserver.eterniaserver.modules.chat;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

final class Handlers implements Listener {

    private final Services.Chat chat;

    public Handlers(Services.Chat chat) {
        this.chat = chat;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLogin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        chat.addHashToUUID(player.getUniqueId(), player.getName());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        chat.removeHashToUUID(event.getPlayer().getName());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncChat(AsyncChatEvent event) {
        event.setCancelled(chat.handleChannel(event));
    }
}
