package br.com.eterniaserver.modules;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.modules.teleportsmanager.commands.*;

import java.util.Objects;

public class TeleportsManager {

    public TeleportsManager(EterniaServer plugin) {
        if (EterniaServer.configs.getBoolean("modules.teleports")) {
            Objects.requireNonNull(plugin.getCommand("back")).setExecutor(new Back(plugin));
            Objects.requireNonNull(plugin.getCommand("spawn")).setExecutor(new Spawn(plugin));
            Objects.requireNonNull(plugin.getCommand("setspawn")).setExecutor(new SetSpawn());
            Objects.requireNonNull(plugin.getCommand("warp")).setExecutor(new Warp(plugin));
            Objects.requireNonNull(plugin.getCommand("setwarp")).setExecutor(new SetWarp());
            Objects.requireNonNull(plugin.getCommand("delwarp")).setExecutor(new DelWarp());
            Objects.requireNonNull(plugin.getCommand("listwarp")).setExecutor(new ListWarp(plugin));
            Objects.requireNonNull(plugin.getCommand("shop")).setExecutor(new Shop(plugin));
            Objects.requireNonNull(plugin.getCommand("setshop")).setExecutor(new SetShop());
            Objects.requireNonNull(plugin.getCommand("teleportaccept")).setExecutor(new TeleportAccept(plugin));
            Objects.requireNonNull(plugin.getCommand("teleportdeny")).setExecutor(new TeleportDeny());
            Objects.requireNonNull(plugin.getCommand("teleporttoplayer")).setExecutor(new TeleportToPlayer());
            Objects.requireNonNull(plugin.getCommand("teleportall")).setExecutor(new TeleportAll());
            Messages.ConsoleMessage("modules.enable", "Teleports");
        } else {
            Messages.ConsoleMessage("modules.disable", "Teleports");
        }
    }

}
