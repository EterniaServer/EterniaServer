package br.com.eterniaserver.eterniaserver.configurations.locales;

import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.core.APIServer;
import br.com.eterniaserver.eterniaserver.enums.Commands;
import br.com.eterniaserver.eterniaserver.objects.CommandLocale;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CommandsLocaleCfg {

    private final String[] commands;
    private final String[] syntax;
    private final String[] descriptions;
    private final String[] perms;

    public CommandsLocaleCfg() {
        Commands[] commands = Commands.values();
        this.commands = new String[Commands.values().length];
        this.syntax = new String[Commands.values().length];
        this.descriptions = new String[Commands.values().length];
        this.perms = new String[Commands.values().length];

        Map<String, CommandLocale> defaults = new HashMap<>();
        this.addDefault(defaults, Commands.CASH, "cash", "eternia.cash", null, "  Abre a GUI da loja de Cash");
        this.addDefault(defaults, Commands.CASH_PAY, "pay", "eternia.cash", "<jogador> <quantia>", " Paga uma quantia de cash a um jogador");
        this.addDefault(defaults, Commands.CASH_ACCEPT, "accept", "eternia.cash", null, " Aceita uma compra da loja de cash");
        this.addDefault(defaults, Commands.CASH_DENY, "deny", "eternia.cash", null, " Recusa uma compra da loja de cash");
        this.addDefault(defaults, Commands.CASH_BALANCE, "balance", "eternia.cash", "<jogador>", " Mostra o saldo atual de cash de um jogador");
        this.addDefault(defaults, Commands.CASH_GIVE, "give", "eternia.cash.admin", "<jogador> <quantia>", " Dá uma quantia de cash a um jogador");
        this.addDefault(defaults, Commands.CASH_REMOVE, "remove", "eternia.cash", "<jogador> <quantia>", " Remove uma quantia de cash de um jogador");
        this.addDefault(defaults, Commands.CASH_HELP, "help", "eternia.cash.admin", "<página>", " Ajuda para o sistema de Cash");

        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(Constants.COMMANDS_LOCALE_FILE_PATH));

        for (Commands commandsEnum : commands) {
            CommandLocale commandLocale = defaults.get(commandsEnum.name());

            this.commands[commandsEnum.ordinal()] = config.getString(commandsEnum.name() + ".name", commandLocale.name);
            config.set(commandsEnum.name() + ".name", this.commands[commandsEnum.ordinal()]);

            if (commandLocale.syntax != null) {
                this.syntax[commandsEnum.ordinal()] = config.getString(commandsEnum.name() + ".syntax", commandLocale.syntax);
                config.set(commandsEnum.name() + ".syntax", this.syntax[commandsEnum.ordinal()]);
            }

            this.descriptions[commandsEnum.ordinal()] = config.getString(commandsEnum.name() + ".description", commandLocale.description);
            config.set(commandsEnum.name() + ".description", this.descriptions[commandsEnum.ordinal()]);

            this.perms[commandsEnum.ordinal()] = config.getString(commandsEnum.name() + ".perm", commandLocale.perm);
            config.set(commandsEnum.name() + ".perm", commandLocale.perm);

        }

        if (new File(Constants.DATA_LOCALE_FOLDER_PATH).mkdir()) {
            APIServer.logError("Pasta de locales criada com sucesso", 1);
        }

        try {
            config.save(Constants.COMMANDS_LOCALE_FILE_PATH);
        } catch (IOException exception) {
            APIServer.logError("Impossível de criar arquivos em " + Constants.DATA_LOCALE_FOLDER_PATH, 3);
        }

        defaults.clear();

    }

    private void addDefault(Map<String, CommandLocale> defaults, Commands id, String name, String perm, String syntax, String description) {
        CommandLocale commandLocale = new CommandLocale(id, name, syntax, description, perm);
        defaults.put(id.name(), commandLocale);
    }

    public String getName(Commands id) {
        return commands[id.ordinal()];
    }

    public String getSyntax(Commands id) {
        return syntax[id.ordinal()];
    }

    public String getDescription(Commands id) {
        return descriptions[id.ordinal()];
    }

    public String getPerm(Commands id) {
        return perms[id.ordinal()];
    }

}
