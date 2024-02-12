package br.com.eterniaserver.eterniaserver.modules.teleport;

import br.com.eterniaserver.eterniaserver.modules.Constants;

final class Enums {

    private Enums() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    enum Commands {
        HOME,
        HOMES,
        SETHOME,
        DELHOME,
        TPA,
        TPAHERE,
        TPALL,
        WARP,
        WARPS,
        SETWARP,
        DELWARP,
        SPAWN,
        SETSPAWN,
    }

}