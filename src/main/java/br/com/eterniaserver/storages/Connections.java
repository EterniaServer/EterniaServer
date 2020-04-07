package br.com.eterniaserver.storages;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.methods.ConsoleMessage;

import org.bukkit.Bukkit;

import java.io.File;
import java.sql.*;

public class Connections {

    private Connection con;
    private final boolean mysql;
    private final EterniaServer plugin;

    public Connections(EterniaServer plugin, final boolean mysql) {
        this.plugin = plugin;
        this.mysql = mysql;
        this.Connect();
    }

    public void Connect() {
        if (mysql) {
            try {
                String host = plugin.getConfig().getString("sql.host");
                String port = plugin.getConfig().getString("sql.port");
                String database = plugin.getConfig().getString("sql.database");
                String username = plugin.getConfig().getString("sql.user");
                String password = plugin.getConfig().getString("sql.password");
                this.con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", username, password);
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
