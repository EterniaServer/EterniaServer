package br.com.eterniaserver.eterniaserver.events;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Vars;

import br.com.eterniaserver.eterniaserver.configs.methods.Checks;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.filter.ChatFormatter;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.filter.Colors;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.filter.CustomPlaceholdersFilter;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.filter.JsonSender;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.act.utils.ChatMessage;
import br.com.eterniaserver.eterniaserver.modules.chatmanager.events.ChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.concurrent.TimeUnit;

public class OnPlayerChat implements Listener {

    private final EterniaServer plugin;
    private final ChatEvent chatEvent;
    private final ChatFormatter cf;
    private final JsonSender js;
    private final Vars vars;
    private final CustomPlaceholdersFilter cp;
    private final Messages messages;
    private final Colors c = new Colors();

    public OnPlayerChat(EterniaServer plugin, ChatEvent chatEvent, Checks checks, Vars vars, Messages messages) {
        this.plugin = plugin;
        this.chatEvent = chatEvent;
        this.vars = vars;
        this.cp = new CustomPlaceholdersFilter(vars);
        this.js = new JsonSender(plugin, vars);
        this.cf = new ChatFormatter(plugin, checks, vars);
        this.messages = messages;
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (e.getMessage().contains("yurinogueira")) {
            Bukkit.getOnlinePlayers().forEach(player -> player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.A)));
        }
        if (e.getMessage().contains("Cobra")) {
            Bukkit.getOnlinePlayers().forEach(player -> player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.B)));
        }
        if (e.getMessage().contains("Mates_CZ")) {
            Bukkit.getOnlinePlayers().forEach(player -> player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.G)));
        }
        if (plugin.serverConfig.getBoolean("modules.playerchecks")) {
            vars.afktime.put(e.getPlayer().getName(), System.currentTimeMillis());
        }
        if (plugin.serverConfig.getBoolean("modules.chat")) {
            if (plugin.chatMuted && !e.getPlayer().hasPermission("eternia.mute.bypass")) {
                messages.sendMessage("chat.chatmuted", e.getPlayer());
                e.setCancelled(true);
                return;
            }
            if (vars.player_muted.get(e.getPlayer().getName()) - System.currentTimeMillis() > 0) {
                messages.sendMessage("chat.muted", "%time%", TimeUnit.MILLISECONDS.toSeconds(vars.player_muted.get(e.getPlayer().getName()) - System.currentTimeMillis()), e.getPlayer());
                e.setCancelled(true);
                return;
            }
            chatEvent.onPlayerChat(e);
            if (e.isCancelled()) {
                return;
            }
            ChatMessage message = new ChatMessage(e.getMessage());
            cf.filter(e, message);
            c.filter(e, message);
            cp.filter(e, message);
            js.filter(e, message);
        }
    }

}