package com.eterniaserver.modules;

import com.eterniaserver.EterniaServer;
import com.eterniaserver.configs.CVar;
import com.eterniaserver.configs.methods.ConsoleMessage;
import com.eterniaserver.modules.genericmanager.commands.messages.*;
import com.eterniaserver.modules.genericmanager.commands.others.*;
import com.eterniaserver.modules.genericmanager.commands.simplifications.*;

import java.util.Objects;

public class GenericManager {
    public GenericManager(EterniaServer plugin) {
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
            Objects.requireNonNull(plugin.getCommand("facebook")).setExecutor(new Facebook());
            Objects.requireNonNull(plugin.getCommand("colors")).setExecutor(new Colors());
            Objects.requireNonNull(plugin.getCommand("vote")).setExecutor(new Vote());
            Objects.requireNonNull(plugin.getCommand("gamemode")).setExecutor(new Gamemode());
            Objects.requireNonNull(plugin.getCommand("sun")).setExecutor(new Sun());
            Objects.requireNonNull(plugin.getCommand("rain")).setExecutor(new Rain());
            Objects.requireNonNull(plugin.getCommand("god")).setExecutor(new God());
            Objects.requireNonNull(plugin.getCommand("thor")).setExecutor(new Thor());
            Objects.requireNonNull(plugin.getCommand("itemrename")).setExecutor(new ItemRename());
            Objects.requireNonNull(plugin.getCommand("reloadeternia")).setExecutor(new ReloadEternia());
            new ConsoleMessage("modules.enable", "Generic");
        } else {
            new ConsoleMessage("modules.disable", "Generic");
        }
    }
}
