package br.com.eterniaserver.eterniaserver.events;

import br.com.eterniaserver.eterniaserver.configs.Strings;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class OnPlayerSignChange implements Listener {

    private final Strings strings;

    public OnPlayerSignChange(Strings strings) {
        this.strings = strings;
    }

    @EventHandler
    public void onPlayerSignChange(SignChangeEvent event) {
        if (event.getPlayer().hasPermission("eternia.sign.color")) for (int i = 0; i < 4; i++) event.setLine(i, strings.getColor(event.getLine(i)));
    }

}
