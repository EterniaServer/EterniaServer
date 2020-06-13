package br.com.eterniaserver.eterniaserver.events;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class OnPlayerSignChange implements Listener {

    private final EFiles strings;

    public OnPlayerSignChange(EterniaServer plugin) {
        this.strings = plugin.getEFiles();
    }

    @EventHandler
    public void onPlayerSignChange(SignChangeEvent event) {
        if (event.getPlayer().hasPermission("eternia.sign.color")) for (int i = 0; i < 4; i++) event.setLine(i, strings.getColor(event.getLine(i)));
    }

}
