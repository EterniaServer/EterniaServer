package eternia.events;

import eternia.configs.CVar;
import eternia.configs.MVar;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import java.util.Objects;

public class OnAnvilRename implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void AnvilRename(InventoryClickEvent e) {
        if (e.getInventory().getType() != InventoryType.ANVIL || Objects.requireNonNull(e.getCurrentItem()).getType() != Material.SPAWNER) {
            return;
        }

        if (!CVar.getBool("spawners.prevent-anvil")) {
            return;
        }

        Player player = (Player) e.getWhoClicked();
        e.setCancelled(true);
        MVar.playerMessage("spawners.anvil", player);
        MVar.consoleReplaceMessage("spawners.anvil-try", player.getName());
    }
}
