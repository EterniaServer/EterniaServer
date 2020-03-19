package eternia.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import eternia.storage.RewardBlockBreak;

public class OnBlockBreak implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        new RewardBlockBreak(event.getBlock().getType(), event);
    }
}