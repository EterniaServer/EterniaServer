package br.com.eterniaserver.eterniaserver.enums;

import br.com.eterniaserver.eterniaserver.EterniaServer;

public enum Colors {

    BLACK(EterniaServer.getString(Strings.CONS_BLACK), "&o"),
    DARK_BLUE(EterniaServer.getString(Strings.CONS_DARK_BLUE), "&1"),
    DARK_GREEN(EterniaServer.getString(Strings.CONS_DARK_GREEN), "&2"),
    DARK_AQUA(EterniaServer.getString(Strings.CONS_DARK_AQUA), "&3"),
    DARK_RED(EterniaServer.getString(Strings.CONS_DARK_RED), "&4"),
    DARK_PURPLE(EterniaServer.getString(Strings.CONS_DARK_PURPLE), "&5"),
    GOLD(EterniaServer.getString(Strings.CONS_GOLD), "&6"),
    GRAY(EterniaServer.getString(Strings.CONS_GRAY), "&7"),
    DARK_GRAY(EterniaServer.getString(Strings.CONS_DARK_GRAY), "&8"),
    BLUE(EterniaServer.getString(Strings.CONS_BLUE), "&9"),
    GREEN(EterniaServer.getString(Strings.CONS_GREEN), "&a"),
    AQUA(EterniaServer.getString(Strings.CONS_AQUA), "&b"),
    RED(EterniaServer.getString(Strings.CONS_RED), "&c"),
    LIGHT_PURPLE(EterniaServer.getString(Strings.CONS_LIGHT_PURPLE), "&d"),
    YELLOW(EterniaServer.getString(Strings.CONS_YELLOW), "&e"),
    WHITE(EterniaServer.getString(Strings.CONS_WHITE), "&f");

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
