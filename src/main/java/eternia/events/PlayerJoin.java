package eternia.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import eternia.player.PlayerManager;

public class PlayerJoin implements Listener {
    @EventHandler
    public void OnJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerManager.CreatePlayer(player.getUniqueId(), player);
    }
}