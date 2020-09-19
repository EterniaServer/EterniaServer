package br.com.eterniaserver.eterniaserver.dependencies.vault;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.generics.APIEconomy;
import br.com.eterniaserver.eterniaserver.generics.PluginConfigs;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;

public class VaultHook {

    public VaultHook(EterniaServer plugin) {
        if (plugin.getServer().getPluginManager().isPluginEnabled("Vault") && PluginConfigs.MODULE_ECONOMY) {
            ServicesManager servicesManager = plugin.getServer().getServicesManager();
            servicesManager.register(Economy.class, new VaultMethods(), plugin, ServicePriority.High);
        } else if (plugin.getServer().getPluginManager().isPluginEnabled("Vault")) {
            RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp != null) {
                APIEconomy.econ = rsp.getProvider();
            }
        } else {
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

}
