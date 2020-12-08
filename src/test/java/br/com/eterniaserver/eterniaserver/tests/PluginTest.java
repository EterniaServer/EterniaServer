package br.com.eterniaserver.eterniaserver.tests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.entity.PlayerMock;

import br.com.eterniaserver.eterniaserver.objects.ChannelObject;
import br.com.eterniaserver.eterniaserver.EterniaServer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PluginTest {

    @BeforeAll
    public static void setUp() {
        MockBukkit.mock();
        MockBukkit.load(EterniaServer.class);
    }
    
    @AfterAll
    public static void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void testTrivial() {
        assertTrue(true);
    }

    @Test
    @DisplayName("ChannelObject Test")
    void channelObject() {
        ChannelObject channelObject = new ChannelObject("{test}", "test", "test.test", "§f", false, 0);
        PlayerMock player = MockBukkit.getMock().addPlayer("yurinogueira");

        assertFalse(player.hasPermission(channelObject.getPerm()));
    }
    
}
