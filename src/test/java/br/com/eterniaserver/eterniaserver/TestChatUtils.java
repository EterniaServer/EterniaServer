package br.com.eterniaserver.eterniaserver;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;

import br.com.eterniaserver.eternialib.EterniaLib;

import org.junit.jupiter.api.*;

public class TestChatUtils {

    private static EterniaServer plugin;

    @BeforeAll
    public static void setUp() {
        MockBukkit.mock();
        MockBukkit.load(EterniaLib.class);

        plugin = MockBukkit.load(EterniaServer.class);
    }

    @AfterAll
    public static void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Check if the translate to new colors are ok")
    void translateHex() {
        final String message = plugin.translateHex("Testando rapa!");
        final String messageWithColor = plugin.translateHex("#00FF00Oie #01FF00Oie");
        final String rightMessageWithColor = "&x&0&0&F&F&0&0Oie &x&0&1&F&F&0&0Oie".replace('&', (char) 0x00A7);

        Assertions.assertEquals("Testando rapa!", message);
        Assertions.assertEquals(rightMessageWithColor, messageWithColor);
    }

}
