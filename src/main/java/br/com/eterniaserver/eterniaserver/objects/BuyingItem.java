package br.com.eterniaserver.eterniaserver.objects;

import java.util.Arrays;
import java.util.List;

public class BuyingItem {

    private final List<String> messages;
    private final List<String> commands;
    private final int cost;

    public BuyingItem(final String messages, final String commands, int cost) {
        this.messages = Arrays.asList(messages.split(";"));
        this.commands = Arrays.asList(commands.split(";"));
        this.cost = cost;
    }

    public List<String> getCommands() {
        return commands;
    }

    public List<String> getMessages() {
        return messages;
    }

    public int getCost() {
        return cost;
    }

}
