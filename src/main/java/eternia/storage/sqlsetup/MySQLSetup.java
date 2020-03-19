package eternia.storage.sqlsetup;

import eternia.EterniaServer;
import eternia.configs.MVar;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLSetup {
    public MySQLSetup(int port, String host, String database, String username, String password) {
        try {
            synchronized (EterniaServer.getMain()) {
                if (EterniaServer.getMain().getConnection() != null && !EterniaServer.getMain().getConnection().isClosed()) {
                    return;
                }
                Class.forName("java.sql.Driver");
                EterniaServer.getMain().connection = (DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password));
                MVar.consoleMessage("conectado-sucesso-mysql");
                try {
                    Statement mysql_conect = EterniaServer.getMain().getConnection().createStatement();
                    String createTable = "CREATE TABLE IF NOT EXISTS eternia " +
                            "(`UUID` varchar(32) NOT NULL, " +
                            "`NAME` varchar(32) NOT NULL, " +
                            "`XP` int(11) NOT NULL);";
                    mysql_conect.executeUpdate(createTable);
                    mysql_conect.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}