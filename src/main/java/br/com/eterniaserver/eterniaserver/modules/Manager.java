package br.com.eterniaserver.eterniaserver.modules;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.modules.bed.BedManager;
import br.com.eterniaserver.eterniaserver.modules.cash.CashManager;
import br.com.eterniaserver.eterniaserver.modules.chat.ChatManager;
import br.com.eterniaserver.eterniaserver.modules.core.CoreManager;
import br.com.eterniaserver.eterniaserver.modules.economy.EconomyManager;
import br.com.eterniaserver.eterniaserver.modules.elevator.ElevatorManager;
import br.com.eterniaserver.eterniaserver.modules.entity.EntityManager;
import br.com.eterniaserver.eterniaserver.modules.experience.ExperienceManager;
import br.com.eterniaserver.eterniaserver.modules.glow.GlowManager;
import br.com.eterniaserver.eterniaserver.modules.item.ItemManager;
import br.com.eterniaserver.eterniaserver.modules.kit.KitManager;
import br.com.eterniaserver.eterniaserver.modules.placeholderapi.PlaceHolderAPIManager;
import br.com.eterniaserver.eterniaserver.modules.reward.RewardManager;
import br.com.eterniaserver.eterniaserver.modules.spawner.SpawnerManager;
import br.com.eterniaserver.eterniaserver.modules.teleport.TeleportManager;

import java.util.ArrayList;
import java.util.List;

public class Manager {

    private final List<Module> modules = new ArrayList<>();

    public Manager(EterniaServer plugin) {
        loadModule(new CoreManager(plugin));

        if (plugin.getBoolean(Booleans.MODULE_SPAWNERS)) modules.add(new SpawnerManager(plugin));
        if (plugin.getBoolean(Booleans.MODULE_EXPERIENCE)) modules.add(new ExperienceManager(plugin));
        if (plugin.getBoolean(Booleans.MODULE_ELEVATOR)) modules.add(new ElevatorManager(plugin));
        if (plugin.getBoolean(Booleans.MODULE_REWARDS)) modules.add(new RewardManager(plugin));
        if (plugin.getBoolean(Booleans.MODULE_GLOW)) modules.add(new GlowManager(plugin));
        if (plugin.getBoolean(Booleans.MODULE_PAPI)) modules.add(new PlaceHolderAPIManager(plugin));
        if (plugin.getBoolean(Booleans.MODULE_CASH)) modules.add(new CashManager(plugin));
        if (plugin.getBoolean(Booleans.MODULE_ENTITY)) modules.add(new EntityManager(plugin));
        if (plugin.getBoolean(Booleans.MODULE_ECONOMY)) modules.add(new EconomyManager(plugin));
        if (plugin.getBoolean(Booleans.MODULE_BED)) modules.add(new BedManager(plugin));
        if (plugin.getBoolean(Booleans.MODULE_ITEM)) modules.add(new ItemManager(plugin));
        if (plugin.getBoolean(Booleans.MODULE_KIT)) modules.add(new KitManager(plugin));
        if (plugin.getBoolean(Booleans.MODULE_TELEPORT)) modules.add(new TeleportManager(plugin));
        if (plugin.getBoolean(Booleans.MODULE_CHAT)) modules.add(new ChatManager(plugin));

        loadModules();
    }

    private void loadModules() {
        modules.forEach(this::loadModule);
    }

    private void loadModule(Module module) {
        module.loadConfigurations();
        module.loadCommandsCompletions();
        module.loadConditions();
        module.loadListeners();
        module.loadSchedules();
        module.loadCommands();
    }

}
