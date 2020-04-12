package br.com.eterniaserver.vault;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;

public class VaultHook {

    public VaultHook(EterniaServer plugin) {
        if (Bukkit.getPluginManager().isPluginEnabled("Vault") && EterniaServer.configs.getBoolean("modules.economy")) {
            final Plugin vault = Bukkit.getPluginManager().getPlugin("Vault");
            final SystemEconomy vaultEcoHook = new SystemEconomy();
            assert vault != null;
            Bukkit.getServicesManager().register(Economy.class, vaultEcoHook, vault, ServicePriority.High);
        } else {
            Messages.ConsoleMessage("server.no-vault");
            plugin.getPluginLoader().disablePlugin(plugin);
        }
    }

}
