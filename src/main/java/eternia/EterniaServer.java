package eternia;

import eternia.configs.CVar;
import eternia.configs.MVar;
import eternia.managers.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.sql.Connection;

public class EterniaServer extends JavaPlugin {
    public Connection connection;
    public static FileConfiguration messagesConfig;
    public static FileConfiguration blocks;
    public static Economy econ;
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
        setEconomy();
    }

    private void setEconomy() {
        if (!new VaultManager(this).load()) {
            MVar.consoleMessage("no-vault");
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
        new StorageManager(this, CVar.getBool("mysql"));
    }

    private void setBlockBreak() {
        new BlockBreakManager(this);
    }

    private void setMessages() {
        new MessagesManager(this);
    }

    public Connection getConnection() {
        return connection;
    }

    public static EterniaServer getMain() {
        return plugin;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static FileConfiguration getBlocks() {
        return blocks;
    }

    public static FileConfiguration getMessages() {
        return messagesConfig;
    }
}