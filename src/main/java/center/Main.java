package center;

import events.*;
import experience.*;
import messages.*;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import others.*;
import teleports.*;
import org.bukkit.Bukkit;
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
    private FileConfiguration messagesConfig;
    @Override
    public void onEnable()
    {
        // Faz com que a classe principal não seja alterada.
        mainclasse = this;
        // Carrega todas as configurações do plugin e após isso
        // ativa a classe NetherTrapCheck com o intervalo definido
        // nas configurações.
        saveDefaultConfig();
        createMessageConfig();
        long delay = getConfig().getInt("intervalo") * 20;
        // Eventos
        new NetherPortal().runTaskTimer(this, 20L, delay);
        this.getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerTeleport(), this);
        this.getServer().getPluginManager().registerEvents(new ExpDrop(), this);
        // Salvamento
        boolean mysql = Vars.getBool("mysql");
        if (mysql)
        {
            MySQLSetup();
        }
        else
        {
            SQLiteSetup();
        }
        // Money.
        if (!VaultCheck())
        {
            Vars.consoleMessage("vault-off");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        // Comandos
        boolean xp_module = Vars.getBool("xp-module");
        boolean warp_module = Vars.getBool("warp-module");
        boolean tpa_module = Vars.getBool("tpa-module");
        if (xp_module)
        {
            Objects.requireNonNull(this.getCommand("depositlvl")).setExecutor(new DepositLevel());
            Objects.requireNonNull(this.getCommand("withdrawlvl")).setExecutor(new WithdrawLevel());
            Objects.requireNonNull(this.getCommand("bottlexp")).setExecutor(new Bottlexp());
            Objects.requireNonNull(this.getCommand("checklevel")).setExecutor(new CheckLevel());
        }
        if (warp_module)
        {
            Objects.requireNonNull(this.getCommand("spawn")).setExecutor(new Spawn());
            Objects.requireNonNull(this.getCommand("setspawn")).setExecutor(new SetSpawn());
            Objects.requireNonNull(this.getCommand("arena")).setExecutor(new Arena());
            Objects.requireNonNull(this.getCommand("setarena")).setExecutor(new SetArena());
            Objects.requireNonNull(this.getCommand("crates")).setExecutor(new Crates());
            Objects.requireNonNull(this.getCommand("setcrates")).setExecutor(new SetCrates());
            Objects.requireNonNull(this.getCommand("event")).setExecutor(new Event());
            Objects.requireNonNull(this.getCommand("setevent")).setExecutor(new SetEvent());
        }
        if (tpa_module)
        {
            Objects.requireNonNull(this.getCommand("teleportaccept")).setExecutor(new TeleportAccept());
            Objects.requireNonNull(this.getCommand("teleportdeny")).setExecutor(new TeleportDeny());
            Objects.requireNonNull(this.getCommand("teleporttoplayer")).setExecutor(new TeleportToPlayer());
            Objects.requireNonNull(this.getCommand("teleportall")).setExecutor(new TeleportAll());
        }
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
        Objects.requireNonNull(this.getCommand("blocks")).setExecutor(new Blocks());
        Objects.requireNonNull(this.getCommand("back")).setExecutor(new Back());
        Objects.requireNonNull(this.getCommand("spectator")).setExecutor(new Spectator());
        Objects.requireNonNull(this.getCommand("survival")).setExecutor(new Survival());
        Objects.requireNonNull(this.getCommand("facebook")).setExecutor(new Facebook());
        Objects.requireNonNull(this.getCommand("colors")).setExecutor(new Colors());
        Objects.requireNonNull(this.getCommand("vote")).setExecutor(new Vote());
    }
    public FileConfiguration getMessages()
    {
        return this.messagesConfig;
    }
    private void createMessageConfig()
    {
        File messagesConfigFile = new File(getDataFolder(), "messages.yml");
        if (!messagesConfigFile.exists())
        {
            //noinspection ResultOfMethodCallIgnored
            messagesConfigFile.getParentFile().mkdirs();
            saveResource("messages.yml", false);
        }
        messagesConfig = new YamlConfiguration();
        try
        {
            messagesConfig.load(messagesConfigFile);
        }
        catch (IOException | InvalidConfigurationException e)
        {
            e.printStackTrace();
        }
    }
    // Confirma que essa classe só será carregada uma vez e os valores
    // fiquem fixos.
    public static Main getMain() { return mainclasse; }
    // Verifica se existe o plugin Vault na pasta de plugins, caso não
    // encontre ele retorna falso e caso encontre retorna true.
    private boolean VaultCheck()
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
    // Caso o método de salvamento escolhido seja MySQL ele irá carregar
    // as configurações, tentar se conectar a database, verificar se a
    // tabela existe e caso não exista ele irá criar uma tabela.
    private void MySQLSetup()
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
                String host = getConfig().getString("host");
                int port = getConfig().getInt("porta");
                String password = getConfig().getString("senha");
                String username = getConfig().getString("usuario");
                String database = getConfig().getString("database");
                setConnection(DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password));
                Vars.consoleMessage("conectado-sucesso-mysql");
                String MySQLCreate = "CREATE TABLE IF NOT EXISTS eternia (`UUID` varchar(32) NOT NULL, `NAME` varchar(32) NOT NULL, `XP` int(11) NOT NULL);";
                try
                {
                    Statement mysql_conect = getConnection().createStatement();
                    mysql_conect.executeUpdate(MySQLCreate);
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
    // Caso o método de salvamento escolhido seja SQLite ele irá procurar
    // na pasta principal desse plugin se o arquivo da tabela existe e caso
    // não exista ele irá criar.
    private void SQLiteSetup()
    {
        try
        {
            synchronized (this)
            {
                if (getConnection() != null && !getConnection().isClosed())
                {
                    return;
                }
                File dataFolder = new File(getDataFolder(), "eternia.db");
                if (!dataFolder.exists())
                {
                    try
                    {
                        //noinspection ResultOfMethodCallIgnored
                        dataFolder.createNewFile();
                    }
                    catch (IOException e)
                    {
                        Vars.consoleReplaceMessage("erro-sqlite", "eternia");
                    }
                }
                Class.forName("org.sqlite.JDBC");
                setConnection(DriverManager.getConnection("jdbc:sqlite:" + dataFolder));
                Vars.consoleMessage("conectado-sucesso-sql");
                String SQLiteCreateTokensTable = "CREATE TABLE IF NOT EXISTS eternia (`UUID` varchar(255) NOT NULL, `NAME` varchar(32) NOT NULL, `XP` int(11) NOT NULL);";
                try
                {
                    Statement sqlite_conect = getConnection().createStatement();
                    sqlite_conect.executeUpdate(SQLiteCreateTokensTable);
                    sqlite_conect.close();
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
    // Sempre que for necessário se conectar a database para salvar ou
    // pegar dados ele irá pegar a conexão já existe sem precisar criar
    // uma nova.
    public Connection getConnection() { return connection; }
    private void setConnection(Connection connection) { this.connection = connection; }
    // Sempre que está função ser chamada ela vai retornar o plugin Vault.
    public static Economy getEconomy() { return econ; }
}