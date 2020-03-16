package center;

import center.managers.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.sql.Connection;

public class Main extends JavaPlugin {
    private Connection connection;
    private static Main mainclasse;
    public static FileConfiguration messagesConfig;
    public static Economy econ;
    @Override
    public void onEnable() {
        mainclasse = this;
        saveDefaultConfig();
        setMessages();
        setStorage();
        setCommands();
        setEvents();
        setEconomy();
    }

    private void setEconomy() {
        if (!new VaultManager(this).load()) {
            Vars.consoleMessage("no-vault");
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
        new StorageManager(this, Vars.getBool("mysql"));
    }

    private void setMessages() {
        new MessagesManager(this);
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public static Main getMain() {
        return mainclasse;
    }

    public static FileConfiguration getMessages() {
        return messagesConfig;
    }

    public static Economy getEconomy() {
        return econ;
    }
}