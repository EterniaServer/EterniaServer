package br.com.eterniaserver.eterniaserver.configurations.dependencies;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.EconomyRelated;
import br.com.eterniaserver.eterniaserver.enums.Booleans;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;

public class VaultHook {

    public VaultHook(EterniaServer plugin) {
        if (plugin.getServer().getPluginManager().isPluginEnabled("Vault") && EterniaServer.getBoolean(Booleans.MODULE_ECONOMY)) {
            ServicesManager servicesManager = plugin.getServer().getServicesManager();
            servicesManager.register(Economy.class, new VaultInterface(), plugin, ServicePriority.High);
        } else if (plugin.getServer().getPluginManager().isPluginEnabled("Vault") && !EterniaServer.getBoolean(Booleans.MODULE_ECONOMY)) {
            RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp != null) {
                EconomyRelated.setEconomy(rsp.getProvider());
            }
        } else {
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

}
