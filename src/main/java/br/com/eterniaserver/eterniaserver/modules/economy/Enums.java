package br.com.eterniaserver.eterniaserver.modules.economy;

import br.com.eterniaserver.eterniaserver.modules.Constants;

final class Enums {

    private Enums() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    enum Commands {

    }

    enum BankRole {
        OWNER,
        TRUSTED,
        MEMBER,
    }

}
