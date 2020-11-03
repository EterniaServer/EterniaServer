package br.com.eterniaserver.eterniaserver.configurations;

import br.com.eterniaserver.eterniaserver.commands.Generic;
import br.com.eterniaserver.eterniaserver.core.APIServer;
import br.com.eterniaserver.eterniaserver.enums.ConfigBooleans;
import br.com.eterniaserver.eterniaserver.enums.ConfigDoubles;
import br.com.eterniaserver.eterniaserver.enums.ConfigIntegers;
import br.com.eterniaserver.eterniaserver.enums.ConfigLists;
import br.com.eterniaserver.eterniaserver.enums.ConfigStrings;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenericCfg {

    private final String[] strings;
    private final Boolean[] booleans;
    private final Integer[] integers;
    private final Double[] doubles;
    private final List<List<?>> lists;

    protected GenericCfg(String[] strings) {
        this(strings, null, null, null, null);
    }

    protected GenericCfg(String[] strings, Boolean[] booleans) {
        this(strings, booleans, null, null, null);
    }

    protected GenericCfg(String[] strings, Boolean[] booleans, Integer[] integers) {
        this(strings, booleans, integers, null, null);
    }

    protected GenericCfg(String[] strings, Boolean[] booleans, Integer[] integers, Double[] doubles) {
        this(strings, booleans, integers, doubles, null);
    }

    protected GenericCfg(String[] strings, Boolean[] booleans, Integer[] integers, Double[] doubles, List<List<?>> lists) {

        this.strings = strings;
        this.booleans = booleans;
        this.integers = integers;
        this.doubles = doubles;
        this.lists = lists;

    }

    protected void saveFile(FileConfiguration fileConfiguration, String filePath, String path) {
        try {
            fileConfiguration.options().header("Caso precise de ajuda acesse https://github.com/EterniaServer/EterniaServer/wiki");
            fileConfiguration.save(filePath);
        } catch (IOException exception) {
            APIServer.logError("Impossível de criar arquivos em " + path, 3);
        }
    }

    protected void setBoolean(ConfigBooleans configBooleans, FileConfiguration file, FileConfiguration outFile, String key, boolean defaultValue) {
        booleans[configBooleans.ordinal()] = file.getBoolean(key, defaultValue);
        outFile.set(key, booleans[configBooleans.ordinal()]);
    }

    protected void setString(ConfigStrings configStrings, FileConfiguration file, FileConfiguration outFile, String key, String defaultValue) {
        strings[configStrings.ordinal()] = file.getString(key, defaultValue).replace('$', (char) 0x00A7);
        outFile.set(key, strings[configStrings.ordinal()]);
    }

    protected void setInteger(ConfigIntegers configIntegers, FileConfiguration file, FileConfiguration outFile, String key, int defaultValue) {
        integers[configIntegers.ordinal()] = file.getInt(key, defaultValue);
        outFile.set(key, integers[configIntegers.ordinal()]);
    }

    protected void setDouble(ConfigDoubles configDoubles, FileConfiguration file, FileConfiguration outFile, String key, double defaultValue) {
        doubles[configDoubles.ordinal()] = file.getDouble(key, defaultValue);
        outFile.set(key, doubles[configDoubles.ordinal()]);
    }

    protected void setList(ConfigLists configLists, FileConfiguration file, FileConfiguration outFile, String key, String... values) {
        List<String> list = file.getStringList(key);
        if (isString(list)) {
            if (list.isEmpty()) {
                list.addAll(Arrays.asList(values));
            }
            lists.set(configLists.ordinal(), new ArrayList<>(list));
            saveList(configLists, outFile, key);
            return;
        }

        List<Material> listOut = new ArrayList<>();
        for (String material : list) {
            Material blockMaterial = Material.getMaterial(material);
            if (blockMaterial == null) {
                APIServer.logError("Configuração de elevadores material " + material + " não encontrado", 2);
                continue;
            }
            listOut.add(blockMaterial);
        }

        if (listOut.isEmpty()) {
            for (String material : values) {
                listOut.add(Material.getMaterial(material));
            }
        }

        lists.set(configLists.ordinal(), new ArrayList<>(listOut));
        saveList(configLists, outFile, key);
    }

    private void saveList(ConfigLists configLists, FileConfiguration fileConfiguration, String key) {
        List<?> list = lists.get(configLists.ordinal());
        if (isString(list)) {
            fileConfiguration.set(key, list);
            return;
        }

        List<String> listOut = new ArrayList<>();
        for (Object object : list) {
            listOut.add(((Material) object).name());
        }
        fileConfiguration.set(key, listOut);
    }

    private boolean isString(List<?> list) {
        try {
            return list.get(0).getClass().equals(String.class);
        } catch (IndexOutOfBoundsException e) {
            return true;
        }
    }


}
