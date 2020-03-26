package eternia.api;

import eternia.EterniaServer;
import eternia.configs.Vars;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ExpAPI {

    public static Integer getExp(UUID uuid) {
        if (Vars.xp.containsKey(uuid.toString())) {
            return Vars.xp.get(uuid.toString());
        }
        int xp = 0;
        try {
            final ResultSet rs = EterniaServer.sqlcon.Query("SELECT XP FROM eternia WHERE UUID='" + uuid.toString() + "';");
            if (rs.next()) {
                rs.getInt("XP");
            }
            xp = rs.getInt("XP");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Vars.xp.put(uuid.toString(), xp);
        return xp;
    }

    public static void setExp(UUID uuid, int valor) {
        Vars.xp.remove(uuid.toString());
        Vars.xp.put(uuid.toString(), valor);
        EterniaServer.sqlcon.Update("UPDATE eternia SET BALANCE='" + valor + "' WHERE UUID='" + uuid.toString() + "';");
    }

    public static void addExp(UUID uuid, int valor) {
        setExp(uuid, getExp(uuid) + valor);
    }

    public static Integer takeExp(UUID uuid) {
        setExp(uuid, 0);
        return getExp(uuid);
    }

}
