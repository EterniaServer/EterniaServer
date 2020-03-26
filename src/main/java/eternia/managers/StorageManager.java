package eternia.managers;

import eternia.storage.sqlsetup.MySQLSetup;
import eternia.storage.sqlsetup.SQLiteSetup;

public class StorageManager {
    public StorageManager(boolean mysql) {
        if (mysql) {
            new MySQLSetup();
        } else {
            new SQLiteSetup();
        }
    }
}