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
    void testExitFromAfkWhenPlayerAreAfk() {
        String playerName = "Test";
        String playerDisplay = "Test";

        Player player = Mockito.mock(Player.class);
        Entities.PlayerProfile playerProfile = Mockito.mock(Entities.PlayerProfile.class);
        PluginManager pluginManagement = Mockito.mock(PluginManager.class);
        Component globalMessages = Mockito.mock(Component.class);

        Mockito.when(playerProfile.getPlayerName()).thenReturn(playerName);
        Mockito.when(playerProfile.getPlayerDisplay()).thenReturn(playerDisplay);
        Mockito.when(playerProfile.isAfk()).thenReturn(true);
        Mockito.when(plugin.getMiniMessage(Messages.AFK_LEAVE, true, playerName, playerDisplay)).thenReturn(globalMessages);
        Mockito.when(server.getPluginManager()).thenReturn(pluginManagement);

        afkService.exitFromAfk(player, playerProfile, AfkStatusEvent.Cause.MOVE);

        Mockito.verify(playerProfile, Mockito.times(1)).setLastMove(Mockito.anyLong());
        Mockito.verify(server, Mockito.times(1)).broadcast(globalMessages);
    }

    @Test
    void testGetGUINotCreated() {
        String title = "Test";
        Player player = Mockito.mock(Player.class);

        Assertions.assertNull(guiAPI.getGUI(title, player));
    }

    @Test
    void testCreateGUIAndGet() {
        String title = "Test";
        int itemAmount = 9;

        Player player = Mockito.mock(Player.class);
        Component titleComponent = Mockito.mock(Component.class);
        Inventory inventory = Mockito.mock(Inventory.class);

        Mockito.when(plugin.getString(Strings.GUI_SECRET)).thenReturn("");
        Mockito.when(plugin.parseColor(title)).thenReturn(titleComponent);
        Mockito.when(titleComponent.compact()).thenReturn(titleComponent);
        Mockito.when(server.createInventory(player, itemAmount, titleComponent)).thenReturn(inventory);

        ItemStack[] itemStacks = new ItemStack[itemAmount];
        for (int i = 0; i < itemAmount; i++) {
            itemStacks[i] = Mockito.mock(ItemStack.class);
        }

        guiAPI.createGUI(title, itemStacks);

        Assertions.assertNotNull(guiAPI.getGUI(title, player));
        for (int i = 0; i < itemAmount; i++) {
            Mockito.verify(inventory).setItem(i, itemStacks[i]);
        }
    }

}
