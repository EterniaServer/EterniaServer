package br.com.eterniaserver.eterniaserver.configurations;

import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.api.ServerRelated;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.Doubles;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.enums.Lists;
import br.com.eterniaserver.eterniaserver.enums.Strings;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class GenericCfg {

    private final String[] strings;
    private final Boolean[] booleans;
    private final Integer[] integers;
    private final Double[] doubles;
    private final List<List<String>> lists;

    protected GenericCfg(String[] strings, Boolean[] booleans, Integer[] integers, Double[] doubles, List<List<String>> lists) {

        this.strings = strings;
        this.booleans = booleans;
        this.integers = integers;
        this.doubles = doubles;
        this.lists = lists;

    }

    protected void saveFile(FileConfiguration fileConfiguration, String filePath) {
        try {
            fileConfiguration.options().header("Caso precise de ajuda acesse https://github.com/EterniaServer/EterniaServer/wiki");
            fileConfiguration.save(filePath);
        } catch (IOException exception) {
            ServerRelated.logError("Impossível de criar arquivos em " + Constants.DATA_LAYER_FOLDER_PATH, 3);
        }
    }

    protected void setBoolean(Booleans configBooleans, FileConfiguration file, FileConfiguration outFile, String key, boolean defaultValue) {
        booleans[configBooleans.ordinal()] = file.getBoolean(key, defaultValue);
        outFile.set(key, booleans[configBooleans.ordinal()]);
    }

    protected void setString(Strings configStrings, FileConfiguration file, FileConfiguration outFile, String key, String defaultValue) {
        strings[configStrings.ordinal()] = file.getString(key, defaultValue).replace('$', (char) 0x00A7);
        outFile.set(key, strings[configStrings.ordinal()]);
    }

    protected void setInteger(Integers configIntegers, FileConfiguration file, FileConfiguration outFile, String key, int defaultValue) {
        integers[configIntegers.ordinal()] = file.getInt(key, defaultValue);
        outFile.set(key, integers[configIntegers.ordinal()]);
    }

    protected void setDouble(Doubles configDoubles, FileConfiguration file, FileConfiguration outFile, String key, double defaultValue) {
        doubles[configDoubles.ordinal()] = file.getDouble(key, defaultValue);
        outFile.set(key, doubles[configDoubles.ordinal()]);
    }

    protected void setList(Lists configLists, FileConfiguration file, FileConfiguration outFile, String key, String... values) {
        List<String> list = file.getStringList(key);
        if (list.isEmpty()) {
            list.addAll(Arrays.asList(values));
        }
        lists.set(configLists.ordinal(), list);
        outFile.set(key, lists.get(configLists.ordinal()));
    }

    protected void setMaterials(List<Material> elevatorMaterials, FileConfiguration file, FileConfiguration outFile, String... values) {
        elevatorMaterials.clear();

        List<String> list = file.getStringList("elevator.block");
        if (list.isEmpty()) {
            list.addAll(Arrays.asList(values));
        }

        for (String value : list) {
            Material material = Material.getMaterial(value);
            if (material == null) {
                ServerRelated.logError("Configuração de elevadores material " + value + " não encontrado", 2);
            } else {
                elevatorMaterials.add(material);
            }
        }

        outFile.set("elevator.block", list);
    }

}
