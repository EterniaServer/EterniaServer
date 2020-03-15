package storage;

import center.Main;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

public class Vault {
    private final Main plugin;

    public Vault(Main plugin) {
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
        Main.econ = rsp.getProvider();
        return true;
    }
}