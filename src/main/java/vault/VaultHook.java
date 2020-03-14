package vault;

import center.Main;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;

public class VaultHook
{
    private final Main plugin;
    public VaultHook(Main plugin)
    {
        this.plugin = plugin;
        plugin.economyImplementer = new EconomyImplementer(plugin);
    }
    public void SetEconomyLink()
    {
        if(!plugin.getServer().getPluginManager().isPluginEnabled("Vault"))
        {
            return;
        }
        ServicesManager servicesManager = plugin.getServer().getServicesManager();
        servicesManager.register(Economy.class, new EconomyImplementer(plugin), plugin, ServicePriority.High);
    }
}
