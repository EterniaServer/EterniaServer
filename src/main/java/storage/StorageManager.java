package storage;

import center.Main;
import storage.sql.*;

public class StorageManager
{
    public StorageManager(Main plugin, boolean mysql)
    {
        if (mysql)
        {
            int port = plugin.getConfig().getInt("mysql.porta");
            String host = plugin.getConfig().getString("mysql.host");
            String database = plugin.getConfig().getString("mysql.database");
            String username = plugin.getConfig().getString("mysql.usuario");
            String password = plugin.getConfig().getString("mysql.senha");
            new MySQLSetup(plugin, port, host, database, username, password);
        }
        else
        {
            new SQLiteSetup(plugin);
        }
    }
}
