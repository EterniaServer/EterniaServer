package br.com.eterniaserver.eterniaserver.modules.entity;

import br.com.eterniaserver.eternialib.configuration.CommandLocale;
import br.com.eterniaserver.eternialib.configuration.ReloadableConfiguration;
import br.com.eterniaserver.eternialib.configuration.enums.ConfigurationCategory;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.util.logging.Level;

final class Configurations {

    static class EntityConfiguration implements ReloadableConfiguration {

        private final EterniaServer plugin;

        private final FileConfiguration inFile;
        private final FileConfiguration outFile;

        public EntityConfiguration(EterniaServer plugin) {
            this.inFile = YamlConfiguration.loadConfiguration(new File(getFilePath()));
            this.outFile = new YamlConfiguration();
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
            return Constants.ENTITY_MODULE_FOLDER_PATH;
        }

        @Override
        public String getFilePath() {
            return Constants.ENTITY_CONFIG_FILE_PATH;
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
            boolean[] booleans = plugin.booleans();
            Utils.EntityControl[] entities = plugin.entities();

            integers[Integers.CLEAR_RANGE.ordinal()] = inFile.getInt("clear.range", 1);
            integers[Integers.CLEAR_TIMER.ordinal()] = inFile.getInt("clear.timer", 30);

            booleans[Booleans.CLEAR_ENTITIES.ordinal()] = inFile.getBoolean("clear.kill", false);
            booleans[Booleans.ENTITY_LIMITER.ordinal()] = inFile.getBoolean("limiter.entity", true);
            booleans[Booleans.BREEDING_LIMITER.ordinal()] = inFile.getBoolean("limiter.breeding", true);
            booleans[Booleans.ENTITY_EDITOR.ordinal()] = inFile.getBoolean("limiter.editor", false);

            for (EntityType entity : EntityType.values()) {
                String entry = "types." + entity.name() + ".";
                int clearAmount = inFile.getInt(entry + "clear", -1);
                int spawnLimit = inFile.getInt(entry + "spawn-limit", -1);
                int breedingLimit = inFile.getInt(entry + "breeding-limit", -1);
                boolean editorState = inFile.getBoolean(entry + "editor.state", false);
                double health = inFile.getDouble(entry + "editor.health", 20.0);
                double attackDamage = inFile.getDouble(entry + "editor.attack-damage", 20.0);
                double speed = inFile.getDouble(entry + "editor.speed", 20.0);

                outFile.set(entry + "clear", clearAmount);
                outFile.set(entry + "spawn-limit", spawnLimit);
                outFile.set(entry + "breeding-limit", breedingLimit);
                outFile.set(entry + "editor.state", editorState);
                outFile.set(entry + "editor.health", health);
                outFile.set(entry + "editor.attack-damage", attackDamage);
                outFile.set(entry + "editor.speed", speed);

                Utils.EntityControl entityControl = new Utils.EntityControl(clearAmount, spawnLimit, breedingLimit, editorState);
                entityControl.setAttackDamage(attackDamage);
                entityControl.setHealth(health);
                entityControl.setSpeed(speed);
                entities[entity.ordinal()] = entityControl;
            }

            outFile.set("clear.range", integers[Integers.CLEAR_RANGE.ordinal()]);
            outFile.set("clear.timer", integers[Integers.CLEAR_TIMER.ordinal()]);

            outFile.set("clear.kill", booleans[Booleans.CLEAR_ENTITIES.ordinal()]);
            outFile.set("limiter.entity", booleans[Booleans.ENTITY_LIMITER.ordinal()]);
            outFile.set("limiter.breeding", booleans[Booleans.BREEDING_LIMITER.ordinal()]);
            outFile.set("limiter.editor", booleans[Booleans.ENTITY_EDITOR.ordinal()]);
        }

        @Override
        public void executeCritical() {
            plugin.getLogger().log(Level.INFO, "entity module: no critical configuration");
        }
    }

}
