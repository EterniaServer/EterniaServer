package br.com.eterniaserver.modules.blockrewardmanager;

import br.com.eterniaserver.EterniaServer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockReward {
    public static void Break(BlockBreakEvent event, EterniaServer plugin) {
        if (event.isCancelled()) {
            return;
        }
        if (EterniaServer.blocks.contains("Blocks." + event.getBlock().getType())) {
            ConfigurationSection cs = EterniaServer.blocks.getConfigurationSection("Blocks." + event.getBlock().getType());
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
                List<String> stringList = EterniaServer.blocks.getStringList("Blocks." + event.getBlock().getType() + "." + lowestNumberAboveRandom);
                for (String command : stringList) {
                    String modifiedCommand = command.replace("%PLAYER%", event.getPlayer().getPlayerListName());
                    plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), modifiedCommand);
                }
            }
        }
    }
}
