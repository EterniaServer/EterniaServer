package eternia.player;

import eternia.configs.MVar;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PlayerFlyState {
    public static void selfFly(Player player) {
        if (player.getAllowFlight()) {
            player.setAllowFlight(false);
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_HIT, 1, 1);
            MVar.playerMessage("fly.disable", player);
        } else {
            player.setAllowFlight(true);
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_HIT, 1, 1);
            MVar.playerMessage("fly.enable", player);
        }
    }

    public static void otherFly(Player target) {
        if (target.getAllowFlight()) {
            target.setAllowFlight(false);
            target.playSound(target.getLocation(), Sound.BLOCK_ANVIL_HIT, 1, 1);
            MVar.playerReplaceMessage("fly.other-disable", "console", target);
            MVar.consoleReplaceMessage("fly.disable-other", target.getName());
        } else {
            target.setAllowFlight(true);
            target.playSound(target.getLocation(), Sound.BLOCK_ANVIL_HIT, 1, 1);
            MVar.playerReplaceMessage("fly.other-enable", "console", target);
            MVar.consoleReplaceMessage("fly.enable-other", target.getName());
        }
    }

}