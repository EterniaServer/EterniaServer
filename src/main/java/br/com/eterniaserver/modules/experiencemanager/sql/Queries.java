package br.com.eterniaserver.modules.experiencemanager.sql;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Vars;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Queries {

    public static Integer getExp(String playerName) {
        if (Vars.xp.containsKey(playerName)) {
            return Vars.xp.get(playerName);
        }
        int xp = 0;
        try {
            final String querie = "SELECT xp FROM " + EterniaServer.configs.getString("sql.table-xp") + " WHERE player_name='" + playerName + "';";
            final ResultSet rs = EterniaServer.sqlcon.Query(querie);
            if (rs.next()) {
                rs.getInt("xp");
            }
            xp = rs.getInt("xp");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Vars.xp.put(playerName, xp);
        return xp;
    }

    public static void setExp(String playerName, int valor) {
        Vars.xp.remove(playerName);
        Vars.xp.put(playerName, valor);
        final String querie = "UPDATE " + EterniaServer.configs.getString("sql.table-xp") + " SET xp='" + valor + "' WHERE player_name='" + playerName + "';";
        EterniaServer.sqlcon.Update(querie);
    }

    public static void addExp(String playerName, int valor) {
        setExp(playerName, getExp(playerName) + valor);
    }

    public static Integer takeExp(String playerName) {
        setExp(playerName, 0);
        return getExp(playerName);
    }

}
