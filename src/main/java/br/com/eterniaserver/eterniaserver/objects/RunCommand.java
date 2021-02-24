package br.com.eterniaserver.eterniaserver.objects;

import br.com.eterniaserver.eternialib.core.interfaces.CommandConfirmable;

public class RunCommand implements CommandConfirmable {

    private final Runnable runnable;

    public RunCommand(final Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void execute() {
        runnable.run();
    }

}
