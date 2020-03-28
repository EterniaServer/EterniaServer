package eternia.storage.sqlsetup;

import eternia.EterniaServer;

public class MySQLSetup {
    public MySQLSetup() {
        String host = EterniaServer.getMain().getConfig().getString("sql.host");
        String database = EterniaServer.getMain().getConfig().getString("sql.database");
        String username = EterniaServer.getMain().getConfig().getString("sql.user");
        String password = EterniaServer.getMain().getConfig().getString("sql.password");
        String createTable = "CREATE TABLE IF NOT EXISTS eternia " +
                "(`NAME` varchar(32) NOT NULL, " +
                "`XP` int(11) NOT NULL, " +
                "`BALANCE` double(22) NOT NULL);";
        EterniaServer.sqlcon = new Queries(host, database, username, password, true);
        EterniaServer.sqlcon.Update(createTable);
    }
}