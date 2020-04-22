package br.com.eterniaserver.dependencies.vault;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;

public class VaultHook {

    public VaultHook(EterniaServer plugin) {
        if (Bukkit.getPluginManager().isPluginEnabled("Vault") && EterniaServer.configs.getBoolean("modules.economy")) {
            ServicesManager servicesManager = plugin.getServer().getServicesManager();
            servicesManager.register(Economy.class, new VaultMethods(), plugin, ServicePriority.High);
        } else {
            Messages.ConsoleMessage("server.no-vault");
        }
    }

}
