package br.com.eterniaserver.eterniaserver.modules.experience;

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
        final Action action = event.getAction();
        if (!(action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK))) {
            return;
        }

        final ItemStack itemStack = event.getItem();
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return;
        }

        final Player player = event.getPlayer();
        final ItemMeta itemMeta = itemStack.getItemMeta();
        final PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        final boolean hasFunction = dataContainer.has(plugin.getKey(ItemsKeys.TAG_FUNCTION), PersistentDataType.INTEGER);
        final boolean hasIntValue = dataContainer.has(plugin.getKey(ItemsKeys.TAG_INT_VALUE), PersistentDataType.INTEGER);
        if (!hasIntValue || !hasFunction) {
            return;
        }

        if (player.getInventory().getItemInMainHand().equals(itemStack)) {
            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
        }
        else {
            player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
        }
        player.giveExp(dataContainer.get(plugin.getKey(ItemsKeys.TAG_INT_VALUE), PersistentDataType.INTEGER));
    }


}
