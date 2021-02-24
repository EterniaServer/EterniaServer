package br.com.eterniaserver.eterniaserver.commands;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.CommandHelp;
import br.com.eterniaserver.acf.annotation.CatchUnknown;
import br.com.eterniaserver.acf.annotation.CommandAlias;
import br.com.eterniaserver.acf.annotation.CommandPermission;
import br.com.eterniaserver.acf.annotation.Default;
import br.com.eterniaserver.acf.annotation.Description;
import br.com.eterniaserver.acf.annotation.HelpCommand;
import br.com.eterniaserver.acf.annotation.Syntax;
import br.com.eterniaserver.eterniaserver.objects.User;

import org.bukkit.entity.Player;

@CommandAlias("%title")
public class Titles extends BaseCommand {

    @Default
    @CatchUnknown
    @HelpCommand
    @Syntax("%title_syntax")
    @CommandPermission("%title_perm")
    @Description("%title_description")
    public void onDefault(CommandHelp help) {
        help.showHelp();
    }

    public void seeTitles(Player player) {
        User user = new User(player);
    }

}
