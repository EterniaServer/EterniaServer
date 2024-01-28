package br.com.eterniaserver.eterniaserver.modules.kit;

import br.com.eterniaserver.eterniaserver.modules.Constants;
import lombok.Data;

import java.util.List;

public class Utils {

    private Utils() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    @Data
    static class CustomKit {
        private final int delay;
        private final List<String> commands;
        private final List<String> messages;
    }

}
