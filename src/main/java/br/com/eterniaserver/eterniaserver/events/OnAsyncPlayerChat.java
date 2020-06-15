package br.com.eterniaserver.eterniaserver.events;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.filter.ChatFormatter;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.filter.Colors;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.filter.CustomPlaceholdersFilter;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.filter.JsonSender;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.utils.ChatMessage;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.concurrent.TimeUnit;

public class OnAsyncPlayerChat implements Listener {

    private final EterniaServer plugin;
    private final ChatFormatter cf;
    private final JsonSender js;
    private final CustomPlaceholdersFilter cp;
    private final EFiles messages;
    private final Colors c = new Colors();

    public OnAsyncPlayerChat(EterniaServer plugin) {
        this.plugin = plugin;
        this.messages = plugin.getEFiles();
        this.cp = new CustomPlaceholdersFilter(plugin);
        this.js = new JsonSender(plugin);
        this.cf = new ChatFormatter(plugin);
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        if (e.isCancelled()) return;

        final Player player = e.getPlayer();
        final String playerName = player.getName();
        String message = e.getMessage();

        if (plugin.serverConfig.getBoolean("modules.chat")) {
            if (plugin.chatMuted && !player.hasPermission("eternia.mute.bypass")) {
                messages.sendMessage("chat.chatmuted", player);
                e.setCancelled(true);
                return;
            }
            if (EterniaServer.player_muted.get(playerName) - System.currentTimeMillis() > 0) {
                messages.sendMessage("chat.muted", "%time%", TimeUnit.MILLISECONDS.toSeconds(EterniaServer.player_muted.get(playerName) - System.currentTimeMillis()), player);
                e.setCancelled(true);
                return;
            }

            switch (EterniaServer.global.getOrDefault(playerName, 0)) {
                case 0:
                    plugin.local.sendMessage(message, player, plugin.chatConfig.getInt("local.range"));
                    e.setCancelled(true);
                    break;
                case 1:
                    break;
                case 2:
                    plugin.staff.SendMessage(message, player);
                    e.setCancelled(true);
                    break;
            }

            if (e.isCancelled()) return;

            ChatMessage messagex = new ChatMessage(message);
            cf.filter(e, messagex);
            c.filter(e, messagex);
            cp.filter(e, messagex);
            js.filter(e, messagex);
        }
    }

}