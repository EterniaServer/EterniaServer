package storage.sql;

import center.Main;
import center.Vars;
import storage.Queries;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLSetup
{
    public MySQLSetup(Main plugin, int port, String host, String database, String username, String password)
    {
        try
        {
            synchronized (this)
            {
                if(plugin.getConnection() != null && !plugin.getConnection().isClosed())
                {
                    return;
                }
                Class.forName("java.sql.Driver");
                plugin.setConnection(DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password));
                Vars.consoleMessage("conectado-sucesso-mysql");
                try
                {
                    Statement mysql_conect = plugin.getConnection().createStatement();
                    mysql_conect.executeUpdate(Queries.createTable);
                    mysql_conect.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch(SQLException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}
