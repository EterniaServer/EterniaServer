package com.eterniaserver.storage.sqlsetup;

import com.eterniaserver.EterniaServer;
import com.eterniaserver.configs.MVar;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.*;

public class Queries {

    private String HOST;
    private String DATABASE;
    private String USER;
    private String PASSWORD;
    private Connection con;
    private final boolean mysql;

    public Queries(final String host, final String database, final String user, final String password, final boolean mysql) {
        this.HOST = "";
        this.DATABASE = "";
        this.USER = "";
        this.PASSWORD = "";
        this.HOST = host;
        this.DATABASE = database;
        this.USER = user;
        this.PASSWORD = password;
        this.mysql = mysql;
        this.Connect();
    }

    public void Connect() {
        if (mysql) {
            try {
                this.con = DriverManager.getConnection("jdbc:mysql://" + this.HOST + ":3306/" + this.DATABASE + "?autoReconnect=true", this.USER, this.PASSWORD);
                MVar.consoleMessage("server.mysql-ok");
            } catch (SQLException e) {
                MVar.consoleReplaceMessage("server.sql-error", e.getMessage());
            }
        } else {
            try {
                File dataFolder = new File(EterniaServer.getMain().getDataFolder(), "eternia.db");
                this.con = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
                MVar.consoleMessage("server.sql-ok");
            } catch (SQLException e) {
                MVar.consoleReplaceMessage("server.sql-error", e.getMessage());
            }
        }
    }

    public void Close() {
        try {
            if (this.con != null) {
                this.con.close();
                MVar.consoleMessage("server.sql-finish");
            }
        } catch (SQLException e) {
            MVar.consoleReplaceMessage("server.sql-error", e.getMessage());
        }
    }

    public void Update(final String sql) {
        Bukkit.getScheduler().runTaskAsynchronously(EterniaServer.getMain(), () -> {
            try {
                con.createStatement().executeUpdate(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public ResultSet Query(final String qry) {
        ResultSet rs = null;
        try {
            final Statement st = this.con.createStatement();
            rs = st.executeQuery(qry);
        } catch (SQLException e) {
            this.Connect();
            e.printStackTrace();
        }
        return rs;
    }

}
