package br.com.eterniaserver.eterniaserver.enums;

import br.com.eterniaserver.eterniaserver.EterniaServer;

public enum Colors {

    BLACK(EterniaServer.constants.cnBlack, "&o"),
    DARK_BLUE(EterniaServer.constants.cnDarkBlue, "&1"),
    DARK_GREEN(EterniaServer.constants.cnDarkGreen, "&2"),
    DARK_AQUA(EterniaServer.constants.cnDarkAqua, "&3"),
    DARK_RED(EterniaServer.constants.cnDarkRed, "&4"),
    DARK_PURPLE(EterniaServer.constants.cnDarkPurple, "&5"),
    GOLD(EterniaServer.constants.cnGold, "&6"),
    GRAY(EterniaServer.constants.cnGray, "&7"),
    DARK_GRAY(EterniaServer.constants.cnDarkGray, "&8"),
    BLUE(EterniaServer.constants.cnBlue, "&9"),
    GREEN(EterniaServer.constants.cnGreen, "&a"),
    AQUA(EterniaServer.constants.cnAqua, "&b"),
    RED(EterniaServer.constants.cnRed, "&c"),
    LIGHT_PURPLE(EterniaServer.constants.cnLightPurple, "&d"),
    YELLOW(EterniaServer.constants.cnYellow, "&e"),
    WHITE(EterniaServer.constants.cnWhite, "&f");

    final String colorName;
    final String colorStr;

    Colors(String colorName, String colorStr) {
        this.colorName = colorName;
        this.colorStr = colorStr;
    }

    public String getName() {
        return colorName;
    }

    public String getColorStr() {
        return colorStr;
    }

}
