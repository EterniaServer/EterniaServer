package br.com.eterniaserver.eterniaserver;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;

import br.com.eterniaserver.eterniaserver.craft.CraftEterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Colors;

import org.junit.jupiter.api.*;

class TestPluginClass {

    public static ServerMock server;
    public static CraftEterniaServer plugin;

    @BeforeAll
    public static void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(CraftEterniaServer.class);
    }

    @AfterAll
    public static void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Verify if the xps equations are right")
    void testXpPerLvL() {
        final int xp = plugin.getXPForLevel(1);
        Assertions.assertEquals(xp, 7);
    }

    @Test
    @DisplayName("Check if the translate to new colors are ok")
    void translateHex() {
        final String message = plugin.translateHex("Testando rapa!");
        final String messageWithColor = plugin.translateHex("#00FF00Oie #01FF00Oie");
        final String rightMessageWithColor = "&x&0&0&F&F&0&0Oie &x&0&1&F&F&0&0Oie".replace('&', (char) 0x00A7);

        Assertions.assertEquals(message, "Testando rapa!");
        Assertions.assertEquals(messageWithColor, rightMessageWithColor);
    }

}
