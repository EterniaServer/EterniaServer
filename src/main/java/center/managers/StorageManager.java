package center.managers;

import center.Main;
import storage.*;

public class StorageManager {
    public StorageManager(Main plugin, boolean mysql) {
        if (mysql) {
            int port = plugin.getConfig().getInt("porta");
            String host = plugin.getConfig().getString("host");
            String database = plugin.getConfig().getString("database");
            String username = plugin.getConfig().getString("usuario");
            String password = plugin.getConfig().getString("senha");
            new MySQLSetup(port, host, database, username, password);
        } else {
            new SQLiteSetup();
        }
    }
}