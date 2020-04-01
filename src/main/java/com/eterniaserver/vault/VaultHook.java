package com.eterniaserver.vault;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;

public class VaultHook {
    public void hook() {
        final Plugin vault = Bukkit.getPluginManager().getPlugin("Vault");
        final SystemEconomy vaultEcoHook = new SystemEconomy();
        assert vault != null;
        Bukkit.getServicesManager().register(Economy.class, vaultEcoHook, vault, ServicePriority.High);
    }
}
