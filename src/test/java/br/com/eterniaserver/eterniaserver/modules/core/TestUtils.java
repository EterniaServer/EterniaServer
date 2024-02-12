package br.com.eterniaserver.eterniaserver.modules.core;

import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class TestUtils {

    @Test
    void getNameAndDisplay() {
        String name = "yuri";

        CommandSender commandSender = Mockito.mock(CommandSender.class);
        Mockito.when(commandSender.getName()).thenReturn(name);

        String[] nameAndDisplay = Utils.getNameAndDisplay(commandSender);

        Assertions.assertEquals(nameAndDisplay[0], name);
        Assertions.assertEquals(nameAndDisplay[1], name);
    }

}
