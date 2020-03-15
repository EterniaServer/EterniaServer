package storage.sql;

import center.Main;
import center.Vars;
import storage.Queries;
import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteSetup {
    public SQLiteSetup(Main plugin) {
        try {
            synchronized (this) {
                if (plugin.getConnection() != null && !plugin.getConnection().isClosed()) {
                    return;
                }
                File dataFolder = new File(plugin.getDataFolder(), "eternia.db");
                boolean fileExists = dataFolder.exists();
                boolean fileCreated = false;
                if (!fileExists) {
                    try {
                        fileCreated = dataFolder.createNewFile();
                    } catch (IOException e) {
                        Vars.consoleReplaceMessage("erro-sqlite", "eternia");
                    }
                }
                fileExists = dataFolder.exists();
                if (fileCreated || fileExists) {
                    Class.forName("org.sqlite.JDBC");
                    plugin.setConnection(DriverManager.getConnection("jdbc:sqlite:" + dataFolder));
                    Vars.consoleMessage("conectado-sucesso-sql");
                    try {
                        Statement sqlite_conect = plugin.getConnection().createStatement();
                        sqlite_conect.executeUpdate(Queries.createTable);
                        sqlite_conect.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}