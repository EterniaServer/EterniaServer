package br.com.eterniaserver.eterniaserver.modules.experience;

import br.com.eterniaserver.eterniaserver.modules.Constants;

final class Enums {

    private Enums() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    enum Commands {
        EXPERIENCE,
        EXPERIENCE_SET,
        EXPERIENCE_TAKE,
        EXPERIENCE_GIVE,
        EXPERIENCE_CHECK,
        EXPERIENCE_BOTTLE,
        EXPERIENCE_WITHDRAW,
        EXPERIENCE_DEPOSIT,
    }

}
