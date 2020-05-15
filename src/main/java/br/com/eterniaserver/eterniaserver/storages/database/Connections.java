package br.com.eterniaserver.eterniaserver.storages.database;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;

import com.zaxxer.hikari.HikariDataSource;

import java.io.File;
import java.sql.*;

public class Connections {

    private final boolean mysql;
    private final EterniaServer plugin;
    private final Messages messages;
    private HikariDataSource hikari;

    public Connections(EterniaServer plugin, final boolean mysql, Messages messages) {
        this.plugin = plugin;
        this.messages = messages;
        this.mysql = mysql;
        this.Connect();
    }

    public void Connect() {
        hikari = new HikariDataSource();
        if (mysql) {
            hikari.setPoolName("EterniaServer MySQL Pool");
            hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
            hikari.addDataSourceProperty("serverName", plugin.serverConfig.getString("sql.host"));
            hikari.addDataSourceProperty("port", plugin.serverConfig.getString("sql.port"));
            hikari.addDataSourceProperty("databaseName", plugin.serverConfig.getString("sql.database"));
            hikari.addDataSourceProperty("user", plugin.serverConfig.getString("sql.user"));
            hikari.addDataSourceProperty("password", plugin.serverConfig.getString("sql.password"));
            hikari.setMaximumPoolSize(50);
            messages.sendConsole("server.mysql-ok");
        } else {
            hikari.setPoolName("EterniaServer SQLite Pool");
            hikari.setDriverClassName("org.sqlite.JDBC");
            hikari.setJdbcUrl("jdbc:sqlite:" + new File(plugin.getDataFolder(), "eternia.db"));
            hikari.setMaximumPoolSize(50);
            messages.sendConsole("server.sql-ok");
        }
    }

    public boolean isClosed() {
        return hikari.isClosed();
    }

    public void Close() {
        hikari.close();
        if (mysql) {
            messages.sendConsole("server.mysql-finish");
        } else {
            messages.sendConsole("server.sql-finish");
        }
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
