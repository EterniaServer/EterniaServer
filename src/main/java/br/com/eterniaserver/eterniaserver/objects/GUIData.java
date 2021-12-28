package br.com.eterniaserver.eterniaserver.objects;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;

public class GUIData {

    private final ItemStack[] items;
    private final Component guiTitle;
    private final int guiSize;

    public GUIData(Component guiTitle, ItemStack[] items) {
        this.items = items;
        this.guiTitle = guiTitle;
        this.guiSize = items.length;
    }

    public ItemStack getItem(int slot) {
        return items[slot];
    }

    public Component getGuiTitle() {
        return guiTitle;
    }

    public int getGuiSize() {
        return guiSize;
    }

}
