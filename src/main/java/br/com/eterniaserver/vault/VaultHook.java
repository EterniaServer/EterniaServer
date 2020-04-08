package br.com.eterniaserver.vault;

import br.com.eterniaserver.EterniaServer;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;

public class VaultHook {

    public void hook(EterniaServer plugin) {
        final Plugin vault = Bukkit.getPluginManager().getPlugin("Vault");
        final SystemEconomy vaultEcoHook = new SystemEconomy();
        assert vault != null;
        Bukkit.getServicesManager().register(Economy.class, vaultEcoHook, vault, ServicePriority.High);
        RegisteredServiceProvider<Chat> rsp = plugin.getServer().getServicesManager().getRegistration(Chat.class);
        assert rsp != null;
        EterniaServer.chat = rsp.getProvider();
    }

}
