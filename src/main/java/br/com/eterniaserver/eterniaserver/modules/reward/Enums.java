package br.com.eterniaserver.eterniaserver.modules.reward;

import br.com.eterniaserver.eterniaserver.modules.Constants;

final class Enums {

    private Enums() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    enum Commands {
        USE_KEY,
        GEN_KEY
    }

}
