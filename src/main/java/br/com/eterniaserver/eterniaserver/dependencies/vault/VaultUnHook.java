package br.com.eterniaserver.eterniaserver.dependencies.vault;

import br.com.eterniaserver.eterniaserver.EterniaServer;

public class VaultUnHook {

    public VaultUnHook(EterniaServer plugin) {
        if (plugin.serverConfig.getBoolean("modules.economy")) plugin.connections.Close();
    }

}
