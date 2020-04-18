package br.com.eterniaserver.modules.experiencemanager.sql;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Vars;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.atomic.AtomicReference;

public class XPAPI {

    public static Integer getExp(String playerName) {
        if (Vars.xp.containsKey(playerName)) {
            return Vars.xp.get(playerName);
        }

        AtomicReference<Integer> xp = new AtomicReference<>(0);
        final String querie = "SELECT xp FROM " + EterniaServer.configs.getString("sql.table-xp") + " WHERE player_name='" + playerName + "';";
        EterniaServer.connection.executeSQLQuery(connection -> {
            PreparedStatement getshop = connection.prepareStatement(querie);
            ResultSet resultSet = getshop.executeQuery();
            if (resultSet.next() && resultSet.getInt("xp") != 0) {
                xp.set(resultSet.getInt("xp"));
            }
        });

        Vars.xp.put(playerName, xp.get());
        return xp.get();
    }

    public static void setExp(String playerName, int valor) {
        Vars.xp.put(playerName, valor);
        final String querie = "UPDATE " + EterniaServer.configs.getString("sql.table-xp") + " SET xp='" + valor + "' WHERE player_name='" + playerName + "';";
        EterniaServer.connection.executeSQLQuery(connection -> {
            PreparedStatement setexp = connection.prepareStatement(querie);
            setexp.execute();
            setexp.close();
        }, true);
    }

    public static void addExp(String playerName, int valor) {
        setExp(playerName, getExp(playerName) + valor);
    }

    public static Integer takeExp(String playerName) {
        setExp(playerName, 0);
        return getExp(playerName);
    }

}
