package com.eterniaserver.configs;

import com.eterniaserver.EterniaServer;

public class CVar {
    public static String getString(String valor) {
        return EterniaServer.getMain().getConfig().getString(valor);
    }

    public static boolean getBool(String valor) {
        return EterniaServer.getMain().getConfig().getBoolean(valor);
    }

    public static double getDouble(String valor) {
        return EterniaServer.getMain().getConfig().getDouble(valor);
    }

    public static int getInt(String valor) {
        return EterniaServer.getMain().getConfig().getInt(valor);
    }

}
