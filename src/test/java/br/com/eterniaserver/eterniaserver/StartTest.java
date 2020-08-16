package br.com.eterniaserver.eterniaserver;

import be.seeseemelk.mockbukkit.MockBukkit;
import br.com.eterniaserver.eterniaserver.configs.Strings;
import com.sun.org.slf4j.internal.Logger;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class StartTest {

    private final Logger logger = new Logger("EterniaServer");

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        MockBukkit.load(EterniaServer.class);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    public void checkMessages() {
        logger.debug(Strings.MSG_LOAD_DATA);
    }

}
