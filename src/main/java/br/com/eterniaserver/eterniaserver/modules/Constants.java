package br.com.eterniaserver.eterniaserver.modules;

import java.io.File;

public class Constants {

    public static final String DATA_LAYER_FOLDER_PATH = "plugins" + File.separator + "EterniaServer";
    public static final String DATA_MODULE_FOLDER_PATH = DATA_LAYER_FOLDER_PATH + File.separator + "modules";

    public static final String CORE_CONFIG_FILE_PATH = DATA_MODULE_FOLDER_PATH + File.separator + "core.yml";
    public static final String CORE_COMMAND_FILE_PATH = DATA_MODULE_FOLDER_PATH + File.separator + "core-cmd.yml";

    public static final String SPAWNER_MODULE_FOLDER_PATH = DATA_MODULE_FOLDER_PATH + File.separator + "spawner";
    public static final String SPAWNER_CONFIG_FILE_PATH = SPAWNER_MODULE_FOLDER_PATH + File.separator + "spawner.yml";
    public static final String SPAWNER_COMMAND_FILE_PATH = SPAWNER_MODULE_FOLDER_PATH + File.separator + "spawner-cmd.yml";
    public static final String SPAWNER_MESSAGE_FILE_PATH = SPAWNER_MODULE_FOLDER_PATH + File.separator + "spawner-msg.yml";

}
