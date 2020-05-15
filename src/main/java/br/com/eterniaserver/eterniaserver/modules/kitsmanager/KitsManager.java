package br.com.eterniaserver.eterniaserver.modules.kitsmanager;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Strings;
import br.com.eterniaserver.eterniaserver.configs.Vars;
import br.com.eterniaserver.eterniaserver.modules.kitsmanager.commands.*;
import br.com.eterniaserver.eterniaserver.player.PlayerManager;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

public class KitsManager {

    private final EterniaServer plugin;
    private final PlayerManager playerManager;
    private final Vars vars;


    public KitsManager(EterniaServer plugin, Messages messages, PlayerManager playerManager, Strings strings, Vars vars) {
        this.plugin = plugin;
        this.playerManager = playerManager;
        this.vars = vars;
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
            plugin.getCommand("kit").setExecutor(new Kit(plugin, this, messages, strings));
            plugin.getCommand("kits").setExecutor(new Kits(plugin, messages, strings));
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
            plugin.connections.executeSQLQuery(connection -> {
                PreparedStatement putkit = connection.prepareStatement(querie);
                putkit.execute();
                putkit.close();
            }, true);
        } else {
            vars.kits_cooldown.put(kit + "." + jogador, data);
            final String querie = "INSERT INTO " + plugin.serverConfig.getString("sql.table-kits") + " (name, cooldown) VALUES ('" + kit + "." + jogador + "', '" + data + "');";
            plugin.connections.executeSQLQuery(connection -> {
                PreparedStatement putkit = connection.prepareStatement(querie);
                putkit.execute();
                putkit.close();
            }, true);
        }
    }

    public String getKitCooldown(final String jogador, final String kit) {
        if (vars.kits_cooldown.containsKey(kit + "." + jogador)) {
            return vars.kits_cooldown.get(kit + "." + jogador);
        }

        AtomicReference<String> cooldown = new AtomicReference<>("");
        if (playerManager.playerCooldownExist(jogador, kit)) {
            final String querie = "SELECT * FROM " + plugin.serverConfig.getString("sql.table-kits") + " WHERE name='" + kit + "." + jogador + "';";
            plugin.connections.executeSQLQuery(connection -> {
                PreparedStatement getkit = connection.prepareStatement(querie);
                ResultSet resultSet = getkit.executeQuery();
                if (resultSet.next() && resultSet.getString("cooldown") != null) {
                    cooldown.set(resultSet.getString("cooldown"));
                }
            });
        } else {
            cooldown.set("2020/01/01 00:00");
        }

        vars.kits_cooldown.put(kit + "." + jogador, cooldown.get());
        return cooldown.get();
    }

}
