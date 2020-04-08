package br.com.eterniaserver.events;

import br.com.eterniaserver.configs.CVar;
import br.com.eterniaserver.configs.Vars;

import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class OnChat implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (e.getMessage().contains("yurinogueira")) {
            Bukkit.getOnlinePlayers().forEach(player -> player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.A)));
        }
        if (e.getMessage().contains("Cobra")) {
            Bukkit.getOnlinePlayers().forEach(player -> player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.B)));
        }
        if (CVar.getBool("modules.playerchecks")) {
            Vars.afktime.put(e.getPlayer().getName(), System.currentTimeMillis());
        }
    }

}