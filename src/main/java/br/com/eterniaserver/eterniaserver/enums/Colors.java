package br.com.eterniaserver.eterniaserver.enums;

import br.com.eterniaserver.eterniaserver.Configs;

public enum Colors {

    BLACK(Configs.instance.cnBlack, "&o"),
    DARK_BLUE(Configs.instance.cnDarkBlue, "&1"),
    DARK_GREEN(Configs.instance.cnDarkGreen, "&2"),
    DARK_AQUA(Configs.instance.cnDarkAqua, "&3"),
    DARK_RED(Configs.instance.cnDarkRed, "&4"),
    DARK_PURPLE(Configs.instance.cnDarkPurple, "&5"),
    GOLD(Configs.instance.cnGold, "&6"),
    GRAY(Configs.instance.cnGray, "&7"),
    DARK_GRAY(Configs.instance.cnDarkGray, "&8"),
    BLUE(Configs.instance.cnBlue, "&9"),
    GREEN(Configs.instance.cnGreen, "&a"),
    AQUA(Configs.instance.cnAqua, "&b"),
    RED(Configs.instance.cnRed, "&c"),
    LIGHT_PURPLE(Configs.instance.cnLightPurple, "&d"),
    YELLOW(Configs.instance.cnYellow, "&e"),
    WHITE(Configs.instance.cnWhite, "&f");

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
