package events;

import center.Vars;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerTeleport implements Listener
{
    @EventHandler
    public void onTeleport(PlayerTeleportEvent event)
    {
        Player player = event.getPlayer();
        Vars.back.put(player, player.getLocation());
    }
}
