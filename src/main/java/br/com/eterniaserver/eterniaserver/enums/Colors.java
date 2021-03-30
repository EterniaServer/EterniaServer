package br.com.eterniaserver.eterniaserver.enums;

import br.com.eterniaserver.eterniaserver.EterniaServer;

import org.bukkit.ChatColor;

public enum Colors {

    BLACK(EterniaServer.getUserAPI().getColorString(0), "&o", ChatColor.BLACK),
    DARK_BLUE(EterniaServer.getUserAPI().getColorString(1), "&1", ChatColor.DARK_BLUE),
    DARK_GREEN(EterniaServer.getUserAPI().getColorString(2), "&2", ChatColor.DARK_GREEN),
    DARK_AQUA(EterniaServer.getUserAPI().getColorString(3), "&3", ChatColor.DARK_AQUA),
    DARK_RED(EterniaServer.getUserAPI().getColorString(4), "&4", ChatColor.DARK_RED),
    DARK_PURPLE(EterniaServer.getUserAPI().getColorString(5), "&5", ChatColor.DARK_PURPLE),
    GOLD(EterniaServer.getUserAPI().getColorString(6), "&6", ChatColor.GOLD),
    GRAY(EterniaServer.getUserAPI().getColorString(7), "&7", ChatColor.GRAY),
    DARK_GRAY(EterniaServer.getUserAPI().getColorString(8), "&8", ChatColor.DARK_GRAY),
    BLUE(EterniaServer.getUserAPI().getColorString(9), "&9", ChatColor.BLUE),
    GREEN(EterniaServer.getUserAPI().getColorString(10), "&a", ChatColor.GREEN),
    AQUA(EterniaServer.getUserAPI().getColorString(11), "&b", ChatColor.AQUA),
    RED(EterniaServer.getUserAPI().getColorString(12), "&c", ChatColor.RED),
    LIGHT_PURPLE(EterniaServer.getUserAPI().getColorString(13), "&d", ChatColor.LIGHT_PURPLE),
    YELLOW(EterniaServer.getUserAPI().getColorString(14), "&e", ChatColor.YELLOW),
    WHITE(EterniaServer.getUserAPI().getColorString(15), "&f", ChatColor.WHITE);

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
