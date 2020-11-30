package br.com.eterniaserver.eterniaserver.enums;

import br.com.eterniaserver.eterniaserver.EterniaServer;

import org.bukkit.ChatColor;

public enum Colors {

    BLACK(EterniaServer.getString(Strings.CONS_BLACK), "&o", ChatColor.BLACK),
    DARK_BLUE(EterniaServer.getString(Strings.CONS_DARK_BLUE), "&1", ChatColor.DARK_BLUE),
    DARK_GREEN(EterniaServer.getString(Strings.CONS_DARK_GREEN), "&2", ChatColor.DARK_GREEN),
    DARK_AQUA(EterniaServer.getString(Strings.CONS_DARK_AQUA), "&3", ChatColor.DARK_AQUA),
    DARK_RED(EterniaServer.getString(Strings.CONS_DARK_RED), "&4", ChatColor.DARK_RED),
    DARK_PURPLE(EterniaServer.getString(Strings.CONS_DARK_PURPLE), "&5", ChatColor.DARK_PURPLE),
    GOLD(EterniaServer.getString(Strings.CONS_GOLD), "&6", ChatColor.GOLD),
    GRAY(EterniaServer.getString(Strings.CONS_GRAY), "&7", ChatColor.GRAY),
    DARK_GRAY(EterniaServer.getString(Strings.CONS_DARK_GRAY), "&8", ChatColor.DARK_GRAY),
    BLUE(EterniaServer.getString(Strings.CONS_BLUE), "&9", ChatColor.BLUE),
    GREEN(EterniaServer.getString(Strings.CONS_GREEN), "&a", ChatColor.GREEN),
    AQUA(EterniaServer.getString(Strings.CONS_AQUA), "&b", ChatColor.AQUA),
    RED(EterniaServer.getString(Strings.CONS_RED), "&c", ChatColor.RED),
    LIGHT_PURPLE(EterniaServer.getString(Strings.CONS_LIGHT_PURPLE), "&d", ChatColor.LIGHT_PURPLE),
    YELLOW(EterniaServer.getString(Strings.CONS_YELLOW), "&e", ChatColor.YELLOW),
    WHITE(EterniaServer.getString(Strings.CONS_WHITE), "&f", ChatColor.WHITE);

    final String colorName;
    final String colorStr;
    final ChatColor chatColor;

    Colors(String colorName, String colorStr, ChatColor chatColor) {
        this.colorName = colorName;
        this.colorStr = colorStr;
        this.chatColor = chatColor;
    }

    public String getName() {
        return colorName;
    }

    public String getColorStr() {
        return colorStr;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

}
