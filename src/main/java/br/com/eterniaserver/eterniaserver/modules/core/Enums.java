package br.com.eterniaserver.eterniaserver.modules.core;

import br.com.eterniaserver.eterniaserver.modules.Constants;

final class Enums {

    private Enums() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    enum Commands {
        GAMEMODE,
        GAMEMODE_SURVIVAL,
        GAMEMODE_CREATIVE,
        GAMEMODE_ADVENTURE,
        GAMEMODE_SPECTATOR,
        AFK,
        GODMODE,
        HAT,
        WORKBENCH,
        ENDERCHEST,
        OPENINV,
    }

}
