package br.com.eterniaserver.eterniaserver;

public enum Constants {
    MESSAGE("%message%"),
    PLAYER("%player_displayname%"),
    TARGET("%target_displayname%"),
    VALUE("%value%"),
    WARP("%warp_name%"),
    MODULE("%module%"),
    TYPE("%type%"),
    AMOUNT("%amount%");

    private final String value;

    Constants(String value) {
        this.value = value;
    }

    public String get() {
        return value;
    }

}
