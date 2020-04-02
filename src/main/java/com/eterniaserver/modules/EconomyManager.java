package com.eterniaserver.modules;

import com.eterniaserver.EterniaServer;
import com.eterniaserver.configs.CVar;
import com.eterniaserver.configs.MVar;
import com.eterniaserver.configs.Vars;
import com.eterniaserver.modules.economymanager.commands.*;
import com.eterniaserver.modules.economymanager.vault.VaultHook;

import org.bukkit.Bukkit;

import java.util.Objects;

public class EconomyManager {
    public EconomyManager(EterniaServer plugin) {
        if (CVar.getBool("modules.economy")) {
            Vars.economy = true;
            if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
                final VaultHook vaultHook = new VaultHook();
                vaultHook.hook();
            } else {
                MVar.consoleMessage("server.no-vault");
            }
            Objects.requireNonNull(plugin.getCommand("pay")).setExecutor(new Pay());
            Objects.requireNonNull(plugin.getCommand("money")).setExecutor(new Money());
            Objects.requireNonNull(plugin.getCommand("baltop")).setExecutor(new Baltop());
            Objects.requireNonNull(plugin.getCommand("eco")).setExecutor(new Eco());
            MVar.consoleReplaceMessage("modules.enable", "Economy");
        } else {
            MVar.consoleReplaceMessage("modules.disable", "Economy");
        }
    }
}
