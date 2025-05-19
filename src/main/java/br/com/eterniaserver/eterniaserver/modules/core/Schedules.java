package br.com.eterniaserver.eterniaserver.modules.core;

import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.chat.MessageOptions;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.events.AfkStatusEvent;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import br.com.eterniaserver.eterniaserver.modules.core.Entities.PlayerProfile;
import br.com.eterniaserver.eterniaserver.modules.core.Services.AfkService;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;


final class Schedules {

    private Schedules() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    static class MainTick extends BukkitRunnable {

        private final EterniaServer plugin;
        private final AfkService afkServices;

        public MainTick(final EterniaServer plugin, final AfkService afkServices) {
            this.plugin = plugin;
            this.afkServices = afkServices;
        }

        @Override
        public void run() {
            for (Player player : Bukkit.getOnlinePlayers()) {
                PlayerProfile playerProfile = EterniaLib.getDatabase().get(PlayerProfile.class, player.getUniqueId());

                checkAFK(player, playerProfile);
                checkPvP(player, playerProfile);
            }
        }

        private void checkAFK(Player player, PlayerProfile playerProfile) {
            if (playerProfile.isAfk() || !afkServices.areAfk(playerProfile)) {
                return;
            }

            MessageOptions messageOptions = new MessageOptions(
                    playerProfile.getPlayerName(),
                    playerProfile.getPlayerDisplay()
            );

            if (!plugin.getBoolean(Booleans.AFK_KICK) || player.hasPermission(plugin.getString(Strings.PERM_AFK))) {
                AfkStatusEvent event = new AfkStatusEvent(player, true, AfkStatusEvent.Cause.ACTIVITY);
                plugin.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return;
                }

                Component afkAutoEnterMessage = EterniaLib.getChatCommons().parseMessage(
                        Messages.AFK_AUTO_ENTER,
                        messageOptions
                );
                plugin.getServer().broadcast(afkAutoEnterMessage);
                playerProfile.setAfk(true);
                return;
            }

            AfkStatusEvent event = new AfkStatusEvent(player, false, AfkStatusEvent.Cause.KICK);
            plugin.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return;
            }

            Component afkKickMessage = EterniaLib.getChatCommons().parseMessage(
                    Messages.AFK_BROADCAST_KICK,
                    messageOptions
            );
            plugin.getServer().broadcast(afkKickMessage);
            player.kick(EterniaLib.getChatCommons().parseMessage(Messages.AFK_KICKED, new MessageOptions(false)));
        }

        private void checkPvP(Player player, PlayerProfile playerProfile) {
            if (!playerProfile.isOnPvP()) {
                return;
            }

            long seconds = TimeUnit.MILLISECONDS.toSeconds(
                    System.currentTimeMillis() - playerProfile.getPvpLastJoin()
            );

            if (seconds > plugin.getInteger(Integers.PVP_TIME)) {
                playerProfile.setOnPvP(false);
                EterniaLib.getChatCommons().sendMessage(player, Messages.LEFT_PVP);
            }
        }
    }

}
