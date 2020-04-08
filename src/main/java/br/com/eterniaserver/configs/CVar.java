package br.com.eterniaserver.configs;

import br.com.eterniaserver.EterniaServer;

public class CVar {

    public static String getString(String valor) {
        return EterniaServer.getConfigs().getString(valor);
    }

    public static boolean getBool(String valor) {
        return EterniaServer.getConfigs().getBoolean(valor);
    }

    public static double getDouble(String valor) {
        return EterniaServer.getConfigs().getDouble(valor);
    }

    public static int getInt(String valor) {
        return EterniaServer.getConfigs().getInt(valor);
    }

}
