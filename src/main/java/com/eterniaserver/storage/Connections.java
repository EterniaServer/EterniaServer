package com.eterniaserver.storage;

import com.eterniaserver.EterniaServer;
import com.eterniaserver.configs.methods.ConsoleMessage;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.*;

public class Connections {

    private String HOST;
    private String DATABASE;
    private String USER;
    private String PASSWORD;
    private Connection con;
    private final boolean mysql;
    private final EterniaServer plugin;

    public Connections(EterniaServer plugin, final String host, final String database, final String user, final String password, final boolean mysql) {
        this.plugin = plugin;
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
                new ConsoleMessage("server.mysql-ok");
            } catch (SQLException e) {
                new ConsoleMessage("server.sql-error", e.getMessage());
            }
        } else {
            try {
                File dataFolder = new File(plugin.getDataFolder(), "eternia.db");
                this.con = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
                new ConsoleMessage("server.sql-ok");
            } catch (SQLException e) {
                new ConsoleMessage("server.sql-error", e.getMessage());
            }
        }
    }

    public void Close() {
        try {
            if (this.con != null) {
                this.con.close();
                new ConsoleMessage("server.sql-finish");
            }
        } catch (SQLException e) {
            new ConsoleMessage("server.sql-error", e.getMessage());
        }
    }

    public void Update(final String sql) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
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
