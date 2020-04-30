package br.com.eterniaserver.modules.playerchecksmanager;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Strings;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.modules.playerchecksmanager.tasks.Checks;
import br.com.eterniaserver.modules.teleportsmanager.TeleportsManager;

public class PlayerChecksManager {

    public PlayerChecksManager(EterniaServer plugin, Messages messages, Strings strings, TeleportsManager teleportsManager, Vars vars) {
        if (plugin.serverConfig.getBoolean("modules.playerchecks")) {
            new Checks(plugin, messages, strings, vars, teleportsManager).runTaskTimer(plugin, 20L, plugin.serverConfig.getInt("server.checks") * 20);
            messages.ConsoleMessage("modules.enable", "%module%", "Player-Checks");
        } else {
            messages.ConsoleMessage("modules.disable", "%module%", "Player-Checks");
        }
    }

}
