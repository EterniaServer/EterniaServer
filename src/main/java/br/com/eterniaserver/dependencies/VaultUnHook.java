package br.com.eterniaserver.dependencies;

import br.com.eterniaserver.EterniaServer;

public class VaultUnHook {

    public VaultUnHook() {
        if (EterniaServer.configs.getBoolean("modules.economy")) {
            EterniaServer.connection.Close();
        }
    }

}