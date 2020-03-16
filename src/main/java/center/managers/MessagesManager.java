package center.managers;

import center.Main;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;

public class MessagesManager {
    public MessagesManager(Main plugin) {
        File messagesConfigFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!messagesConfigFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        Main.messagesConfig = new YamlConfiguration();
        try {
            Main.messagesConfig.load(messagesConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}