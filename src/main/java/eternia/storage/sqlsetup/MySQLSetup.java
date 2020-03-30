package eternia.storage.sqlsetup;

import eternia.EterniaServer;

public class MySQLSetup {
    public MySQLSetup() {
        String host = EterniaServer.getMain().getConfig().getString("sql.host");
        String database = EterniaServer.getMain().getConfig().getString("sql.database");
        String username = EterniaServer.getMain().getConfig().getString("sql.user");
        String password = EterniaServer.getMain().getConfig().getString("sql.password");
        EterniaServer.sqlcon = new Queries(host, database, username, password, true);
        new Tables();
    }
}