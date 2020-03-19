package eternia.events;

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
    }
}