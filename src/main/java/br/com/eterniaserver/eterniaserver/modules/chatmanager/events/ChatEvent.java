package br.com.eterniaserver.eterniaserver.modules.chatmanager.events;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Vars;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.chats.*;

import org.bukkit.event.player.AsyncPlayerChatEvent;


public class ChatEvent {

    private final EterniaServer plugin;
    private final Local local;
    private final Staff staff;
    private final Vars vars;

    public ChatEvent(EterniaServer plugin, Local local, Staff staff, Vars vars) {
        this.plugin = plugin;
        this.local = local;
        this.staff = staff;
        this.vars = vars;
    }

    public void onPlayerChat(AsyncPlayerChatEvent event) {
        switch (vars.global.getOrDefault(event.getPlayer().getName(), 0)) {
            case 0:
                local.SendMessage(event.getMessage(), event.getPlayer(), plugin.chatConfig.getInt("local.range"));
                event.setCancelled(true);
                break;
            case 1:
                break;
            case 2:
                staff.SendMessage(event.getMessage(), event.getPlayer());
                event.setCancelled(true);
                break;
        }
    }

}
