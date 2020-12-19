package br.com.eterniaserver.eterniaserver.commands;

import org.bukkit.command.CommandSender;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.CommandHelp;
import br.com.eterniaserver.acf.annotation.CommandAlias;
import br.com.eterniaserver.acf.annotation.CommandCompletion;
import br.com.eterniaserver.acf.annotation.CommandPermission;
import br.com.eterniaserver.acf.annotation.Default;
import br.com.eterniaserver.acf.annotation.Description;
import br.com.eterniaserver.acf.annotation.HelpCommand;
import br.com.eterniaserver.acf.annotation.Optional;
import br.com.eterniaserver.acf.annotation.Subcommand;
import br.com.eterniaserver.acf.annotation.Syntax;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;

@CommandAlias("%intern")
public class Intern extends BaseCommand {

    private final EterniaServer plugin;

    public Intern(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @Default
    @HelpCommand
    @Syntax("%intern_syntax")
    @CommandPermission("%intern_perm")
    @Description("%intern_description")
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

    @CommandAlias("%settings")
    @Syntax("%settings_syntax")
    @CommandPermission("%settings_perm")
    @CommandCompletion("entities_module|entities_editor|entities_limiter|entities_breeding")
    @Description("%settings_description")
    public void changeState(CommandSender sender, String type, @Optional Boolean state) {
        if ((state == null && !EterniaServer.changeState(type)) || (state != null && !EterniaServer.changeState(type, state))) {
            EterniaServer.sendMessage(sender, Messages.SETTINGS_WRONG);
            return;
        }
        EterniaServer.sendMessage(sender, Messages.SETTINGS_CHANGE, type);
    }

    @Subcommand("%reload")
    @Syntax("%reload_syntax")
    @Description("%reload_description")
    @CommandPermission("%reload_perm")
    @CommandCompletion("configs|messages|constants|blocks|chat|kits|cash|rewards|schedules|entity")
    public void onReload(CommandSender sender, String module) {
        switch (module) {
            case "configs":
                plugin.configs();
                break;
            case "messages":
                plugin.messages();
                break;
            case "constants":
                plugin.constants();
                break;
            case "blocks":
                plugin.blocks();
                break;
            case "chat":
                plugin.chat();
                break;
            case "kits":
                plugin.kits();
                break;
            case "cash":
                plugin.cash();
                break;
            case "rewards":
                plugin.rewards();
                break;
            case "schedules":
                plugin.schedule();
                break;
            case "entity":
                plugin.entity();
                break;
            default:
                plugin.configs();
                plugin.messages();
                plugin.constants();
                plugin.blocks();
                plugin.chat();
                plugin.kits();
                plugin.cash();
                plugin.rewards();
                plugin.schedule();
                plugin.entity();
                EterniaServer.sendMessage(sender, Messages.RELOAD_ALL);
                return;
        }
        EterniaServer.sendMessage(sender, Messages.RELOAD_MODULE, module);
    }

}
