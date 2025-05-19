package br.com.eterniaserver.eterniaserver.modules.core;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.events.AfkStatusEvent;
import br.com.eterniaserver.eterniaserver.api.interfaces.GUIAPI;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;

import net.kyori.adventure.text.Component;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

class TestServices {

    private EterniaServer plugin;
    private Server server;

    private GUIAPI guiAPI;
    private Services.AfkService afkService;

    @BeforeEach
    void setUp() {
        plugin = Mockito.mock(EterniaServer.class);
        server = Mockito.mock(Server.class);

        Mockito.when(plugin.getServer()).thenReturn(server);

        guiAPI = new Services.GUI(plugin);
        afkService = new Services.AfkService(plugin);
    }

    @Test
    void testPlayerAreaAfk() {
        long lastMove = System.currentTimeMillis();
        int limitTime = 60;

        Entities.PlayerProfile playerProfile = Mockito.mock(Entities.PlayerProfile.class);


        Mockito.when(plugin.getInteger(Integers.AFK_TIMER)).thenReturn(limitTime);
        Mockito.when(playerProfile.getLastMove()).thenReturn(lastMove - 61000);

        Assertions.assertTrue(afkService.areAfk(playerProfile));
    }

    @Test
    void testPlayerAreNotAfk() {
        long lastMove = System.currentTimeMillis();
        int limitTime = 60;

        Entities.PlayerProfile playerProfile = Mockito.mock(Entities.PlayerProfile.class);

        Mockito.when(plugin.getInteger(Integers.AFK_TIMER)).thenReturn(limitTime);
        Mockito.when(playerProfile.getLastMove()).thenReturn(lastMove - 10000);

        Assertions.assertFalse(afkService.areAfk(playerProfile));
    }

    @Test
    void testExitFromAfkWhenPlayerAreNotAfk() {
        Player player = Mockito.mock(Player.class);
        Entities.PlayerProfile playerProfile = Mockito.mock(Entities.PlayerProfile.class);

        Mockito.when(playerProfile.isAfk()).thenReturn(false);

        afkService.exitFromAfk(player, playerProfile, AfkStatusEvent.Cause.MOVE);

        Mockito.verify(playerProfile, Mockito.times(1)).setLastMove(Mockito.anyLong());
    }

    @Test
    void testGetGUINotCreated() {
        String title = "Test";
        Player player = Mockito.mock(Player.class);

        Assertions.assertNull(guiAPI.getGUI(title, player));
    }

}
