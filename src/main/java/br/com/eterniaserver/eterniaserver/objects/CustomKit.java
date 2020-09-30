package br.com.eterniaserver.eterniaserver.objects;

import java.util.List;

public class CustomKit {

    private final int delay;
    private final List<String> commands;
    private final List<String> messages;

    public CustomKit(int delay, List<String> commands, List<String> messages) {
        this.delay = delay;
        this.commands = commands;
        this.messages = messages;
    }

    public List<String> getMessages() {
        return messages;
    }

    public List<String> getCommands() {
        return commands;
    }

    public int getDelay() {
        return delay;
    }

}
