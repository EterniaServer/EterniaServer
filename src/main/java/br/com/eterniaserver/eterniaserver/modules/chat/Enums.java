package br.com.eterniaserver.eterniaserver.modules.chat;

import br.com.eterniaserver.eterniaserver.modules.Constants;

final class Enums {

    private Enums() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    enum Commands {
        CHAT,
        CHAT_CLEAR,
        CHANNEL,
        CHAT_SPY,
        CHAT_REPLY,
        CHAT_TELL,
    }
}
