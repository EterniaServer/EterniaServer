package br.com.eterniaserver.eterniaserver.objects;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CashItem {

    private final ItemStack itemStack;
    private final int cost;
    private final List<String> messages;
    private final List<String> commands;
    private final boolean glass;

    public CashItem(ItemStack itemStack, int cost, List<String> messages, List<String> commands, boolean glass) {
        this.itemStack = itemStack;
        this.cost = cost;
        this.messages = messages;
        this.commands = commands;
        this.glass = glass;
    }

    public boolean isGlass() {
        return glass;
    }

    public ItemStack getItemStack() {
        return itemStack;
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
