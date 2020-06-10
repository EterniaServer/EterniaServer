package br.com.eterniaserver.eterniaserver.events;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Vars;

import br.com.eterniaserver.eterniaserver.configs.Checks;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.filter.ChatFormatter;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.filter.Colors;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.filter.CustomPlaceholdersFilter;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.filter.JsonSender;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.utils.ChatMessage;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.chats.Local;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.chats.Staff;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.concurrent.TimeUnit;

public class OnPlayerChat implements Listener {

    private final EterniaServer plugin;
    private final Local local;
    private final Staff staff;
    private final ChatFormatter cf;
    private final JsonSender js;
    private final Vars vars;
    private final CustomPlaceholdersFilter cp;
    private final Messages messages;
    private final Colors c = new Colors();

    public OnPlayerChat(EterniaServer plugin, Checks checks, Vars vars, Messages messages, Local local, Staff staff) {
        this.plugin = plugin;
        this.vars = vars;
        this.local = local;
        this.staff = staff;
        this.cp = new CustomPlaceholdersFilter(vars);
        this.js = new JsonSender(plugin, vars);
        this.cf = new ChatFormatter(plugin, checks, vars);
        this.messages = messages;
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
            if (vars.player_muted.get(playerName) - System.currentTimeMillis() > 0) {
                messages.sendMessage("chat.muted", "%time%", TimeUnit.MILLISECONDS.toSeconds(vars.player_muted.get(playerName) - System.currentTimeMillis()), player);
                e.setCancelled(true);
                return;
            }

            switch (vars.global.getOrDefault(playerName, 0)) {
                case 0:
                    local.SendMessage(message, player, plugin.chatConfig.getInt("local.range"));
                    e.setCancelled(true);
                    break;
                case 1:
                    break;
                case 2:
                    staff.SendMessage(message, player);
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