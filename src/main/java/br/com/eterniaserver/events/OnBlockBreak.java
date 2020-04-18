package br.com.eterniaserver.events;

import br.com.eterniaserver.EterniaServer;
import br.com.eterniaserver.configs.Messages;
import br.com.eterniaserver.configs.Strings;
import br.com.eterniaserver.modules.spawnermanager.actions.SpawnerBreak;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OnBlockBreak implements Listener {

    private final EterniaServer plugin;

    public OnBlockBreak(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent breakEvent) {
        if (EterniaServer.configs.getBoolean("modules.spawners")) {
            SpawnerBreak.Break(breakEvent);
        }
        if (EterniaServer.configs.getBoolean("modules.block-reward")) {
            if (breakEvent.isCancelled()) {
                return;
            }
            if (EterniaServer.blocks.contains("Blocks." + breakEvent.getBlock().getType())) {
                ConfigurationSection cs = EterniaServer.blocks.getConfigurationSection("Blocks." + breakEvent.getBlock().getType());
                double randomNumber = new Random().nextDouble();
                assert cs != null;
                List<String> mainList = new ArrayList<>(cs.getKeys(true));
                double lowestNumberAboveRandom = 1.1;
                for (int i = 1; i < cs.getKeys(true).size(); i++) {
                    double current = Double.parseDouble(mainList.get(i));
                    if (current < lowestNumberAboveRandom && current > randomNumber) {
                        lowestNumberAboveRandom = current;
                    }
                }
                if (lowestNumberAboveRandom <= 1) {
                    List<String> stringList = EterniaServer.blocks.getStringList("Blocks." + breakEvent.getBlock().getType() + "." + lowestNumberAboveRandom);
                    for (String command : stringList) {
                        if (Strings.papi) {
                            String modifiedCommand = Messages.putPAPI(breakEvent.getPlayer(), command);
                            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), modifiedCommand);
                        } else {
                            String modifiedCommand = command.replace("%player_name%", breakEvent.getPlayer().getPlayerListName());
                            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), modifiedCommand);
                        }
                    }
                }
            }
        }
    }

}