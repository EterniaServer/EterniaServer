package br.com.eterniaserver.eterniaserver.modules.elevator;

import br.com.eterniaserver.eternialib.configuration.CommandLocale;
import br.com.eterniaserver.eternialib.configuration.ReloadableConfiguration;
import br.com.eterniaserver.eternialib.configuration.enums.ConfigurationCategory;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;
import java.util.logging.Level;

final class Configurations {

    private Configurations() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    static class ElevatorConfiguration implements ReloadableConfiguration {

        private final EterniaServer plugin;

        private final FileConfiguration inFile;
        private final FileConfiguration outFile;

        public ElevatorConfiguration(EterniaServer plugin) {
            this.inFile = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "elevator.yml"));
            this.outFile = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "elevator.yml"));
            this.plugin = plugin;
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
            return Constants.ELEVATOR_MODULE_FOLDER_PATH;
        }

        @Override
        public String getFilePath() {
            return Constants.ELEVATOR_CONFIG_FILE_PATH;
        }

        @Override
        public String[] messages() {
            return plugin.messages();
        }

        @Override
        public CommandLocale[] commandsLocale() {
            return new CommandLocale[0];
        }

        @Override
        public ConfigurationCategory category() {
            return ConfigurationCategory.GENERIC;
        }

        @Override
        public void executeConfig() {
            int[] integers = plugin.integers();
            String[] strings = plugin.strings();

            List<Material> elevatorMaterials = plugin.getElevatorMaterials();

            strings[Strings.PERM_ELEVATOR.ordinal()] = inFile.getString("permission", "eternia.elevator");
            integers[Integers.ELEVATOR_MAX.ordinal()] = inFile.getInt("space-between.max", 50);
            integers[Integers.ELEVATOR_MIN.ordinal()] = inFile.getInt("space-between.min", 3);

            List<String> list = inFile.getStringList("valid-materials");
            list = list.isEmpty() ? List.of("IRON_BLOCK") : list;

            elevatorMaterials.clear();
            elevatorMaterials.addAll(list.stream().map(Material::valueOf).toList());

            outFile.set("permission", strings[Strings.PERM_ELEVATOR.ordinal()]);
            outFile.set("space-between.max", integers[Integers.ELEVATOR_MAX.ordinal()]);
            outFile.set("space-between.min", integers[Integers.ELEVATOR_MIN.ordinal()]);
            outFile.set("valid-materials", elevatorMaterials.stream().map(Material::name).toList());
        }

        @Override
        public void executeCritical() {
            plugin.getLogger().log(Level.INFO, "elevator module: no critical configuration");
        }
    }

}
