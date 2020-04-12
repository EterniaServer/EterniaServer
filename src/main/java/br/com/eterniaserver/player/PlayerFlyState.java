package br.com.eterniaserver.player;

import br.com.eterniaserver.configs.Messages;
import org.bukkit.entity.Player;

public class PlayerFlyState {

    public PlayerFlyState(Player player) {
        if (player.getAllowFlight()) {
            player.setAllowFlight(false);
            Messages.PlayerMessage("fly.disable", player);
        } else {
            player.setAllowFlight(true);
            Messages.PlayerMessage("fly.enable", player);
        }
    }

}