package storage;

import center.Main;
import center.Vars;

import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteSetup {
    public SQLiteSetup() {
        try {
            synchronized (Main.getMain()) {
                if (Main.getMain().getConnection() != null && !Main.getMain().getConnection().isClosed()) {
                    return;
                }
                File dataFolder = new File(Main.getMain().getDataFolder(), "eternia.db");
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
                    Main.getMain().setConnection(DriverManager.getConnection("jdbc:sqlite:" + dataFolder));
                    Vars.consoleMessage("conectado-sucesso-sql");
                    try {
                        Statement sqlite_conect = Main.getMain().getConnection().createStatement();
                        String createTable = "CREATE TABLE IF NOT EXISTS eternia " +
                                "(`UUID` varchar(32) NOT NULL, " +
                                "`NAME` varchar(32) NOT NULL, " +
                                "`XP` int(11) NOT NULL);";
                        sqlite_conect.executeUpdate(createTable);
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