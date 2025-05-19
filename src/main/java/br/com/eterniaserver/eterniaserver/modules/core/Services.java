package br.com.eterniaserver.eterniaserver.modules.core;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.chat.MessageOptions;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.events.AfkStatusEvent;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import br.com.eterniaserver.eterniaserver.modules.core.Entities.PlayerProfile;
import br.com.eterniaserver.eterniaserver.api.interfaces.GUIAPI;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;

import net.kyori.adventure.text.Component;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


final class Services {

    private Services() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    static class AfkService {

        private final EterniaServer plugin;

        protected AfkService(final EterniaServer plugin) {
            this.plugin = plugin;
        }

        protected boolean areAfk(PlayerProfile playerProfile) {
            int secondsAfk = (int) TimeUnit.MILLISECONDS.toSeconds(
                    System.currentTimeMillis() - playerProfile.getLastMove()
            );
            int limitTime = plugin.getInteger(Integers.AFK_TIMER);

            return secondsAfk > limitTime;
        }

        protected void exitFromAfk(Player player, PlayerProfile playerProfile, AfkStatusEvent.Cause cause) {
            if (!playerProfile.isAfk()) {
                playerProfile.setLastMove(System.currentTimeMillis());
                return;
            }

            AfkStatusEvent event = new AfkStatusEvent(player, false, cause);
            plugin.getServer().getPluginManager().callEvent(event);
            
            if (event.isCancelled()) {
                return;
            }
            
            playerProfile.setAfk(false);
            playerProfile.setLastMove(System.currentTimeMillis());
            MessageOptions messageOptions = new MessageOptions(
                    playerProfile.getPlayerName(),
                    playerProfile.getPlayerDisplay()
            );
            Component message = EterniaLib.getChatCommons().parseMessage(Messages.AFK_LEAVE, messageOptions);
            plugin.getServer().broadcast(message);
        }
    }

    static class GUI implements GUIAPI {

        private final EterniaServer plugin;

        private final Map<String, Utils.GUIData> guisMap = new HashMap<>();

        protected GUI(final EterniaServer plugin) {
            this.plugin = plugin;
        }

        @Override
        public void createGUI(String guiName, ItemStack[] items) {
            Component title = EterniaLib.getChatCommons().parseColor(String.format(guiName, plugin.getString(Strings.GUI_SECRET))).compact();
            guisMap.put(guiName, new Utils.GUIData(title, items));
        }

        @Override
        public Inventory getGUI(String title, Player player) {
            Utils.GUIData guiData = guisMap.get(title);
            if (guiData == null) {
                return null;
            }

            Inventory gui = plugin.getServer().createInventory(player, guiData.getGuiSize(), guiData.getGuiTitle());
            for (int i = 0; i < guiData.getGuiSize(); i++) {
                gui.setItem(i, guiData.getItem(i));
            }
            return gui;
        }
    }

}
