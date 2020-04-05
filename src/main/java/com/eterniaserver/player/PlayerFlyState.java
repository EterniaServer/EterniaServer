package com.eterniaserver.player;

import com.eterniaserver.configs.methods.ConsoleMessage;
import com.eterniaserver.configs.methods.PlayerMessage;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PlayerFlyState {
    public static void selfFly(Player player) {
        if (player.getAllowFlight()) {
            player.setAllowFlight(false);
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_HIT, 1, 1);
            new PlayerMessage("fly.disable", player);
        } else {
            player.setAllowFlight(true);
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_HIT, 1, 1);
            new PlayerMessage("fly.enable", player);
        }
    }

    public static void otherFly(Player target) {
        if (target.getAllowFlight()) {
            target.setAllowFlight(false);
            target.playSound(target.getLocation(), Sound.BLOCK_ANVIL_HIT, 1, 1);
            new PlayerMessage("fly.other-disable", "console", target);
            new ConsoleMessage("fly.disable-other", target.getName());
        } else {
            target.setAllowFlight(true);
            target.playSound(target.getLocation(), Sound.BLOCK_ANVIL_HIT, 1, 1);
            new PlayerMessage("fly.other-enable", "console", target);
            new ConsoleMessage("fly.enable-other", target.getName());
        }
    }

}