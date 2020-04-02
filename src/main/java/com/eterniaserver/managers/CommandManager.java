package com.eterniaserver.managers;

import com.eterniaserver.EterniaServer;
import com.eterniaserver.commands.economy.*;
import com.eterniaserver.commands.experience.*;
import com.eterniaserver.commands.messages.*;
import com.eterniaserver.commands.others.*;
import com.eterniaserver.commands.simplifications.*;
import com.eterniaserver.commands.teleports.*;
import com.eterniaserver.configs.CVar;
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
            Objects.requireNonNull(plugin.getCommand("facebook")).setExecutor(new Facebook());
            Objects.requireNonNull(plugin.getCommand("colors")).setExecutor(new Colors());
            Objects.requireNonNull(plugin.getCommand("vote")).setExecutor(new Vote());
            Objects.requireNonNull(plugin.getCommand("pay")).setExecutor(new Pay());
            Objects.requireNonNull(plugin.getCommand("money")).setExecutor(new Money());
            Objects.requireNonNull(plugin.getCommand("baltop")).setExecutor(new Baltop());
            Objects.requireNonNull(plugin.getCommand("eco")).setExecutor(new Eco());
            Objects.requireNonNull(plugin.getCommand("spawnergive")).setExecutor(new SpawnerGive());
            Objects.requireNonNull(plugin.getCommand("gamemode")).setExecutor(new Gamemode());
            Objects.requireNonNull(plugin.getCommand("sun")).setExecutor(new Sun());
            Objects.requireNonNull(plugin.getCommand("rain")).setExecutor(new Rain());
            Objects.requireNonNull(plugin.getCommand("god")).setExecutor(new God());
            Objects.requireNonNull(plugin.getCommand("thor")).setExecutor(new Thor());
            Objects.requireNonNull(plugin.getCommand("itemrename")).setExecutor(new ItemRename());
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
            Objects.requireNonNull(plugin.getCommand("warp")).setExecutor(new Warp());
            Objects.requireNonNull(plugin.getCommand("setwarp")).setExecutor(new SetWarp());
            Objects.requireNonNull(plugin.getCommand("delwarp")).setExecutor(new DelWarp());
            Objects.requireNonNull(plugin.getCommand("listwarp")).setExecutor(new ListWarp());
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