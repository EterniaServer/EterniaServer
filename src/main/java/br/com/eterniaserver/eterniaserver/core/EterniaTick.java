package br.com.eterniaserver.eterniaserver.core;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;
import br.com.eterniaserver.paperlib.PaperLib;
import br.com.eterniaserver.eterniaserver.objects.User;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


public class EterniaTick extends BukkitRunnable {

    private final EterniaServer plugin;

    public EterniaTick(final EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player != null) {
                User user = new User(player);

                refreshPlayers(user);
            }
        }
    }

    private void refreshPlayers(User user) {
        EterniaServer.getUserAPI().setNameOnline(user.getName(), user.getUUID());
        EterniaServer.getUserAPI().setNameOnline(user.getDisplayName(), user.getUUID());
    }

}