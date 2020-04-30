package br.com.eterniaserver.dependencies.vault;

import br.com.eterniaserver.EterniaServer;

public class VaultUnHook {

    public VaultUnHook(EterniaServer plugin) {
        if (plugin.serverConfig.getBoolean("modules.economy")) {
            plugin.connections.Close();
        }
    }

}
