package com.eterniaserver.modules;

import com.eterniaserver.EterniaServer;
import com.eterniaserver.configs.CVar;
import com.eterniaserver.configs.methods.ConsoleMessage;
import com.eterniaserver.modules.teleportsmanager.commands.*;

import java.util.Objects;

public class TeleportsManager {

    public TeleportsManager(EterniaServer plugin) {
        if (CVar.getBool("modules.teleports")) {
            Objects.requireNonNull(plugin.getCommand("back")).setExecutor(new Back());
            Objects.requireNonNull(plugin.getCommand("spawn")).setExecutor(new Spawn());
            Objects.requireNonNull(plugin.getCommand("setspawn")).setExecutor(new SetSpawn());
            Objects.requireNonNull(plugin.getCommand("warp")).setExecutor(new Warp());
            Objects.requireNonNull(plugin.getCommand("setwarp")).setExecutor(new SetWarp());
            Objects.requireNonNull(plugin.getCommand("delwarp")).setExecutor(new DelWarp());
            Objects.requireNonNull(plugin.getCommand("listwarp")).setExecutor(new ListWarp());
            Objects.requireNonNull(plugin.getCommand("shop")).setExecutor(new Shop());
            Objects.requireNonNull(plugin.getCommand("setshop")).setExecutor(new SetShop());
            Objects.requireNonNull(plugin.getCommand("teleportaccept")).setExecutor(new TeleportAccept());
            Objects.requireNonNull(plugin.getCommand("teleportdeny")).setExecutor(new TeleportDeny());
            Objects.requireNonNull(plugin.getCommand("teleporttoplayer")).setExecutor(new TeleportToPlayer());
            Objects.requireNonNull(plugin.getCommand("teleportall")).setExecutor(new TeleportAll());
            new ConsoleMessage("modules.enable", "Teleports");
        } else {
            new ConsoleMessage("modules.disable", "Teleports");
        }
    }

}
