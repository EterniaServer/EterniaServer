package br.com.eterniaserver.eterniaserver.modules.chat;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.modules.core.Entities;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

final class Handlers implements Listener {

    private final EterniaServer plugin;
    private final Services.Chat chat;

    public Handlers(EterniaServer plugin, Services.Chat chat) {
        this.plugin = plugin;
        this.chat = chat;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLogin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        Entities.PlayerProfile playerProfile = EterniaLib.getDatabase().get(Entities.PlayerProfile.class, uuid);
        if (playerProfile.getPlayerDisplay() != null) {
            Component nicknameComponent = plugin.parseColor(playerProfile.getPlayerDisplayColor());
            player.displayName(nicknameComponent);
        }

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
