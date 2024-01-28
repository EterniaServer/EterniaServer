package br.com.eterniaserver.eterniaserver.modules.item;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.ItemsKeys;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

final class Handlers implements Listener {

    private final EterniaServer plugin;

    public Handlers(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
            ItemStack itemStack = event.getItem();
            if (itemFunction(player, itemStack)) {
                event.setCancelled(true);
            }
        }
    }

    private boolean itemFunction(Player player, final ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return false;
        }

        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        if (container.has(plugin.getKey(ItemsKeys.TAG_FUNCTION), PersistentDataType.INTEGER)) {
            int function = container.getOrDefault(plugin.getKey(ItemsKeys.TAG_FUNCTION), PersistentDataType.INTEGER, 0);
            if (function == 2) {
                return customFunction(player, itemStack);
            }
        }

        return false;
    }

    private boolean customFunction(Player player, ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (!itemMeta.getPersistentDataContainer().has(plugin.getKey(ItemsKeys.TAG_RUN_COMMAND), PersistentDataType.STRING)) {
            return false;
        }

        String cmds = itemMeta.getPersistentDataContainer().getOrDefault(plugin.getKey(ItemsKeys.TAG_RUN_COMMAND), PersistentDataType.STRING, "");
        boolean runInConsole = itemMeta.getPersistentDataContainer().getOrDefault(plugin.getKey(ItemsKeys.TAG_RUN_IN_CONSOLE), PersistentDataType.INTEGER, 0) == 1;

        for (String cmd : cmds.split(";")) {
            if (runInConsole) {
                plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), plugin.setPlaceholders(player, cmd));
            } else {
                player.performCommand(plugin.setPlaceholders(player, cmd));
            }
        }

        int itemUsages = itemMeta.getPersistentDataContainer().getOrDefault(plugin.getKey(ItemsKeys.TAG_USAGES), PersistentDataType.INTEGER,1);
        if (itemUsages == 1) {
            if (compareItems(player, itemStack)) {
                player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            } else {
                player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
            }
        } else if (itemUsages > 1) {
            itemMeta.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.TAG_USAGES), PersistentDataType.INTEGER, (itemUsages - 1));
            itemStack.setItemMeta(itemMeta);
        }

        return true;
    }

    private boolean compareItems(Player player, final ItemStack itemStack) {
        return player.getInventory().getItemInMainHand().isSimilar(itemStack);
    }

}
