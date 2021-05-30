package br.com.eterniaserver.eterniaserver;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;

import br.com.eterniaserver.eternialib.EterniaLib;

import br.com.eterniaserver.eterniaserver.enums.Colors;
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
    void testTranslateHex() {
        final String rightMessage = "Testando rapa!";
        final String message = plugin.translateHex(rightMessage);
        final String rightMessageWithColor = "&x&0&0&F&F&0&0Oie &x&0&1&F&F&0&0Oie".replace('&', (char) 0x00A7);
        final String messageWithColor = plugin.translateHex("#00FF00Oie #01FF00Oie");

        Assertions.assertEquals(rightMessage, message);
        Assertions.assertEquals(rightMessageWithColor, messageWithColor);
    }

    @Test
    @DisplayName("Verify if the method to get Colors Enum from string is working")
    void testColorFromString() {
        final Colors rightColor = Colors.BLACK;
        final Colors color = plugin.colorFromString("BLACK");

        Assertions.assertEquals(rightColor, color);
    }

}
