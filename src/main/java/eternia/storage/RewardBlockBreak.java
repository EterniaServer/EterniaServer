package eternia.storage;

import eternia.EterniaServer;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.block.BlockBreakEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RewardBlockBreak {
    public RewardBlockBreak(Material material, BlockBreakEvent event) {
        if (EterniaServer.getBlocks().contains("Blocks." + material)) {
            ConfigurationSection cs = EterniaServer.getBlocks().getConfigurationSection("Blocks." + material);
            double randomNumber = new Random().nextDouble();
            assert cs != null;
            List<String> mainList = new ArrayList<>(cs.getKeys(true));
            double lowestNumberAboveRandom = 1.1;
            for (int i = 1; i < cs.getKeys(true).size(); i++) {
                double current = Double.parseDouble(mainList.get(i).replace("[", "").replace("]", ""));
                if (current < lowestNumberAboveRandom && current > randomNumber) {
                    lowestNumberAboveRandom = current;
                }
            }
            if (lowestNumberAboveRandom <= 1) {
                List<String> stringList = EterniaServer.getBlocks().getStringList("Blocks." + material + "." + "[" + lowestNumberAboveRandom + "]");
                for (String command : stringList) {
                    String modifiedCommand = command.replace("%PLAYER%", event.getPlayer().getPlayerListName());
                    EterniaServer.getMain().getServer().dispatchCommand(EterniaServer.getMain().getServer().getConsoleSender(), modifiedCommand);

                }
            }
        }
    }
}