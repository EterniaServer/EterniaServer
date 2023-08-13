package br.com.eterniaserver.eterniaserver.modules.core;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Utils {

    private Utils() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    public static String[] getNameAndDisplay(CommandSender sender) {
        String senderName;
        String senderDisplay;
        if (sender instanceof Player player) {
            Entities.PlayerProfile playerProfile = EterniaLib.getDatabase().get(
                    Entities.PlayerProfile.class,
                    player.getUniqueId()
            );
            senderName = playerProfile.getPlayerName();
            senderDisplay = playerProfile.getPlayerDisplay();
        } else {
            senderName = sender.getName();
            senderDisplay = sender.getName();
        }

        return new String[] {senderName, senderDisplay};
    }

}
