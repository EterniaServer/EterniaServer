package br.com.eterniaserver.eterniaserver.modules.item;

import br.com.eterniaserver.eterniaserver.modules.Constants;

final class Enums {

    private Enums() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    enum Commands {
        ITEM,
        ITEM_SEND_CUSTOM,
        ITEM_CUSTOM_MODEL,
        ITEM_CLEAR,
        ITEM_CLEAR_LORE,
        ITEM_CLEAR_NAME,
        ITEM_ADD_LORE,
        ITEM_SET,
        ITEM_SET_LORE,
        ITEM_SET_NAME,
    }

}
