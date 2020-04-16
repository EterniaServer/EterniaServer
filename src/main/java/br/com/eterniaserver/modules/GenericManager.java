package br.com.eterniaserver.modules;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.modules.genericmanager.commands.messages.*;
import br.com.eterniaserver.modules.genericmanager.commands.others.*;
import br.com.eterniaserver.modules.genericmanager.commands.replaces.*;
import br.com.eterniaserver.modules.genericmanager.commands.simplifications.*;

import java.util.Objects;

public class GenericManager {

    public GenericManager(EterniaServer plugin) {
        if (EterniaServer.configs.getBoolean("modules.generic")) {
            Objects.requireNonNull(plugin.getCommand("suicide")).setExecutor(new Suicide());
            Objects.requireNonNull(plugin.getCommand("discord")).setExecutor(new Discord());
            Objects.requireNonNull(plugin.getCommand("donation")).setExecutor(new Donation());
            Objects.requireNonNull(plugin.getCommand("rules")).setExecutor(new Rules());
            Objects.requireNonNull(plugin.getCommand("feed")).setExecutor(new Feed());
            Objects.requireNonNull(plugin.getCommand("hat")).setExecutor(new Hat());
            Objects.requireNonNull(plugin.getCommand("tell")).setExecutor(new Tell());
            Objects.requireNonNull(plugin.getCommand("resp")).setExecutor(new Resp());
            Objects.requireNonNull(plugin.getCommand("advice")).setExecutor(new Advice());
            Objects.requireNonNull(plugin.getCommand("fly")).setExecutor(new Fly());
            Objects.requireNonNull(plugin.getCommand("goldenshovel")).setExecutor(new GoldenShovel());
            Objects.requireNonNull(plugin.getCommand("blocks")).setExecutor(new Blocks());
            Objects.requireNonNull(plugin.getCommand("facebook")).setExecutor(new Facebook());
            Objects.requireNonNull(plugin.getCommand("colors")).setExecutor(new Colors());
            Objects.requireNonNull(plugin.getCommand("vote")).setExecutor(new Vote());
            Objects.requireNonNull(plugin.getCommand("mem")).setExecutor(new Mem());
            Objects.requireNonNull(plugin.getCommand("gamemode")).setExecutor(new Gamemode());
            Objects.requireNonNull(plugin.getCommand("sun")).setExecutor(new Sun());
            Objects.requireNonNull(plugin.getCommand("rain")).setExecutor(new Rain());
            Objects.requireNonNull(plugin.getCommand("god")).setExecutor(new God());
            Objects.requireNonNull(plugin.getCommand("thor")).setExecutor(new Thor());
            Objects.requireNonNull(plugin.getCommand("itemrename")).setExecutor(new ItemRename());
            Objects.requireNonNull(plugin.getCommand("reloadeternia")).setExecutor(new ReloadEternia(plugin));
            Objects.requireNonNull(plugin.getCommand("enderchest")).setExecutor(new EnderChest());
            Objects.requireNonNull(plugin.getCommand("openinventory")).setExecutor(new OpenInventory());
            Objects.requireNonNull(plugin.getCommand("workbench")).setExecutor(new Workbench());
            Messages.ConsoleMessage("modules.enable", "Generic");
        } else {
            Messages.ConsoleMessage("modules.disable", "Generic");
        }
    }

}
