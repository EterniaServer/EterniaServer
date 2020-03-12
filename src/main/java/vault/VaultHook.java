package vault;

import center.Main;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

public class VaultHook
{
    private Main plugin = Main.getMain();
    private Economy provider;
    public void hook()
    {
        provider = plugin.economyImplementer;
        Bukkit.getServicesManager().register(Economy.class, this.provider, this.plugin, ServicePriority.Normal);
        Bukkit.getConsoleSender().sendMessage("Vault conectado ao ETERNIA");
    }
    public void unhook() { Bukkit.getServicesManager().unregister(Economy.class, this.provider); }
}
