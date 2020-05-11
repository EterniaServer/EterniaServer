package br.com.eterniaserver.eterniaserver.dependencies.vault;

import br.com.eterniaserver.eterniaserver.API.Money;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.player.PlayerManager;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;

public class VaultHook {

    public VaultHook(EterniaServer plugin, Messages messages, PlayerManager playerManager, Money moneyx) {
        if (plugin.getServer().getPluginManager().isPluginEnabled("Vault") && plugin.serverConfig.getBoolean("modules.economy")) {
            ServicesManager servicesManager = plugin.getServer().getServicesManager();
            servicesManager.register(Economy.class, new VaultMethods(plugin, playerManager, moneyx), plugin, ServicePriority.High);
        } else {
            messages.ConsoleMessage("server.no-vault");
        }
    }

}