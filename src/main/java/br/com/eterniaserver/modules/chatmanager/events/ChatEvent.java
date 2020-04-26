package br.com.eterniaserver.modules.chatmanager.events;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.modules.chatmanager.chats.Local;

import br.com.eterniaserver.modules.chatmanager.chats.Staff;

import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.event.player.AsyncPlayerChatEvent;


public class ChatEvent {

    public static void onPlayerChat(AsyncPlayerChatEvent event) {
        switch (Vars.global.getOrDefault(event.getPlayer().getName(), 0)) {
            case 0:
                Local.SendMessage(event.getMessage(), event.getPlayer(), EterniaServer.chat.getInt("local.range"));
                event.setCancelled(true);
                break;
            case 1:
                String message = EterniaServer.chat.getString("global.format");
                message = PlaceholderAPI.setPlaceholders(event.getPlayer(), message);
                if (message != null) {
                    event.setFormat(message);
                } else {
                    event.setCancelled(true);
                }
                break;
            case 2:
                Staff.SendMessage(event.getMessage(), event.getPlayer());
                event.setCancelled(true);
                break;
        }
    }

}
