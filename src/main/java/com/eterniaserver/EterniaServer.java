package com.eterniaserver;

import com.eterniaserver.configs.CVar;
import com.eterniaserver.configs.MVar;
import com.eterniaserver.managers.*;
import com.eterniaserver.storage.Queries;
import com.eterniaserver.vault.VaultHook;
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
        testPaper();
    }

    @Override
    public void onDisable() {
        sqlcon.Close();
    }

    private void testPaper() {
        boolean isPapermc = false;
        try {
            isPapermc = Class.forName("com.destroystokyo.paper.VersionHistoryManager$VersionData") != null;
        } catch (ClassNotFoundException e) {
            MVar.consoleMessage("server.gotopaper");
        }

        if (isPapermc) {
            MVar.consoleMessage("server.usepaper");
        }
    }

    private void setVault() {
        if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            final VaultHook vaultHook = new VaultHook();
            vaultHook.hook();
        } else {
            MVar.consoleMessage("server.no-vault");
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