package br.com.eterniaserver.eterniaserver.modules.genericmanager;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.configs.Messages;
import br.com.eterniaserver.eterniaserver.configs.Strings;
import br.com.eterniaserver.eterniaserver.configs.Vars;
import br.com.eterniaserver.eterniaserver.modules.genericmanager.commands.messages.*;
import br.com.eterniaserver.eterniaserver.modules.genericmanager.commands.others.*;
import br.com.eterniaserver.eterniaserver.modules.genericmanager.commands.replaces.*;
import br.com.eterniaserver.eterniaserver.modules.genericmanager.commands.simplifications.*;
import br.com.eterniaserver.eterniaserver.player.PlayerFlyState;

public class GenericManager {

    public GenericManager(EterniaServer plugin, Messages messages, Strings strings, PlayerFlyState playerFlyState, Reload reload, Vars vars) {
        if (plugin.serverConfig.getBoolean("modules.generic")) {
            plugin.getCommand("suicide").setExecutor(new Suicide(messages));
            plugin.getCommand("feed").setExecutor(new Feed(messages));
            plugin.getCommand("hat").setExecutor(new Hat(messages));
            plugin.getCommand("fly").setExecutor(new Fly(messages, playerFlyState));
            plugin.getCommand("blocks").setExecutor(new Blocks(messages));
            plugin.getCommand("mem").setExecutor(new Mem(messages));
            plugin.getCommand("memall").setExecutor(new MemAll(messages));
            plugin.getCommand("gamemode").setExecutor(new Gamemode(messages));
            plugin.getCommand("profile").setExecutor(new Profile(plugin, messages, strings, vars));
            plugin.getCommand("speed").setExecutor(new Speed(messages));
            plugin.getCommand("sun").setExecutor(new Sun(messages));
            plugin.getCommand("rain").setExecutor(new Rain(messages));
            plugin.getCommand("god").setExecutor(new God(messages, vars));
            plugin.getCommand("thor").setExecutor(new Thor(messages));
            plugin.getCommand("afk").setExecutor(new AFK(messages, vars));
            plugin.getCommand("itemrename").setExecutor(new ItemRename(messages, strings));
            plugin.getCommand("reloadeternia").setExecutor(new ReloadEternia(messages, reload));
            plugin.getCommand("enderchest").setExecutor(new EnderChest(messages));
            plugin.getCommand("openinventory").setExecutor(new OpenInventory(messages));
            plugin.getCommand("workbench").setExecutor(new Workbench(messages));
            messages.sendConsole("modules.enable", "%module%", "Generic");
        } else {
            messages.sendConsole("modules.disable", "%module%", "Generic");
        }
    }

}
