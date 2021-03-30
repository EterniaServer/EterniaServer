package br.com.eterniaserver.eterniaserver.objects;

import java.util.List;

public class CommandData {

    private final String description;
    private final List<String> aliases;
    private final List<String> commands;
    private final List<String> text;
    private final boolean console;

    public CommandData(String description, List<String> aliases, List<String> commands, List<String> text, boolean console) {
        this.description = description;
        this.aliases = aliases;
        this.commands = commands;
        this.text = text;
        this.console = console;
    }

    public List<String> getCommands() {
        return commands;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public List<String> getText() {
        return text;
    }

    public String getDescription() {
        return description;
    }

    public boolean getConsole() {
        return console;
    }

}
