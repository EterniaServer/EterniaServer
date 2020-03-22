package eternia.managers;

import eternia.EterniaServer;
import eternia.configs.CVar;
import eternia.commands.experience.*;
import eternia.commands.economy.*;
import eternia.commands.messages.*;
import eternia.commands.others.*;
import eternia.commands.teleports.*;
import java.util.Objects;

public class CommandManager {
    public CommandManager(EterniaServer plugin) {
        if (CVar.getBool("modules.generic")) {
            Objects.requireNonNull(plugin.getCommand("suicide")).setExecutor(new Suicide());
            Objects.requireNonNull(plugin.getCommand("advice")).setExecutor(new Advice());
            Objects.requireNonNull(plugin.getCommand("discord")).setExecutor(new Discord());
            Objects.requireNonNull(plugin.getCommand("donation")).setExecutor(new Donation());
            Objects.requireNonNull(plugin.getCommand("rules")).setExecutor(new Rules());
            Objects.requireNonNull(plugin.getCommand("feed")).setExecutor(new Feed());
            Objects.requireNonNull(plugin.getCommand("hat")).setExecutor(new Hat());
            Objects.requireNonNull(plugin.getCommand("nome")).setExecutor(new Name());
            Objects.requireNonNull(plugin.getCommand("fly")).setExecutor(new Fly());
            Objects.requireNonNull(plugin.getCommand("goldenshovel")).setExecutor(new GoldenShovel());
            Objects.requireNonNull(plugin.getCommand("blocks")).setExecutor(new Blocks());
            Objects.requireNonNull(plugin.getCommand("back")).setExecutor(new Back());
            Objects.requireNonNull(plugin.getCommand("spectator")).setExecutor(new Spectator());
            Objects.requireNonNull(plugin.getCommand("survival")).setExecutor(new Survival());
            Objects.requireNonNull(plugin.getCommand("facebook")).setExecutor(new Facebook());
            Objects.requireNonNull(plugin.getCommand("colors")).setExecutor(new Colors());
            Objects.requireNonNull(plugin.getCommand("vote")).setExecutor(new Vote());
            Objects.requireNonNull(plugin.getCommand("pay")).setExecutor(new Pay());
            Objects.requireNonNull(plugin.getCommand("money")).setExecutor(new Money());
            Objects.requireNonNull(plugin.getCommand("baltop")).setExecutor(new Baltop());
        }
        if (CVar.getBool("modules.tpa")) {
            Objects.requireNonNull(plugin.getCommand("teleportaccept")).setExecutor(new TeleportAccept());
            Objects.requireNonNull(plugin.getCommand("teleportdeny")).setExecutor(new TeleportDeny());
            Objects.requireNonNull(plugin.getCommand("teleporttoplayer")).setExecutor(new TeleportToPlayer());
            Objects.requireNonNull(plugin.getCommand("teleportall")).setExecutor(new TeleportAll());
        }
        if (CVar.getBool("modules.warp")) {
            Objects.requireNonNull(plugin.getCommand("spawn")).setExecutor(new Spawn());
            Objects.requireNonNull(plugin.getCommand("setspawn")).setExecutor(new SetSpawn());
            Objects.requireNonNull(plugin.getCommand("arena")).setExecutor(new Arena());
            Objects.requireNonNull(plugin.getCommand("setarena")).setExecutor(new SetArena());
            Objects.requireNonNull(plugin.getCommand("crates")).setExecutor(new Crates());
            Objects.requireNonNull(plugin.getCommand("setcrates")).setExecutor(new SetCrates());
            Objects.requireNonNull(plugin.getCommand("event")).setExecutor(new Event());
            Objects.requireNonNull(plugin.getCommand("setevent")).setExecutor(new SetEvent());
            Objects.requireNonNull(plugin.getCommand("shop")).setExecutor(new Shop());
            Objects.requireNonNull(plugin.getCommand("setshop")).setExecutor(new SetShop());
        }
        if (CVar.getBool("modules.xp")) {
            Objects.requireNonNull(plugin.getCommand("depositlvl")).setExecutor(new DepositLevel());
            Objects.requireNonNull(plugin.getCommand("withdrawlvl")).setExecutor(new WithdrawLevel());
            Objects.requireNonNull(plugin.getCommand("bottlexp")).setExecutor(new Bottlexp());
            Objects.requireNonNull(plugin.getCommand("checklevel")).setExecutor(new CheckLevel());
        }
    }
}