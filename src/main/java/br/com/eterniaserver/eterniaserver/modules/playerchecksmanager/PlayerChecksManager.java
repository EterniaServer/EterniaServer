package br.com.eterniaserver.eterniaserver.modules.playerchecksmanager;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Strings;
import br.com.eterniaserver.eterniaserver.configs.Vars;
import br.com.eterniaserver.eterniaserver.modules.playerchecksmanager.tasks.Checks;
import br.com.eterniaserver.eterniaserver.modules.teleportsmanager.TeleportsManager;

import io.papermc.lib.PaperLib;

public class PlayerChecksManager {

    public PlayerChecksManager(EterniaServer plugin, Messages messages, Strings strings, TeleportsManager teleportsManager, Vars vars) {
        if (plugin.serverConfig.getBoolean("modules.playerchecks")) {
            if (PaperLib.isPaper()) {
                if (plugin.serverConfig.getBoolean("server.async-check")) {
                    new Checks(plugin, messages, strings, vars, teleportsManager).runTaskTimerAsynchronously(plugin, 20L, plugin.serverConfig.getInt("server.checks") * 20);
                } else {
                    new Checks(plugin, messages, strings, vars, teleportsManager).runTaskTimer(plugin, 20L, plugin.serverConfig.getInt("server.checks") * 20);
                }
            } else {
                new Checks(plugin, messages, strings, vars, teleportsManager).runTaskTimer(plugin, 20L, plugin.serverConfig.getInt("server.checks") * 20);
            }
            messages.sendConsole("modules.enable", "%module%", "Player-Checks");
        } else {
            messages.sendConsole("modules.disable", "%module%", "Player-Checks");
        }
    }

}
