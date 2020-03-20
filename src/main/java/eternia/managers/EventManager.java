package eternia.managers;

import eternia.EterniaServer;
import eternia.events.*;

public class EventManager {
    public EventManager(EterniaServer plugin) {
        new NetherPortal().runTaskTimer(EterniaServer.getMain(), 20L, plugin.getConfig().getInt("intervalo") * 20);
        plugin.getServer().getPluginManager().registerEvents(new OnChat(), EterniaServer.getMain());
        plugin.getServer().getPluginManager().registerEvents(new PlayerJoin(), EterniaServer.getMain());
        plugin.getServer().getPluginManager().registerEvents(new PlayerTeleport(), EterniaServer.getMain());
        plugin.getServer().getPluginManager().registerEvents(new ExpDrop(), EterniaServer.getMain());
        plugin.getServer().getPluginManager().registerEvents(new OnBlockBreak(), EterniaServer.getMain());
        plugin.getServer().getPluginManager().registerEvents(new PlayerLeave(), EterniaServer.getMain());
    }
}