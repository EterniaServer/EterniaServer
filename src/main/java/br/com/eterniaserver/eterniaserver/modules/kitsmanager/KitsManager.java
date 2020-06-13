package br.com.eterniaserver.eterniaserver.modules.kitsmanager;

import br.com.eterniaserver.eternialib.EFiles;
import br.com.eterniaserver.eternialib.EQueries;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.objects.Vars;
import br.com.eterniaserver.eterniaserver.modules.kitsmanager.commands.*;
import br.com.eterniaserver.eterniaserver.objects.PlayerManager;

import co.aikar.commands.PaperCommandManager;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class KitsManager {

    private final EterniaServer plugin;
    private final PlayerManager playerManager;
    private final Vars vars;


    public KitsManager(EterniaServer plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getPlayerManager();
        this.vars = plugin.getVars();

        final EFiles messages = plugin.getEFiles();
        final PaperCommandManager manager = plugin.getManager();

        if (plugin.serverConfig.getBoolean("modules.kits")) {
            File commandsConfigFile = new File(plugin.getDataFolder(), "kits.yml");
            if (!commandsConfigFile.exists()) {
                plugin.saveResource("kits.yml", false);
            }
            plugin.kitConfig = new YamlConfiguration();
            try {
                plugin.kitConfig.load(commandsConfigFile);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
            manager.registerCommand(new KitSystem(plugin, this));
            messages.sendConsole("modules.enable", "%module%", "Kits");
        } else {
            messages.sendConsole("modules.disable", "%module%", "Kits");
        }
    }

    public void setKitCooldown(final String jogador, final String kit) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        final String data = format.format(date);
        if (playerManager.playerCooldownExist(jogador, kit)) {
            vars.kits_cooldown.put(kit + "." + jogador, data);
            final String querie = "UPDATE " + plugin.serverConfig.getString("sql.table-kits") + " SET cooldown='" + data + "' WHERE name='" + kit + "." + jogador + "';";
            EQueries.executeQuery(querie);
        } else {
            vars.kits_cooldown.put(kit + "." + jogador, data);
            final String querie = "INSERT INTO " + plugin.serverConfig.getString("sql.table-kits") + " (name, cooldown) VALUES ('" + kit + "." + jogador + "', '" + data + "');";
            EQueries.executeQuery(querie);
        }
    }

    public String getKitCooldown(final String jogador, final String kit) {
        if (vars.kits_cooldown.containsKey(kit + "." + jogador)) {
            return vars.kits_cooldown.get(kit + "." + jogador);
        }

        String cooldown;
        if (playerManager.playerCooldownExist(jogador, kit)) {
            final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-kits") + " WHERE name='" + kit + "." + jogador + "';";
            cooldown = EQueries.queryString(querie, "cooldown");
        } else {
            cooldown = "2020/01/01 00:00";
        }

        vars.kits_cooldown.put(kit + "." + jogador, cooldown);
        return cooldown;
    }

}
