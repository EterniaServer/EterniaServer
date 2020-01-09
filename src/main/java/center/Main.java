package center;

import center.sql.*;
import commands.player.*;
import commands.staff.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin
{
    private static Economy econ;
    private Connection connection;
    private static Main mainclasse;
    @Override
    public void onEnable()
    {
        mainclasse = this;
        if (!new File(getDataFolder(), "config.yml").exists())
        {
            saveDefaultConfig();
        }
        FileConfiguration file = getConfig();
        long delay = file.getInt("intervalo") * 20;
        new NetherTrapCheck(file).runTaskTimer(this, 20L, delay);
        if (!setupEconomy())
        {
            this.getLogger().severe("Opa, vault não encontrado :/");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        boolean mysql = NetherTrapCheck.file.getBoolean("mysql");
        if (mysql)
        {
            mysqlSetup();
        }
        else
        {
            sqlitesetup();
        }
        this.getServer().getPluginManager().registerEvents(new SQL(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        Objects.requireNonNull(this.getCommand("suicide")).setExecutor(new Suicide());
        Objects.requireNonNull(this.getCommand("advice")).setExecutor(new Advice());
        Objects.requireNonNull(this.getCommand("discord")).setExecutor(new Discord());
        Objects.requireNonNull(this.getCommand("donation")).setExecutor(new Donation());
        Objects.requireNonNull(this.getCommand("rules")).setExecutor(new Rules());
        Objects.requireNonNull(this.getCommand("feed")).setExecutor(new Feed());
        Objects.requireNonNull(this.getCommand("hat")).setExecutor(new Hat());
        Objects.requireNonNull(this.getCommand("nome")).setExecutor(new Name());
        Objects.requireNonNull(this.getCommand("fly")).setExecutor(new Fly());
        Objects.requireNonNull(this.getCommand("goldenshovel")).setExecutor(new GoldenShovel());
        Objects.requireNonNull(this.getCommand("depositlvl")).setExecutor(new DepositLevel());
        Objects.requireNonNull(this.getCommand("withdrawlvl")).setExecutor(new WithdrawLevel());
        Objects.requireNonNull(this.getCommand("blocks")).setExecutor(new Blocks());
        Objects.requireNonNull(this.getCommand("back")).setExecutor(new Back());
        Objects.requireNonNull(this.getCommand("spectator")).setExecutor(new Spectator());
        Objects.requireNonNull(this.getCommand("survival")).setExecutor(new Survival());
        Objects.requireNonNull(this.getCommand("facebook")).setExecutor(new Facebook());
        Objects.requireNonNull(this.getCommand("bottlexp")).setExecutor(new Bottlexp());
        Objects.requireNonNull(this.getCommand("colors")).setExecutor(new Colors());
        Objects.requireNonNull(this.getCommand("spawn")).setExecutor(new Spawn());
        Objects.requireNonNull(this.getCommand("setspawn")).setExecutor(new SetSpawn());
    }
    public static Main getMain()
    {
        return mainclasse;
    }
    private boolean setupEconomy()
    {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null)
        {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
        {
            return false;
        }
        econ = rsp.getProvider();
        return true;
    }
    public final String table = "eternia";
    private void mysqlSetup()
    {
        try
        {
            synchronized (this)
            {
                if(getConnection() != null && !getConnection().isClosed())
                {
                    return;
                }
                Class.forName("java.sql.Driver");
                String host = NetherTrapCheck.file.getString("host");
                int port = NetherTrapCheck.file.getInt("porta");
                String password = NetherTrapCheck.file.getString("senha");
                String database = NetherTrapCheck.file.getString("database");
                String username = NetherTrapCheck.file.getString("usuario");
                setConnection(DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password));
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Conectado com sucesso a database");
                String MySQLCreate = "CREATE TABLE IF NOT EXISTS eternia (`UUID` varchar(32) NOT NULL, `NAME` varchar(32) NOT NULL, `XP` int(11) NOT NULL);";
                try
                {
                    Statement s = getConnection().createStatement();
                    s.executeUpdate(MySQLCreate);
                    s.close();
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
    private void sqlitesetup()
    {
        try
        {
            synchronized (this)
            {
                if(getConnection() != null && !getConnection().isClosed())
                {
                    return;
                }
                String dbname = getConfig().getString("SQLite.Filename", "eternia");
                File dataFolder = new File(getDataFolder(), dbname+".db");
                if (!dataFolder.exists())
                {
                    try
                    {
                        //noinspection ResultOfMethodCallIgnored
                        dataFolder.createNewFile();
                    }
                    catch (IOException e)
                    {
                        getLogger().severe( "Erro ao escrever arquivo: "+dbname+".db");
                    }
                }
                Class.forName("org.sqlite.JDBC");
                setConnection(DriverManager.getConnection("jdbc:sqlite:" + dataFolder));
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Conectado com sucesso a database");
                String SQLiteCreateTokensTable = "CREATE TABLE IF NOT EXISTS eternia (`UUID` varchar(255) NOT NULL, `NAME` varchar(32) NOT NULL, `XP` int(11) NOT NULL);";
                try
                {
                    Statement s = getConnection().createStatement();
                    s.executeUpdate(SQLiteCreateTokensTable);
                    s.close();
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
    public Connection getConnection()
    {
        return connection;
    }
    private void setConnection(Connection connection)
    {
        this.connection = connection;
    }
    public static Economy getEconomy()
    {
        return econ;
    }
}