package br.com.eterniaserver.eterniaserver;

import org.junit.jupiter.api.Test;

import br.com.eterniaserver.eterniaserver.objects.User;
import br.com.eterniaserver.eterniaserver.objects.ChannelObject;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ChannelsTest {

    @Test
    public void testTrivial() {
        assertTrue(true);
    }

    @Test
    public void channelObject() {
        ChannelObject channelObject = new ChannelObject("{test}", "teste", "teste.test", "§f", false, 0);
        User user = mock(User.class);
        when(user.hasPermission("teste.test")).thenReturn(true);

        assertNotNull(channelObject);
        assertTrue(user.hasPermission(channelObject.getPerm()));
    }
    
}
