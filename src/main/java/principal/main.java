package principal;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class main extends JavaPlugin {
    private Connection connection;
    public String host, database, username, password, table;
    public int port;
    @Override
    public void onEnable() {
        mysqlSetup();
        this.getServer().getPluginManager().registerEvents(new principal.mysql(), this);
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Até aqui ta okay!");
        this.getCommand("suicidio").setExecutor(new comandos.player.suicidio());
        this.getCommand("aviso").setExecutor(new comandos.staff.aviso());
        this.getCommand("discord").setExecutor(new comandos.player.discord());
        this.getCommand("doar").setExecutor(new comandos.player.doar());
        this.getCommand("regras").setExecutor(new comandos.player.regras());
        this.getCommand("feed").setExecutor(new comandos.staff.feed());
        this.getCommand("hat").setExecutor(new comandos.player.hat());
        this.getCommand("nome").setExecutor(new comandos.staff.nome());
        this.getCommand("fly").setExecutor(new comandos.staff.fly());
        this.getCommand("pa").setExecutor(new comandos.player.pa());
        this.getCommand("guardaxp").setExecutor(new comandos.player.guardaxp());
        this.getCommand("retirarxp").setExecutor(new comandos.player.retirarxp());
    }

    @Override
    public void onDisable() {
    }

    public void mysqlSetup(){
        host = "167.114.85.28";
        port = 3306;
        username = "hm_5763";
        database = "hm_5763";
        password = "8f949c48dc";
        table = "eternia";
        try {
            synchronized (this){
                if(getConnection() != null && !getConnection().isClosed()){
                    return;
                }
                Class.forName("java.sql.Driver");
                setConnection(DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/"
                        + this.database, this.username, this.password));
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Conectado com sucesso a database");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public Connection getConnection() {
        return connection;
    }
    public void setConnection(Connection connection){
        this.connection = connection;
    }
}