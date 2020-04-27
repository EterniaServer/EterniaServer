package br.com.eterniaserver.events;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Vars;

import br.com.eterniaserver.modules.chatmanager.act.filter.ChatFormatter;
import br.com.eterniaserver.modules.chatmanager.act.filter.Colors;
import br.com.eterniaserver.modules.chatmanager.act.filter.CustomPlaceholdersFilter;
import br.com.eterniaserver.modules.chatmanager.act.filter.JsonSender;
import br.com.eterniaserver.modules.chatmanager.act.utils.ChatMessage;
import br.com.eterniaserver.modules.chatmanager.events.ChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class OnPlayerChat implements Listener {

    final ChatFormatter cf = new ChatFormatter();
    final JsonSender js = new JsonSender();
    final CustomPlaceholdersFilter cp = new CustomPlaceholdersFilter();
    final Colors c = new Colors();

    @EventHandler
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
        if (EterniaServer.configs.getBoolean("modules.playerchecks")) {
            Vars.afktime.put(e.getPlayer().getName(), System.currentTimeMillis());
        }
        if (EterniaServer.configs.getBoolean("modules.chat")) {
            ChatEvent.onPlayerChat(e);
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