package storage;

import center.Main;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;

public class MessagesConfig
{
    public MessagesConfig(Main plugin)
    {
        File messagesConfigFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!messagesConfigFile.exists())
        {
            plugin.saveResource("messages.yml", false);
        }
        plugin.messagesConfig = new YamlConfiguration();
        try
        {
            plugin.messagesConfig.load(messagesConfigFile);
        }
        catch (IOException | InvalidConfigurationException e)
        {
            e.printStackTrace();
        }
    }
}
