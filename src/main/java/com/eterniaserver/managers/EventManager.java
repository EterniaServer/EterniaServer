package com.eterniaserver.managers;

import com.eterniaserver.EterniaServer;
import com.eterniaserver.configs.CVar;
import com.eterniaserver.configs.Vars;
import com.eterniaserver.events.*;

public class EventManager {
    public EventManager(EterniaServer plugin) {
        new NetherPortal().runTaskTimer(EterniaServer.getMain(), 20L, plugin.getConfig().getInt("server.nether-check") * 20);
        plugin.getServer().getPluginManager().registerEvents(new OnChat(), EterniaServer.getMain());
        plugin.getServer().getPluginManager().registerEvents(new OnPlayerJoin(), EterniaServer.getMain());
        plugin.getServer().getPluginManager().registerEvents(new OnPlayerTeleport(), EterniaServer.getMain());
        plugin.getServer().getPluginManager().registerEvents(new OnExpDrop(), EterniaServer.getMain());
        plugin.getServer().getPluginManager().registerEvents(new OnBlockBreak(), EterniaServer.getMain());
        plugin.getServer().getPluginManager().registerEvents(new OnPlayerLeave(), EterniaServer.getMain());
        plugin.getServer().getPluginManager().registerEvents(new OnBlockPlace(), EterniaServer.getMain());
        if (CVar.getBool("spawners.prevent-anvil")) {
            plugin.getServer().getPluginManager().registerEvents(new OnInventoryClick(), EterniaServer.getMain());
        }
        plugin.getServer().getPluginManager().registerEvents(new OnDamage(), EterniaServer.getMain());
    }
}