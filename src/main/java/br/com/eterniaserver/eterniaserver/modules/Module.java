package br.com.eterniaserver.eterniaserver.modules;

public interface Module {

    void loadConfigurations();

    void loadCommandsCompletions();

    void loadConditions();

    void loadListeners();

    void loadSchedules();

    void loadCommands();
}
