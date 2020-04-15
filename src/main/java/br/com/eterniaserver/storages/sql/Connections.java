package br.com.eterniaserver.storages.sql;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;

import com.zaxxer.hikari.HikariDataSource;

import java.io.File;
import java.sql.*;

public class Connections {

    private final boolean mysql;
    private final EterniaServer plugin;
    private HikariDataSource hikari;

    public Connections(EterniaServer plugin, final boolean mysql) {
        this.plugin = plugin;
        this.mysql = mysql;
        this.Connect();
    }

    public void Connect() {
        if (mysql) {
            hikari = new HikariDataSource();
            hikari.setPoolName("EterniaServer MySQL Pool");
            hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
            hikari.addDataSourceProperty("serverName", EterniaServer.configs.getString("sql.host"));
            hikari.addDataSourceProperty("port", EterniaServer.configs.getString("sql.port"));
            hikari.addDataSourceProperty("databaseName", EterniaServer.configs.getString("sql.database"));
            hikari.addDataSourceProperty("user", EterniaServer.configs.getString("sql.user"));
            hikari.addDataSourceProperty("password", EterniaServer.configs.getString("sql.password"));
            hikari.setMaximumPoolSize(50);
            Messages.ConsoleMessage("server.mysql-ok");
        } else {
            hikari = new HikariDataSource();
            hikari.setPoolName("EterniaServer SQLite Pool");
            hikari.setDriverClassName("org.sqlite.JDBC");
            hikari.setJdbcUrl("jdbc:sqlite:" + new File(plugin.getDataFolder(), "eternia.db"));
            hikari.setMaximumPoolSize(50);
            Messages.ConsoleMessage("server.sql-ok");
        }
    }

    public boolean isClosed() {
        return hikari.isClosed();
    }

    public void Close() {
        hikari.close();
        Messages.ConsoleMessage("server.sql-finish");
    }

    public void executeSQLQuery(SQLCallback callback) {
        executeSQLQuery(callback, false);
    }

    public void executeSQLQuery(SQLCallback callback, boolean async) {
        SQLTask task = new SQLTask(this, callback);
        if(async) {
            task.executeAsync();
        } else {
            task.run();
        }
    }

    public Connection getConnection() throws SQLException {
        return hikari != null ? hikari.getConnection() : null;
    }

    public EterniaServer getPlugin() {
        return plugin;
    }
}
