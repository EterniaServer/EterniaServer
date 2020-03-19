package eternia.managers;

import eternia.EterniaServer;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultManager {
    private final EterniaServer plugin;

    public VaultManager(EterniaServer plugin) {
        this.plugin = plugin;
    }

    public boolean load() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        EterniaServer.econ = rsp.getProvider();
        return true;
    }
}