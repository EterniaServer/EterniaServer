package br.com.eterniaserver.eterniaserver.modules.elevator;

import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.FileCfg;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.modules.Constants;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

final class Configurations {

    static class Configs implements FileCfg {

        private final FileConfiguration inFile;
        private final FileConfiguration outFile;

        protected Configs(final EterniaServer plugin) {
            this.inFile = YamlConfiguration.loadConfiguration(new File(getFilePath()));
            this.outFile = new YamlConfiguration();


            final int[] integers = plugin.integers();
            final String[] strings = plugin.strings();

            final List<Material> elevatorMaterials = plugin.elevatorMaterials;

            // Strings
            strings[Strings.PERM_ELEVATOR.ordinal()] = inFile.getString("permission", "eternia.elevator");
            // Integers
            integers[Integers.ELEVATOR_MAX.ordinal()] = inFile.getInt("space-between.max", 50);
            integers[Integers.ELEVATOR_MIN.ordinal()] = inFile.getInt("space-between.min", 3);
            // Lists
            List<String> list = inFile.getStringList("valid-materials");
            list = list.isEmpty() ? List.of("IRON_BLOCK") : list;
            elevatorMaterials.clear();
            elevatorMaterials.addAll(list.stream().map(Material::valueOf).collect(Collectors.toList()));

            // Strings
            outFile.set("permission", strings[Strings.PERM_ELEVATOR.ordinal()]);
            // Integers
            outFile.set("space-between.max", integers[Integers.ELEVATOR_MAX.ordinal()]);
            outFile.set("space-between.min", integers[Integers.ELEVATOR_MIN.ordinal()]);
            // Lists
            outFile.set("valid-materials", elevatorMaterials.stream().map(Material::name).collect(Collectors.toList()));

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
            return Constants.ELEVATOR_MODULE_FOLDER_PATH;
        }

        @Override
        public String getFilePath() {
            return Constants.ELEVATOR_CONFIG_FILE_PATH;
        }

        @Override
        public String[] messages() {
            return null;
        }

    }
}
