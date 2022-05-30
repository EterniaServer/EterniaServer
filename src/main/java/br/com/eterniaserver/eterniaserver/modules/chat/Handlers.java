package br.com.eterniaserver.eterniaserver.modules.chat;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

final class Handlers implements Listener {

    private final EterniaServer plugin;
    private final Services.ChatService chatService;

    public Handlers(final EterniaServer plugin, final Services.ChatService chatService) {
        this.chatService = chatService;
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncChat(AsyncChatEvent event) {
        final Player player = event.getPlayer();
        if (!player.hasPermission(plugin.getString(Strings.PERM_MUTE_BYPASS))) {
            if (EterniaServer.getChatAPI().isChannelsMute()) {
                plugin.sendMiniMessages(player, Messages.CHAT_ALL_CHANNELS_ARE_MUTE);
                event.setCancelled(true);
                return;
            }
            if (EterniaServer.getChatAPI().isMuted(player.getUniqueId())) {
                final String secondsLeft = String.valueOf(EterniaServer.getChatAPI().secondsMutedLeft(player.getUniqueId()));
                plugin.sendMiniMessages(player, Messages.CHAT_ARE_MUTED, secondsLeft);
                event.setCancelled(true);
                return;
            }
        }

        event.viewers().clear();
        event.setCancelled(chatService.handleChannel(event.message(), player));
    }
}
