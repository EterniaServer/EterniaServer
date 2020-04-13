package br.com.eterniaserver.configs;

import br.com.eterniaserver.EterniaServer;

public class Checks {

    public static boolean isTp(String jogador) {
        long secondsLeft = ((Vars.teleporting.get(jogador) / 1000) + EterniaServer.configs.getInt("server.cooldown")) - (System.currentTimeMillis() / 1000);
        return secondsLeft >= 0;
    }

}
