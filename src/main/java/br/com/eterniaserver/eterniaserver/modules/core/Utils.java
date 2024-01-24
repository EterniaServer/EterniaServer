package br.com.eterniaserver.eterniaserver.modules.core;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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

    public static class GUIData {

        private final ItemStack[] items;
        @Getter
        private final Component guiTitle;
        @Getter
        private final int guiSize;

        public GUIData(Component guiTitle, ItemStack[] items) {
            this.items = items;
            this.guiTitle = guiTitle;
            this.guiSize = items.length;
        }

        public ItemStack getItem(int slot) {
            return items[slot];
        }

    }
}
