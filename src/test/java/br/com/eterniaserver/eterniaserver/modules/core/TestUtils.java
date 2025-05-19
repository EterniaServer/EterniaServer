package br.com.eterniaserver.eterniaserver.modules.core;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.database.Database;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.UUID;

class TestUtils {

    @Test
    void getNameAndDisplayFromConsole() {
        String name = "yuri";

        CommandSender commandSender = Mockito.mock(CommandSender.class);
        Mockito.when(commandSender.getName()).thenReturn(name);

        String[] nameAndDisplay = Utils.getNameAndDisplay(commandSender);

        Assertions.assertEquals(name, nameAndDisplay[0]);
        Assertions.assertEquals(name, nameAndDisplay[1]);
    }

    @Test
    void getNameAndDisplayFromPlayer() {
        String name = "yuri";
        String display = "Yuri";
        UUID uuid = UUID.randomUUID();

        Entities.PlayerProfile playerProfile = Mockito.mock(Entities.PlayerProfile.class);
        Mockito.when(playerProfile.getPlayerName()).thenReturn(name);
        Mockito.when(playerProfile.getPlayerDisplay()).thenReturn(display);
        Mockito.when(playerProfile.getUuid()).thenReturn(uuid);

        Player player = Mockito.mock(Player.class);
        Mockito.when(player.getName()).thenReturn(name);
        Mockito.when(player.getUniqueId()).thenReturn(uuid);

        try (MockedStatic<EterniaLib> eterniaLib = Mockito.mockStatic(EterniaLib.class)) {
            Database database = Mockito.mock(Database.class);
            eterniaLib.when(EterniaLib::getDatabase).thenReturn(database);

            Mockito.when(database.get(Entities.PlayerProfile.class, uuid)).thenReturn(playerProfile);

            String[] nameAndDisplay = Utils.getNameAndDisplay(player);
            Assertions.assertEquals(playerProfile.getPlayerName(), nameAndDisplay[0]);
            Assertions.assertEquals(playerProfile.getPlayerDisplay(), nameAndDisplay[1]);
        }
    }

}
