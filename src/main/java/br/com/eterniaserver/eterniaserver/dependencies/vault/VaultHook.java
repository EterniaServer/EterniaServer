package br.com.eterniaserver.eterniaserver.dependencies.vault;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.core.PluginVars;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;

public class VaultHook {

    public VaultHook(EterniaServer plugin) {
        if (plugin.getServer().getPluginManager().isPluginEnabled("Vault") && EterniaServer.configs.moduleEconomy) {
            ServicesManager servicesManager = plugin.getServer().getServicesManager();
            servicesManager.register(Economy.class, new VaultInterface(), plugin, ServicePriority.High);
        } else if (plugin.getServer().getPluginManager().isPluginEnabled("Vault") && !EterniaServer.configs.moduleEconomy) {
            RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp != null) {
                PluginVars.setEcon(rsp.getProvider());
            }
        } else {
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

}
