package br.com.eterniaserver.eterniaserver.modules.core;

final class Enums {

    private Enums() {
        throw new IllegalStateException("Utility class");
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
