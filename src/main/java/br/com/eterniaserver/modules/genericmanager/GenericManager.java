package br.com.eterniaserver.modules.genericmanager;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Strings;
import br.com.eterniaserver.configs.Vars;
import br.com.eterniaserver.modules.genericmanager.commands.messages.*;
import br.com.eterniaserver.modules.genericmanager.commands.others.*;
import br.com.eterniaserver.modules.genericmanager.commands.replaces.*;
import br.com.eterniaserver.modules.genericmanager.commands.simplifications.*;
import br.com.eterniaserver.player.PlayerFlyState;

import java.util.Objects;

public class GenericManager {

    public GenericManager(EterniaServer plugin, Messages messages, Strings strings, PlayerFlyState playerFlyState, Reload reload, Vars vars) {
        if (plugin.serverConfig.getBoolean("modules.generic")) {
            Objects.requireNonNull(plugin.getCommand("suicide")).setExecutor(new Suicide(messages));
            Objects.requireNonNull(plugin.getCommand("feed")).setExecutor(new Feed(messages));
            Objects.requireNonNull(plugin.getCommand("hat")).setExecutor(new Hat(messages));
            Objects.requireNonNull(plugin.getCommand("fly")).setExecutor(new Fly(messages, playerFlyState));
            Objects.requireNonNull(plugin.getCommand("blocks")).setExecutor(new Blocks(messages));
            Objects.requireNonNull(plugin.getCommand("mem")).setExecutor(new Mem(messages));
            Objects.requireNonNull(plugin.getCommand("memall")).setExecutor(new MemAll(messages));
            Objects.requireNonNull(plugin.getCommand("gamemode")).setExecutor(new Gamemode(messages));
            Objects.requireNonNull(plugin.getCommand("profile")).setExecutor(new Profile(plugin, messages, strings, vars));
            Objects.requireNonNull(plugin.getCommand("speed")).setExecutor(new Speed(messages));
            Objects.requireNonNull(plugin.getCommand("sun")).setExecutor(new Sun(messages));
            Objects.requireNonNull(plugin.getCommand("rain")).setExecutor(new Rain(messages));
            Objects.requireNonNull(plugin.getCommand("god")).setExecutor(new God(messages, vars));
            Objects.requireNonNull(plugin.getCommand("thor")).setExecutor(new Thor(messages));
            Objects.requireNonNull(plugin.getCommand("afk")).setExecutor(new AFK(messages, vars));
            Objects.requireNonNull(plugin.getCommand("itemrename")).setExecutor(new ItemRename(messages, strings));
            Objects.requireNonNull(plugin.getCommand("reloadeternia")).setExecutor(new ReloadEternia(messages, reload));
            Objects.requireNonNull(plugin.getCommand("enderchest")).setExecutor(new EnderChest(messages));
            Objects.requireNonNull(plugin.getCommand("openinventory")).setExecutor(new OpenInventory(messages));
            Objects.requireNonNull(plugin.getCommand("workbench")).setExecutor(new Workbench(messages));
            messages.ConsoleMessage("modules.enable", "%module%", "Generic");
        } else {
            messages.ConsoleMessage("modules.disable", "%module%", "Generic");
        }
    }

}
