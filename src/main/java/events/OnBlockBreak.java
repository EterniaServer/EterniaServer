package events;

import center.Main;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OnBlockBreak implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Material material = event.getBlock().getType();
        if (Main.getMain().getConfig().contains("Blocks." + material)) {

            ConfigurationSection cs = Main.getMain().getConfig().getConfigurationSection("Blocks." + material);

            Random random = new Random();
            double randomNumber = random.nextDouble();

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
                List<String> stringList = Main.getMain().getConfig().getStringList("Blocks." + material + "." + "[" + lowestNumberAboveRandom + "]");
                for (String command : stringList) {
                    String modifiedCommand = command.replace("%PLAYER%", event.getPlayer().getPlayerListName());
                    Main.getMain().getServer().dispatchCommand(Main.getMain().getServer().getConsoleSender(), modifiedCommand);

                }
            }
        }
    }
}