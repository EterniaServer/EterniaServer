package br.com.eterniaserver.eterniaserver.objects;

import br.com.eterniaserver.eterniaserver.enums.Commands;

public class CommandLocale {

    public final Commands id;
    public final String name;
    public final String syntax;
    public final String description;
    public final String perm;

    public CommandLocale(Commands id, String name, String syntax, String description, String perm) {
        this.id = id;
        this.name = name;
        this.syntax = syntax;
        this.description = description;
        this.perm = perm;
    }

}
