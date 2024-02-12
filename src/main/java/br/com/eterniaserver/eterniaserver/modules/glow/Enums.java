package br.com.eterniaserver.eterniaserver.modules.glow;

import br.com.eterniaserver.eterniaserver.modules.Constants;

final class Enums {

    private Enums() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    enum Commands {
        GLOW,
        GLOW_COLOR
    }

    enum Color {
        BLACK("§0"),
        DARK_GREEN("§2"),
        DARK_RED("§4"),
        GOLD("§6"),
        DARK_GRAY("§8"),
        GREEN("§a"),
        RED("§c"),
        YELLOW("§e"),
        DARK_BLUE("§1"),
        DARK_AQUA("§3"),
        DARK_PURPLE("§5"),
        GRAY("§7"),
        BLUE("§9"),
        AQUA("§b"),
        LIGHT_PURPLE("§d"),
        WHITE("§f");

        private final String value;

        Color(String value) {
            this.value = value;
        }

        public String getColor() {
            return value;
        }
    }

}
