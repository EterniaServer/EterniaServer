package br.com.eterniaserver.modules;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.modules.teleportsmanager.commands.*;
import br.com.eterniaserver.configs.CVar;
import br.com.eterniaserver.configs.methods.ConsoleMessage;

import java.util.Objects;

public class TeleportsManager {

    public TeleportsManager(EterniaServer plugin) {
        if (CVar.getBool("modules.teleports")) {
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
            new ConsoleMessage("modules.enable", "Teleports");
        } else {
            new ConsoleMessage("modules.disable", "Teleports");
        }
    }

}
