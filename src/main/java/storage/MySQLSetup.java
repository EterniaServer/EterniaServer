package storage;

import center.Main;
import center.Vars;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLSetup {
    public MySQLSetup(int port, String host, String database, String username, String password) {
        try {
            synchronized (Main.getMain()) {
                if (Main.getMain().getConnection() != null && !Main.getMain().getConnection().isClosed()) {
                    return;
                }
                Class.forName("java.sql.Driver");
                Main.getMain().setConnection(DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password));
                Vars.consoleMessage("conectado-sucesso-mysql");
                try {
                    Statement mysql_conect = Main.getMain().getConnection().createStatement();
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