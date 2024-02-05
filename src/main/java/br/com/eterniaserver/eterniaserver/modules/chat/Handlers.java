package br.com.eterniaserver.eterniaserver.modules.chat;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.database.DatabaseInterface;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

final class Handlers implements Listener {

    private final EterniaServer plugin;
    private final DatabaseInterface databaseInterface;

    public Handlers(EterniaServer plugin) {
        this.plugin = plugin;
        this.databaseInterface = EterniaLib.getDatabase();
    }

    @EventHandler
    public void onAsyncChat(AsyncChatEvent event) {


        event.setCancelled(true);
    }
}
