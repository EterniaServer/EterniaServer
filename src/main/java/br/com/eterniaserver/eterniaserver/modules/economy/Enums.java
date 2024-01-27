package br.com.eterniaserver.eterniaserver.modules.economy;

import br.com.eterniaserver.eterniaserver.modules.Constants;

final class Enums {

    private Enums() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    enum Commands {
        ECONOMY,
        ECONOMY_BALTOP,
        ECONOMY_BALANCE,
        ECONOMY_PAY,
        ECONOMY_GIVE,
        ECONOMY_TAKE,
        ECONOMY_BANK,
        ECONOMY_BANK_LIST,
        ECONOMY_BANK_CREATE,
        ECONOMY_BANK_INFO,
        ECONOMY_BANK_DELETE,
        ECONOMY_BANK_MY_BANKS,
        ECONOMY_BANK_DEPOSIT,
        ECONOMY_BANK_WITHDRAW,
        ECONOMY_BANK_CHANGE_ROLE,
        ECONOMY_BANK_AFFILIATE,
    }

    enum BankRole {
        OWNER,
        TRUSTED,
        MEMBER,
    }

}
