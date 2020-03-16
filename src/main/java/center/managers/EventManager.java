package center.managers;

import center.Main;
import events.*;

public class EventManager {
    public EventManager(Main plugin) {
        new NetherPortal().runTaskTimer(Main.getMain(), 20L, plugin.getConfig().getInt("intervalo") * 20);
        plugin.getServer().getPluginManager().registerEvents(new OnChat(), Main.getMain());
        plugin.getServer().getPluginManager().registerEvents(new PlayerJoin(), Main.getMain());
        plugin.getServer().getPluginManager().registerEvents(new PlayerTeleport(), Main.getMain());
        plugin.getServer().getPluginManager().registerEvents(new ExpDrop(), Main.getMain());
        plugin.getServer().getPluginManager().registerEvents(new OnBlockBreak(), Main.getMain());
    }
}