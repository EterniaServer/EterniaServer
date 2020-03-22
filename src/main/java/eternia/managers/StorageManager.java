package eternia.managers;

import eternia.EterniaServer;
import eternia.storage.sqlsetup.MySQLSetup;
import eternia.storage.sqlsetup.SQLiteSetup;

public class StorageManager {
    public StorageManager(EterniaServer plugin, boolean mysql) {
        String host = plugin.getConfig().getString("host");
        String database = plugin.getConfig().getString("database");
        String username = plugin.getConfig().getString("usuario");
        String password = plugin.getConfig().getString("senha");
        if (mysql) {
            new MySQLSetup(host, database, username, password);
        } else {
            new SQLiteSetup();
        }
    }
}