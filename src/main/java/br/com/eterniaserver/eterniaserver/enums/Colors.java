package br.com.eterniaserver.eterniaserver.enums;

import br.com.eterniaserver.eterniaserver.Configs;

public enum Colors {

    BLACK(Configs.getInstance().cnBlack, "&o"),
    DARK_BLUE(Configs.getInstance().cnDarkBlue, "&1"),
    DARK_GREEN(Configs.getInstance().cnDarkGreen, "&2"),
    DARK_AQUA(Configs.getInstance().cnDarkAqua, "&3"),
    DARK_RED(Configs.getInstance().cnDarkRed, "&4"),
    DARK_PURPLE(Configs.getInstance().cnDarkPurple, "&5"),
    GOLD(Configs.getInstance().cnGold, "&6"),
    GRAY(Configs.getInstance().cnGray, "&7"),
    DARK_GRAY(Configs.getInstance().cnDarkGray, "&8"),
    BLUE(Configs.getInstance().cnBlue, "&9"),
    GREEN(Configs.getInstance().cnGreen, "&a"),
    AQUA(Configs.getInstance().cnAqua, "&b"),
    RED(Configs.getInstance().cnRed, "&c"),
    LIGHT_PURPLE(Configs.getInstance().cnLightPurple, "&d"),
    YELLOW(Configs.getInstance().cnYellow, "&e"),
    WHITE(Configs.getInstance().cnWhite, "&f");

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
