package br.com.eterniaserver.eterniaserver.modules.glow;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.modules.core.Entities;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;

final class Handlers implements Listener {

    private final Services.Glow glow;

    public Handlers(Services.Glow glow) {
        this.glow = glow;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLogout(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        glow.clearPlayerTeams(player);

        Entities.PlayerProfile playerProfile = EterniaLib.getDatabase().get(Entities.PlayerProfile.class, player.getUniqueId());
        playerProfile.setColor(null);

        player.removePotionEffect(PotionEffectType.GLOWING);
        player.setGlowing(false);
    }

}
