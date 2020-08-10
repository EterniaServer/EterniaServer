package br.com.eterniaserver.eterniaserver.generics;

import br.com.eterniaserver.eterniaserver.Strings;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class OnPlayerSignChange implements Listener {


    @EventHandler (priority = EventPriority.LOWEST)
    public void onPlayerSignChange(SignChangeEvent event) {
        if (event.getPlayer().hasPermission("eternia.sign.color")) for (byte i = 0; i < 4; i++) event.setLine(i, Strings.getColor(event.getLine(i)));
    }

}
