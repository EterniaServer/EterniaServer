package center;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;

public class PlayerListener implements Listener
{
    public static final HashMap<Player, Location> back = new HashMap<>();
    // Sempre que um jogador se teleportar a localização anterior dele será
    // atualizada se já existir alguma localização ou salva na váriavel back.
    @EventHandler
    public void onTeleport(PlayerTeleportEvent event)
    {
        Player player = event.getPlayer();
        back.put(player, player.getLocation());
    }
}