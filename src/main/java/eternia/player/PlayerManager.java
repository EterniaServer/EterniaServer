package eternia.player;

import eternia.EterniaServer;
import eternia.configs.CVar;
import org.bukkit.Bukkit;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

public class PlayerManager {
    public static boolean PlayerExist(final UUID uuid) {
        try {
            final ResultSet rs = EterniaServer.sqlcon.Query("SELECT * FROM eternia WHERE UUID='" + uuid.toString() + "';");
            return rs.next() && rs.getString("UUID") != null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void CreatePlayer(final UUID uuid) {
        if (!PlayerExist(uuid)) {
            EterniaServer.sqlcon.Update("INSERT INTO eternia (UUID, NAME, XP, BALANCE) VALUES ('" + uuid.toString() + "'" +
                    ", '" + Objects.requireNonNull(Bukkit.getPlayer(uuid)).getName() + "', '0','" + CVar.getDouble("money-inicial") + "');");
        }
    }
}