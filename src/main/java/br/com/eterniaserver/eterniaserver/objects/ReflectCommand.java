package br.com.eterniaserver.eterniaserver.objects;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

final class ReflectCommand extends Command {

    private final AbstractCommand exe;

    protected ReflectCommand(String command, AbstractCommand exe) {
        super (command);
        this.exe = exe;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        return exe.onCommand(sender, this, commandLabel, args);
    }

}