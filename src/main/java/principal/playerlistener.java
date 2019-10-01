package principal;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import java.util.HashMap;

public class playerlistener implements Listener
{
    public static final HashMap<Player, Location> back = new HashMap<>();
    @EventHandler
    public void onDeath(PlayerDeathEvent event)
    {
        Player player = event.getEntity();
        back.put(player, player.getLocation());
    }
}