package br.com.eterniaserver.eterniaserver.configurations.configs;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.configurations.GenericCfg;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.objects.EntityControl;

public class EntityCfg extends GenericCfg {
    public EntityCfg(Boolean[] booleans, Integer[] integers, EntityControl[] entities) {

        super(null, booleans, integers, null, null);

        FileConfiguration file = YamlConfiguration.loadConfiguration(new File(Constants.ENTITY_FILE_PATH));
        FileConfiguration outFile = new YamlConfiguration();

        setBoolean(Booleans.CLEAR_ENTITIES, file, outFile, "modules.clear", false);
        setBoolean(Booleans.ENTITY_LIMITER, file, outFile, "modules.entity-limiter", false);
        setBoolean(Booleans.BREEDING_LIMITER, file, outFile, "modules.breeding-limiter", false);
        setBoolean(Booleans.ENTITY_EDITOR, file, outFile, "modules.entity-editor", false);

        setInteger(Integers.CLEAR_RANGE, file, outFile, "clear.range", 1);
        setInteger(Integers.CLEAR_TIMER, file, outFile, "clear.timer", 30);

        for (EntityType entity : EntityType.values()) {
            String entry = "types." + entity.name() + ".";
            int clearAmount = file.getInt(entry + "clear", -1);
            int spawnLimit = file.getInt(entry + "spawn-limit", -1);
            int breedingLimit = file.getInt(entry + "breeding-limit", -1);
            boolean editorState = file.getBoolean(entry + "editor.state", false);
            double health = file.getDouble(entry + "editor.health", 20.0);
            double attackDamage = file.getDouble(entry + "editor.attack-damage", 20.0);
            double speed = file.getDouble(entry + "editor.speed", 20.0);

            outFile.set(entry + "clear", clearAmount);
            outFile.set(entry + "spawn-limit", spawnLimit);
            outFile.set(entry + "breeding-limit", breedingLimit);
            outFile.set(entry + "editor.state", editorState);
            outFile.set(entry + "editor.health", health);
            outFile.set(entry + "editor.attack-damage", attackDamage);
            outFile.set(entry + "editor.speed", speed);  
            
            EntityControl entityControl = new EntityControl(clearAmount, spawnLimit, breedingLimit, editorState);
            entityControl.setAttackDamage(attackDamage);
            entityControl.setHealth(health);
            entityControl.setSpeed(speed);
            entities[entity.ordinal()] = entityControl;
        }

        saveFile(outFile, Constants.ENTITY_FILE_PATH);

    }
}
