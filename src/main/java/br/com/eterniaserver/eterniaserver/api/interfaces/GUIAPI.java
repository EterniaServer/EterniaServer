package br.com.eterniaserver.eterniaserver.api.interfaces;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface GUIAPI {

    void createGUI(String guiName, ItemStack[] items);

    boolean contains(Component title);

    Inventory getGUI(String title, Player player);

}
