package br.com.eterniaserver.eterniaserver.objects;

import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class CashGui {

    private final Map<Integer, ItemStack> menuGui;
    private final Map<Integer, ItemStack> permGui;
    private final Map<Integer, ItemStack> pacoteGui;
    private final Map<Integer, ItemStack> tagGui;
    private final Map<Integer, ItemStack> spawnerGui;

    public CashGui(Map<Integer, ItemStack> menuGui, Map<Integer, ItemStack> permGui, Map<Integer, ItemStack> pacoteGui, Map<Integer, ItemStack> tagGui, Map<Integer, ItemStack> spawnerGui) {
        this.menuGui = menuGui;
        this.permGui = permGui;
        this.pacoteGui = pacoteGui;
        this.tagGui = tagGui;
        this.spawnerGui = spawnerGui;
    }

    public Map<Integer, ItemStack> getMenuGui() {
        return menuGui;
    }

    public Map<Integer, ItemStack> getPacoteGui() {
        return pacoteGui;
    }

    public Map<Integer, ItemStack> getPermGui() {
        return permGui;
    }

    public Map<Integer, ItemStack> getSpawnerGui() {
        return spawnerGui;
    }

    public Map<Integer, ItemStack> getTagGui() {
        return tagGui;
    }

}
