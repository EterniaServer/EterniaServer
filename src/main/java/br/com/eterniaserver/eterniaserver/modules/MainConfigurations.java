package br.com.eterniaserver.eterniaserver.modules;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.FileCfg;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MainConfigurations implements FileCfg {

    private final FileConfiguration inFile;
    private final FileConfiguration outFile;

    public MainConfigurations(final EterniaServer plugin) {
        this.inFile = YamlConfiguration.loadConfiguration(new File(getFilePath()));
        this.outFile = new YamlConfiguration();

        final String[] strings = plugin.strings();

        // Strings
        strings[Strings.TABLE_PLAYER_PROFILES.ordinal()] = inFile.getString("sql.table-player-profiles", "es_players");
        strings[Strings.TABLE_SERVER_LOCATIONS.ordinal()] = inFile.getString("sql.table-server-locations", "es_locations_new");

        // Strings
        outFile.set("sql.table-player-profiles", strings[Strings.TABLE_PLAYER_PROFILES.ordinal()]);
        outFile.set("sql.table-server-locations", strings[Strings.TABLE_SERVER_LOCATIONS.ordinal()]);

        saveConfiguration(true);
    }

    @Override
    public FileConfiguration inFileConfiguration() {
        return inFile;
    }

    @Override
    public FileConfiguration outFileConfiguration() {
        return outFile;
    }

    @Override
    public String getFolderPath() {
        return Constants.DATA_LAYER_FOLDER_PATH;
    }

    @Override
    public String getFilePath() {
        return Constants.MAIN_CONFIG_FILE_PATH;
    }

    @Override
    public String[] messages() {
        return null;
    }
}
