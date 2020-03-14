package center;

import economy.*;
import vault.*;
import events.*;
import experience.*;
import messages.*;
import others.*;
import teleports.*;
import storage.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.sql.Connection;
import java.util.Objects;

public class Main extends JavaPlugin
{
    private static Main mainclasse;
    public EconomyImplementer economyImplementer;
    private Connection connection;
    public FileConfiguration messagesConfig;
    @Override
    public void onEnable()
    {
        mainclasse = this;
        // Configs
        saveDefaultConfig();
        // Armazenamento
        new MessagesConfig(this);
        new StorageManager(this, Vars.getBool("mysql.mysql"));
        new VaultHook(this).SetEconomyLink();
        // Ativando eventos
        new NetherPortal().runTaskTimer(this, 20L, getConfig().getInt("intervalo") * 20);
        this.getServer().getPluginManager().registerEvents(new OnChat(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerTeleport(), this);
        this.getServer().getPluginManager().registerEvents(new ExpDrop(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerQuit(), this);
        // Comandos
        if (Vars.getBool("xp-module"))
        {
            Objects.requireNonNull(this.getCommand("depositlvl")).setExecutor(new DepositLevel());
            Objects.requireNonNull(this.getCommand("withdrawlvl")).setExecutor(new WithdrawLevel());
            Objects.requireNonNull(this.getCommand("bottlexp")).setExecutor(new Bottlexp());
            Objects.requireNonNull(this.getCommand("checklevel")).setExecutor(new CheckLevel());
        }
        if (Vars.getBool("warp-module"))
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
        if (Vars.getBool("tpa-module"))
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
        Objects.requireNonNull(this.getCommand("balance")).setExecutor(new Balance());
        Objects.requireNonNull(this.getCommand("pay")).setExecutor(new Pay());
        Objects.requireNonNull(this.getCommand("eco")).setExecutor(new Eco());
    }
    public FileConfiguration getMessages()
    {
        return this.messagesConfig;
    }
    public Connection getConnection()
    {
        return connection;
    }
    public static Main getMain()
    {
        return mainclasse;
    }
    public void setConnection(Connection connection)
    {
        this.connection = connection;
    }
}