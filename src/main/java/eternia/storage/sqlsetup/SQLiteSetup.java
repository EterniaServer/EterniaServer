package eternia.storage.sqlsetup;

import eternia.EterniaServer;

public class SQLiteSetup {
    public SQLiteSetup() {
        String host = EterniaServer.getMain().getConfig().getString("host");
        String database = EterniaServer.getMain().getConfig().getString("database");
        String username = EterniaServer.getMain().getConfig().getString("usuario");
        String password = EterniaServer.getMain().getConfig().getString("senha");
        String createTable = "CREATE TABLE IF NOT EXISTS eternia " +
                "(`UUID` varchar(32) NOT NULL, " +
                "`NAME` varchar(32) NOT NULL, " +
                "`XP` int(11) NOT NULL, " +
                "`BALANCE` double(22) NOT NULL);";
        EterniaServer.sqlcon = new Queries(host, database, username, password, false);
        EterniaServer.sqlcon.Update(createTable);
    }
}