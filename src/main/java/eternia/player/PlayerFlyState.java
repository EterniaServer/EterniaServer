package eternia.player;

import eternia.configs.MVar;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PlayerFlyState {
    public static void selfFly(Player player) {
        if (player.getAllowFlight()) {
            player.setAllowFlight(false);
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_HIT, 1, 1);
            MVar.playerMessage("desativar-voar", player);
        } else {
            player.setAllowFlight(true);
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_HIT, 1, 1);
            MVar.playerMessage("ativar-voar", player);
        }
    }

    public static void otherFly(Player target) {
        if (target.getAllowFlight()) {
            target.setAllowFlight(false);
            target.playSound(target.getLocation(), Sound.BLOCK_ANVIL_HIT, 1, 1);
            MVar.playerReplaceMessage("desativaram-voar", "console", target);
            MVar.consoleReplaceMessage("desativar-voar-de", target.getName());
        } else {
            target.setAllowFlight(true);
            target.playSound(target.getLocation(), Sound.BLOCK_ANVIL_HIT, 1, 1);
            MVar.playerReplaceMessage("ativaram-voar", "console", target);
            MVar.consoleReplaceMessage("ativar-voar-de", target.getName());
        }
    }

}