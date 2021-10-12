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

    public static final String EXPERIENCE_MODULE_FOLDER_PATH = DATA_MODULE_FOLDER_PATH + File.separator + "experience";
    public static final String EXPERIENCE_CONFIG_FILE_PATH = EXPERIENCE_MODULE_FOLDER_PATH + File.separator + "experience.yml";
    public static final String EXPERIENCE_COMMAND_FILE_PATH = EXPERIENCE_MODULE_FOLDER_PATH + File.separator + "experience-cmd.yml";
    public static final String EXPERIENCE_MESSAGE_FILE_PATH = EXPERIENCE_MODULE_FOLDER_PATH + File.separator + "experience-msg.yml";

    public static final String ELEVATOR_MODULE_FOLDER_PATH = DATA_MODULE_FOLDER_PATH + File.separator + "elevator";
    public static final String ELEVATOR_CONFIG_FILE_PATH = ELEVATOR_MODULE_FOLDER_PATH + File.separator + "elevator.yml";

    public static final String REWARDS_MODULE_FOLDER_PATH = DATA_MODULE_FOLDER_PATH + File.separator + "rewards";
    public static final String REWARDS_CONFIG_FILE_PATH = REWARDS_MODULE_FOLDER_PATH + File.separator + "rewards.yml";
    public static final String REWARDS_COMMAND_FILE_PATH = REWARDS_MODULE_FOLDER_PATH + File.separator + "rewards-cmd.yml";
    public static final String REWARDS_MESSAGE_FILE_PATH = REWARDS_MODULE_FOLDER_PATH + File.separator + "rewards-msg.yml";

}
