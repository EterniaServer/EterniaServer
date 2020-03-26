package eternia;

import eternia.configs.CVar;
import eternia.managers.*;
import eternia.storage.sqlsetup.Queries;
import eternia.vault.VaultHook;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class EterniaServer extends JavaPlugin {

    public static FileConfiguration messagesConfig;
    public static FileConfiguration blocks;
    public static Queries sqlcon;
    private static EterniaServer plugin;

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        setMessages();
        setBlockBreak();
        setStorage();
        setCommands();
        setEvents();
        setVault();
    }

    @Override
    public void onDisable() {
        sqlcon.Close();
    }

    private void setVault() {
        if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            final VaultHook vaultHook = new VaultHook();
            vaultHook.hook();
        } else {
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    private void setEvents() {
        new EventManager(this);
    }

    private void setCommands() {
        new CommandManager(this);
    }

    private void setStorage() {
        new StorageManager(CVar.getBool("sql.mysql"));
    }

    private void setBlockBreak() {
        new BlockBreakManager(this);
    }

    private void setMessages() {
        new MessagesManager(this);
    }

    public static EterniaServer getMain() {
        return plugin;
    }

    public static FileConfiguration getBlocks() {
        return blocks;
    }

    public static FileConfiguration getMessages() {
        return messagesConfig;
    }

}