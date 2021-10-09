package br.com.eterniaserver.eterniaserver.api;

public interface Module {

    void loadConfigurations();

    void loadCommandsLocales();

    void loadCommandsCompletions();

    void loadListeners();

    void loadSchedules();

    void loadCommands();

    void reloadConfigurations();

}
