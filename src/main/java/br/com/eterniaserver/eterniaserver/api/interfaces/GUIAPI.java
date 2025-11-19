package br.com.eterniaserver.eterniaserver.api.interfaces;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface GUIAPI {

    /**
     * Create a GUI with a title and a list of items
     * @param guiName the name of the GUI
     * @param items the items to be added to the GUI
     */
    void createGUI(String guiName, ItemStack[] items);

    /**
     * Get the GUI by the title
     * @param title the title of the GUI
     * @param player the player that will open the GUI
     * @return the GUI
     */
    Inventory getGUI(String title, Player player);

    /**
     * Get the title of a GUI
     * @param title the title of the GUI
     * @return the title
     */
    Component getTitle(String title);

}
