package br.com.eterniaserver.eterniaserver.modules.cash;

import br.com.eterniaserver.eterniaserver.modules.Constants;

final class Enums {

    private Enums() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    enum Commands {
        CASH,
        CASH_HELP,
        CASH_BALANCE,
        CASH_ACCEPT,
        CASH_DENY,
        CASH_PAY,
        CASH_GIVE,
        CASH_REMOVE,
    }

}
