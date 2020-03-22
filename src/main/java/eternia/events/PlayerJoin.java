package eternia.events;

import eternia.configs.MVar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import eternia.player.PlayerManager;

public class PlayerJoin implements Listener {
    @EventHandler
    public void OnJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!PlayerManager.PlayerExist(player.getUniqueId())) {
            PlayerManager.CreatePlayer(player.getUniqueId());
        }
        event.setJoinMessage(null);
        MVar.broadcastReplaceMessage("join", player.getName());
    }
}