package br.com.eterniaserver.eterniaserver;

import org.junit.jupiter.api.Test;

import br.com.eterniaserver.eterniaserver.core.APIServer;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;


public class UserTest {

    @Test
    public void testTrivial() {
        assertTrue(true);
    }

    @Test
    public void testPlayerProfile() {
        PlayerProfile playerProfile = new PlayerProfile("yurinogueira", System.currentTimeMillis(), System.currentTimeMillis(), 0);

        assertNotNull(playerProfile.getPlayerDisplayName());
        assertNotNull(playerProfile.getHomes());

        APIServer.putProfile(UUID.randomUUID(), playerProfile);

        assertEquals(1, APIServer.getProfileMapSize());
        
    }
    
}
