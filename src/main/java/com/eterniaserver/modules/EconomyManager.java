package com.eterniaserver.modules;

import com.eterniaserver.EterniaServer;
import com.eterniaserver.configs.CVar;
import com.eterniaserver.configs.methods.ConsoleMessage;
import com.eterniaserver.modules.economymanager.commands.*;
import com.eterniaserver.modules.economymanager.vault.VaultHook;

import org.bukkit.Bukkit;

import java.util.Objects;

public class EconomyManager {
    public EconomyManager(EterniaServer plugin) {
        if (CVar.getBool("modules.economy")) {
            if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
                final VaultHook vaultHook = new VaultHook();
                vaultHook.hook();
            } else {
                new ConsoleMessage("server.no-vault");
            }
            Objects.requireNonNull(plugin.getCommand("pay")).setExecutor(new Pay());
            Objects.requireNonNull(plugin.getCommand("money")).setExecutor(new Money());
            Objects.requireNonNull(plugin.getCommand("baltop")).setExecutor(new Baltop());
            Objects.requireNonNull(plugin.getCommand("eco")).setExecutor(new Eco());
            new ConsoleMessage("modules.enable", "Economy");
        } else {
            new ConsoleMessage("modules.disable", "Economy");
        }
    }
}
