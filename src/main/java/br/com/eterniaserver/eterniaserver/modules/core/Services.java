package br.com.eterniaserver.eterniaserver.modules.core;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.events.AfkStatusEvent;
import br.com.eterniaserver.eterniaserver.api.interfaces.GUIAPI;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.objects.GUIData;
import br.com.eterniaserver.eterniaserver.objects.PlayerProfile;

import net.kyori.adventure.text.Component;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


final class Services {

    static class Afk {

        private final EterniaServer plugin;

        protected Afk(final EterniaServer plugin) {
            this.plugin = plugin;
        }

        protected boolean areAfk(PlayerProfile playerProfile) {
            final int secondsAfk = (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - playerProfile.getLastMove());
            final int limitTime = plugin.getInteger(Integers.AFK_TIMER);

            return secondsAfk > limitTime;
        }

        protected void exitFromAfk(Player player, PlayerProfile playerProfile, AfkStatusEvent.Cause cause) {
            if (!playerProfile.getAfk()) {
                playerProfile.setLastMove(System.currentTimeMillis());
                return;
            }

            final AfkStatusEvent event = new AfkStatusEvent(player, false, cause);
            plugin.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                playerProfile.setAfk(false);
                playerProfile.setLastMove(System.currentTimeMillis());
                Bukkit.broadcast(plugin.getMiniMessage(Messages.AFK_LEAVE, true, playerProfile.getName(), playerProfile.getDisplayName()));
            }
        }
    }

    static class GUI implements GUIAPI {

        private final EterniaServer plugin;

        private final Map<String, GUIData> guisMap = new HashMap<>();

        protected GUI(final EterniaServer plugin) {
            this.plugin = plugin;
        }

        @Override
        public void createGUI(String guiName, ItemStack[] items) {
            Component title = plugin.parseColor(String.format(guiName, plugin.getString(Strings.GUI_SECRET))).compact();
            guisMap.put(guiName, new GUIData(title, items));
        }

        @Override
        public boolean contains(Component title) {
            return guisMap.containsKey(title);
        }

        @Override
        public Inventory getGUI(String title, Player player) {
            GUIData guiData = guisMap.get(title);
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
