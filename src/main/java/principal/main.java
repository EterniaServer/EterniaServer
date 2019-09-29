package principal;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class main extends JavaPlugin {
    private Connection connection;
    @Override
    public void onEnable() {
        mysqlSetup();
        this.getServer().getPluginManager().registerEvents(new principal.mysql(), this);
        Objects.requireNonNull(this.getCommand("suicide")).setExecutor(new comandos.player.suicide());
        Objects.requireNonNull(this.getCommand("advice")).setExecutor(new comandos.staff.advice());
        Objects.requireNonNull(this.getCommand("discord")).setExecutor(new comandos.player.discord());
        Objects.requireNonNull(this.getCommand("doar")).setExecutor(new comandos.player.doar());
        Objects.requireNonNull(this.getCommand("regras")).setExecutor(new comandos.player.regras());
        Objects.requireNonNull(this.getCommand("feed")).setExecutor(new comandos.staff.feed());
        Objects.requireNonNull(this.getCommand("hat")).setExecutor(new comandos.player.hat());
        Objects.requireNonNull(this.getCommand("nome")).setExecutor(new comandos.staff.nome());
        Objects.requireNonNull(this.getCommand("fly")).setExecutor(new comandos.staff.fly());
        Objects.requireNonNull(this.getCommand("pa")).setExecutor(new comandos.player.pa());
        Objects.requireNonNull(this.getCommand("guardaxp")).setExecutor(new comandos.player.guardaxp());
        Objects.requireNonNull(this.getCommand("retirarxp")).setExecutor(new comandos.player.retirarxp());
    }

    @Override
    public void onDisable() {
    }

    public final String table = "eternia";
    private void mysqlSetup(){
        try {
            synchronized (this){
                if(getConnection() != null && !getConnection().isClosed()){
                    return;
                }
                Class.forName("java.sql.Driver");
                String host = "167.114.85.28";
                int port = 3306;
                String password = "8f949c48dc";
                String database = "hm_5763";
                String username = "hm_5763";
                setConnection(DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/"
                        + database, username, password));
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Conectado com sucesso a database");
            }
        } catch(SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public Connection getConnection() {
        return connection;
    }
    private void setConnection(Connection connection){
        this.connection = connection;
    }
}