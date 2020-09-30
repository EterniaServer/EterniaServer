package br.com.eterniaserver.eterniaserver;

import br.com.eterniaserver.eterniaserver.configurations.BlocksCfg;
import br.com.eterniaserver.eterniaserver.configurations.CashCfg;
import br.com.eterniaserver.eterniaserver.configurations.ChatCfg;
import br.com.eterniaserver.eterniaserver.configurations.CommandsCfg;
import br.com.eterniaserver.eterniaserver.configurations.ConfigsCfg;
import br.com.eterniaserver.eterniaserver.configurations.KitsCfg;
import br.com.eterniaserver.eterniaserver.configurations.MsgCfg;
import br.com.eterniaserver.eterniaserver.configurations.RewardsCfg;
import br.com.eterniaserver.eterniaserver.configurations.ScheduleCfg;
import br.com.eterniaserver.eterniaserver.configurations.TableCfg;
import br.com.eterniaserver.eterniaserver.configurations.dependencies.PlaceHolders;
import br.com.eterniaserver.eterniaserver.configurations.dependencies.VaultHook;
import br.com.eterniaserver.eterniaserver.events.BlockHandler;
import br.com.eterniaserver.eterniaserver.events.EntityHandler;
import br.com.eterniaserver.eterniaserver.events.PlayerHandler;
import br.com.eterniaserver.eterniaserver.events.ServerHandler;

import org.bukkit.plugin.java.JavaPlugin;

public class EterniaServer extends JavaPlugin {

    public static final ConfigsCfg configs = new ConfigsCfg();
    public static final MsgCfg msg = new MsgCfg();
    public static final BlocksCfg block = new BlocksCfg();
    public static final CashCfg cash = new CashCfg();
    public static final KitsCfg kits = new KitsCfg();
    public static final ChatCfg chat = new ChatCfg();
    public static final ScheduleCfg schedule =  new ScheduleCfg();
    public static final CommandsCfg commands = new CommandsCfg();
    public static final RewardsCfg rewards = new RewardsCfg();

    @Override
    public void onEnable() {

        new TableCfg();
        new PlaceHolders().register();
        new Managers(this);
        new VaultHook(this);

        this.getServer().getPluginManager().registerEvents(new BlockHandler(), this);
        this.getServer().getPluginManager().registerEvents(new EntityHandler(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerHandler(), this);
        this.getServer().getPluginManager().registerEvents(new ServerHandler(), this);

    }

}
