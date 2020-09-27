package br.com.eterniaserver.eterniaserver.enums;

import br.com.eterniaserver.eterniaserver.EterniaServer;

public enum Colors {

    BLACK(EterniaServer.configs.cnBlack, "&o"),
    DARK_BLUE(EterniaServer.configs.cnDarkBlue, "&1"),
    DARK_GREEN(EterniaServer.configs.cnDarkGreen, "&2"),
    DARK_AQUA(EterniaServer.configs.cnDarkAqua, "&3"),
    DARK_RED(EterniaServer.configs.cnDarkRed, "&4"),
    DARK_PURPLE(EterniaServer.configs.cnDarkPurple, "&5"),
    GOLD(EterniaServer.configs.cnGold, "&6"),
    GRAY(EterniaServer.configs.cnGray, "&7"),
    DARK_GRAY(EterniaServer.configs.cnDarkGray, "&8"),
    BLUE(EterniaServer.configs.cnBlue, "&9"),
    GREEN(EterniaServer.configs.cnGreen, "&a"),
    AQUA(EterniaServer.configs.cnAqua, "&b"),
    RED(EterniaServer.configs.cnRed, "&c"),
    LIGHT_PURPLE(EterniaServer.configs.cnLightPurple, "&d"),
    YELLOW(EterniaServer.configs.cnYellow, "&e"),
    WHITE(EterniaServer.configs.cnWhite, "&f");

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
