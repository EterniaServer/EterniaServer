package br.com.eterniaserver.eterniaserver.configurations.locales;

import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.api.ServerRelated;
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
    private final String[] aliases;

    public CommandsLocaleCfg() {

        this.commands = new String[Commands.values().length];
        this.syntax = new String[Commands.values().length];
        this.descriptions = new String[Commands.values().length];
        this.perms = new String[Commands.values().length];
        this.aliases = new String[Commands.values().length];

        Map<String, CommandLocale> defaults = new HashMap<>();

        this.addDefault(defaults, Commands.CASH, "cash", "eternia.cash", null, "  Abre a GUI da loja de Cash", null);
        this.addDefault(defaults, Commands.CASH_PAY, "pay", "eternia.cash", " <jogador> <quantia>", " Paga uma quantia de cash a um jogador", null);
        this.addDefault(defaults, Commands.CASH_ACCEPT, "accept", "eternia.cash", null, " Aceita uma compra da loja de cash", null);
        this.addDefault(defaults, Commands.CASH_DENY, "deny", "eternia.cash", null, " Recusa uma compra da loja de cash", null);
        this.addDefault(defaults, Commands.CASH_BALANCE, "balance", "eternia.cash", " <jogador>", " Mostra o saldo atual de cash de um jogador", null);
        this.addDefault(defaults, Commands.CASH_GIVE, "give", "eternia.cash.admin", " <jogador> <quantia>", " Dá uma quantia de cash a um jogador", null);
        this.addDefault(defaults, Commands.CASH_REMOVE, "remove", "eternia.cash.admin", " <jogador> <quantia>", " Remove uma quantia de cash de um jogador", null);
        this.addDefault(defaults, Commands.CASH_HELP, "help", "eternia.cash.admin", " <página>", " Ajuda para o sistema de Cash", null);
        this.addDefault(defaults, Commands.CHANNEL, "ch|channel", "eternia.chat.channel", " <página>", " Ajuda para o sistema de Canais", null);
        this.addDefault(defaults, Commands.CHAT, "chat", "eternia.chat.help", " <página>", " Ajuda para o sistema de Chat", null);
        this.addDefault(defaults, Commands.CHAT_CLEAR, "clear", "eternia.chat.clear", null, " Limpa todo o chat", "clearchat|limparchat");
        this.addDefault(defaults, Commands.CHAT_BROADCAST, "broadcast", "eternia.broadcast", " <msg>", " Envie uma mensagem para todo o servidor", "broadcast|advice|aviso");
        this.addDefault(defaults, Commands.CHAT_VANISH, "vanish", "eternia.vanish", null, " Se esconda dos jogadores", "chupadadimensional|vanish");
        this.addDefault(defaults, Commands.CHAT_SPY, "spy", "eternia.spy", null, " Ative o spy de todos os chats", "spy");
        this.addDefault(defaults, Commands.CHAT_REPLY, "reply", "eternia.tell", " <msg>", " Responde um jogador", "r|w|reply");
        this.addDefault(defaults, Commands.CHAT_TELL, "tell", "eternia.tell", " <jogador> <msg>", " Envia uma mensagem privada a um jogador", "tell|msg|whisper");
        this.addDefault(defaults, Commands.ECO, "eco", "eternia.money.user", " <página>", " Ajuda para o sistema de Economia", null);
        this.addDefault(defaults, Commands.ECO_SET, "set", "eternia.money.admin", " <jogador> <quantia>", " Define o saldo de um jogador", null);
        this.addDefault(defaults, Commands.ECO_TAKE, "take", "eternia.money.admin", " <jogador> <quantia>", " Retira uma quantia de saldo de um jogador", null);
        this.addDefault(defaults, Commands.ECO_GIVE, "give", "eternia.money.admin", " <jogador> <quantia>", " Dar uma quantia de saldo a um jogador", null);
        this.addDefault(defaults, Commands.ECO_MONEY, "money", "eternia.money.user", " <jogador>", " Verifica o saldo de um jogador", "money|balance");
        this.addDefault(defaults, Commands.ECO_PAY, "pay", "eternia.money.user", " <jogador> <quantia>", " Paga uma quantia a um jogador", "pay");
        this.addDefault(defaults, Commands.ECO_BALTOP, "baltop", "eternia.money.user", null, " Verifica a lista de mais ricos", "baltop");
        this.addDefault(defaults, Commands.XP, "xp", "eternia.xp.user", " <página>", " Ajuda para o sistema de Experiência", null);
        this.addDefault(defaults, Commands.XP_SET, "set", "eternia.xp.admin", " <jogador> <quantia>", " Define o nível do jogador", null);
        this.addDefault(defaults, Commands.XP_TAKE, "take", "eternia.xp.admin", " <jogador> <quantia>", " Retira uma quantidade de nível do jogador", null);
        this.addDefault(defaults, Commands.XP_GIVE, "give", "eternia.xp.admin", " <jogador> <quantia>", " Dá uma quantidade de nível do jogador", null);
        this.addDefault(defaults, Commands.XP_CHECK, "check", "eternia.xp.user", null, " Verifica quantos leveis você possui guardado", null);
        this.addDefault(defaults, Commands.XP_BOTTLE, "bottle", "eternia.xp.user", " <quantia>", " Converte uma quantia de nível para uma garra de EXP", null);
        this.addDefault(defaults, Commands.XP_WITHDRAW, "withdraw", "eternia.xp.user", " <quantia>", " Retira uma quantia de nível", null);
        this.addDefault(defaults, Commands.XP_DEPOSIT, "deposit", "eternia.xp.user", " <quantia>", " Guarda uma quantia de nível", null);
        this.addDefault(defaults, Commands.XP_DEPOSIT, "deposit", "eternia.xp.user", " <quantia>", " Guarda uma quantia de nível", null);
        this.addDefault(defaults, Commands.GAMEMODE, "gamemode|gm", "eternia.gamemode", " <página>", " Ajuda para o sistema de Gamemode", null);
        this.addDefault(defaults, Commands.GAMEMODE_SURVIVAL, "survival|s|0", "eternia.gamemode", " <jogador>", " Define o modo de jogo seu ou de alguém para survival", null);
        this.addDefault(defaults, Commands.GAMEMODE_CREATIVE, "creative|c|1", "eternia.gamemode", " <jogador>", " Define o modo de jogo seu ou de alguém para criativo", null);
        this.addDefault(defaults, Commands.GAMEMODE_ADVENTURE, "adventure|a|2", "eternia.gamemode", " <jogador>", " Define o modo de jogo seu ou de alguém para aventura", null);
        this.addDefault(defaults, Commands.GAMEMODE_SPECTATOR, "spectator|spec|3", "eternia.gamemode", " <jogador>", " Define o modo de jogo seu ou de alguém para espectador", null);
        this.addDefault(defaults, Commands.SPEED, "speed", "eternia.speed", " <valor>", " Define a velocidade do jogador", null);
        this.addDefault(defaults, Commands.GOD, "god", "eternia.god", " <jogador>", " Ativa o modo deus para você ou para um joador", null);
        this.addDefault(defaults, Commands.PROFILE, "profile", "eternia.profile", " <jogador>", " Veja o seu perfil ou o de outro jogador", null);
        this.addDefault(defaults, Commands.MEM, "mem", "eternia.mem", null, " Veja informações do servidor", null);
        this.addDefault(defaults, Commands.MEM_ALL, "memall", "eternia.mem.all", null, " Mostre globalmente as informações do servidor", null);
        this.addDefault(defaults, Commands.FLY, "fly", "eternia.fly", " <jogador>", " Ative o seu ou o fly de outro jogador", null);
        this.addDefault(defaults, Commands.FLY_DEBUG, "flydebug", "eternia.admin", null, " Desativa o fly de todos jogadores", null);
        this.addDefault(defaults, Commands.FEED, "feed", "eternia.feed", " <jogador>", " Se sacie ou sacie um jogador", null);
        this.addDefault(defaults, Commands.CONDENSER, "blocks|condenser", "eternia.blocks", null, " Comprima seus minérios em blocos para liberar espaço", null);
        this.addDefault(defaults, Commands.THOR, "thor", "eternia.thor", " <jogador>", " Solte um raio ou puna um jogador", null);
        this.addDefault(defaults, Commands.SUICIDE, "suicide", "eternia.suicide", " <mensagem>", " Se mate e envie uma mensagem após isso", null);
        this.addDefault(defaults, Commands.AFK, "afk", "eternia.afk", null, " Fique ausente", null);
        this.addDefault(defaults, Commands.GLOW, "glow", "eternia.glow", null, " Brilhe como você merece", null);
        this.addDefault(defaults, Commands.GLOW_COLOR, "color", "eternia.glow", " <cor>", " Escolha a cor que você deseja brilhar", null);
        this.addDefault(defaults, Commands.HOME, "home", "eternia.home", " <home> <jogador>", " Vá até sua home ou a de outro jogador", null);
        this.addDefault(defaults, Commands.DELHOME, "delhome", "eternia.delhome", " <home>", " Delete uma home sua", null);
        this.addDefault(defaults, Commands.HOMES, "homes", "eternia.homes", " <jogador>", " Veja a sua lista de homes ou a de outro jogador", null);
        this.addDefault(defaults, Commands.SETHOME, "sethome", "eternia.sethome", " <home>", " Defina uma nova home ou uma bússula de teleporte", null);
        this.addDefault(defaults, Commands.HAT, "hat", "eternia.hat", null, " Coloque seu caçapete", null);
        this.addDefault(defaults, Commands.WORKBENCH, "workbench", "eternia.workbench", null, " Abra uma bancada de trabalho virtual", null);
        this.addDefault(defaults, Commands.OPENINV, "openinv", "eternia.openinv", " <jogador>", " Abra o inventário de um jogador", null);
        this.addDefault(defaults, Commands.ENDERCHEST, "enderchest", "eternia.enderchest", " <jogador>", " Abra o seu enderchest ou o de um jogador", null);
        this.addDefault(defaults, Commands.ITEM, "item", "eternia.item", " <página>", " Ajuda para o sistema de Items", null);
        this.addDefault(defaults, Commands.ITEM_NBT, "nbt", "eternia.item.nbt", " <página>", " Ajuda para o sistema de NBT de Items", null);
        this.addDefault(defaults, Commands.ITEM_NBT_ADDSTRING, "addstring", "eternia.item.nbt", " <chave> <valor>", " Adiciona uma chave e um valor NBT ao item", null);
        this.addDefault(defaults, Commands.ITEM_NBT_ADDINT, "addint", "eternia.item.nbt", " <chave> <valor>", " Adiciona uma chave e um valor NBT ao item", null);
        this.addDefault(defaults, Commands.ITEM_CLEAR, "clear", "eternia.item", " <página>", " Ajuda para o sistema de Clear de Items", null);
        this.addDefault(defaults, Commands.ITEM_CLEAR_LORE, "lore", "eternia.item", null, " Remove a lore de um item", null);
        this.addDefault(defaults, Commands.ITEM_CLEAR_NAME, "name", "eternia.item", null, " Remove o nome de um item", null);
        this.addDefault(defaults, Commands.ITEM_ADD_LORE, "add lore", "eternia.item", " <lore>", " Adiciona uma nova linha de lore ao item", null);
        this.addDefault(defaults, Commands.ITEM_SET, "set", "eternia.item", " <página>", " Ajuda para o sistema de Set de Items", null);
        this.addDefault(defaults, Commands.ITEM_SET_LORE, "lore", "eternia.item", " <lore>", " Define a lore do item", null);
        this.addDefault(defaults, Commands.ITEM_SET_NAME, "name", "eternia.item", " <nome>", " Define o nome do item", null);
        this.addDefault(defaults, Commands.KIT, "kit", "eternia.kit", " <kit>", " Escolha um kit para pegar", null);
        this.addDefault(defaults, Commands.KITS, "kits", "eternia.kits", null, " Veja a lista de kits disponíveis", null);
        this.addDefault(defaults, Commands.MUTE, "mute", "eternia.mute", " <página>", " Receba ajuda para o sistema de Mute", null);
        this.addDefault(defaults, Commands.MUTE_CHANNELS, "channels|muteall", "eternia.mute", null, " Muta todos os canais", null);
        this.addDefault(defaults, Commands.MUTE_PERMA, "perma", "eternia.mute.perma", " <jogador> <mensagem>", " Muta um jogador permanentemente", null);
        this.addDefault(defaults, Commands.MUTE_UNDO, "unmute", "eternia.mute", " <jogador>", " Desmuta um jogador", null);
        this.addDefault(defaults, Commands.MUTE_TEMP, "temp", "eternia.mute", " <jogador> <tempo> <mensagem>", " Muta um jogador temporariamente", null);
        this.addDefault(defaults, Commands.NICK, "nick", "eternia.nickname", " <novo nome> ou <novo nome> <jogador>", " Altera o seu apelido ou de um jogador", null);
        this.addDefault(defaults, Commands.USEKEY, "usekey", "eternia.usekey", " <chave>", " Ative uma key de rewards", null);
        this.addDefault(defaults, Commands.GENKEY, "genkey", "eterniaMessages.ECO_BALTOP_LIST.genkey", " <reward>", " Crie uma key de rewards", null);
        this.addDefault(defaults, Commands.SPAWNERGIVE, "spawnergive", "eternia.spawnergive", " <mob> <quantia> <jogador>", " Dá uma quantia de spawners para um jogador", null);
        this.addDefault(defaults, Commands.TPALL, "tpall", "eternia.tpall", null, " Teleporte todos jogadores até você", null);
        this.addDefault(defaults, Commands.BACK, "back", "eternia.back", null, " Volte ao seu ultimo local", null);
        this.addDefault(defaults, Commands.TPA, "tpa", "eternia.tpa", " <jogador>", " Envie um pedido de tpa a um jogador", null);
        this.addDefault(defaults, Commands.TPA_ACCEPT, "accept", "eternia.tpa", null, " Aceite um pedido de tpa", null);
        this.addDefault(defaults, Commands.TPA_DENY, "deny", "eternia.tpa", null, " Negue um pedido de tpa", null);
        this.addDefault(defaults, Commands.SPAWN, "spawn", "eternia.spawn", " <jogador>", " Vá ou leve alguém até o spawner", null);
        this.addDefault(defaults, Commands.SPAWNSET, "setspawn", "eternia.setspawn", null, " Defina a localização do spawn", null);
        this.addDefault(defaults, Commands.SHOP, "shop|loja", "eternia.shop.player", " <jogador>", " Vá até a loja geral ou a de alguém", null);
        this.addDefault(defaults, Commands.SETSHOP, "setshop|setloja", "eternia.setshop", null, " Defina a sua loja", null);
        this.addDefault(defaults, Commands.DELSHOP, "delshop|delloja", "eternia.setshop", null, " Delete a sua loja", null);
        this.addDefault(defaults, Commands.WARP, "warp", "eternia.warp", " <warp>", " Vá até uma warp", null);
        this.addDefault(defaults, Commands.SETWARP, "setwarp", "eternia.setwarp", " <warp>", " Define uma nova warp", null);
        this.addDefault(defaults, Commands.DELWARP, "delwarp", "eternia.delwarp", " <warp>", " Delete uma warp", null);
        this.addDefault(defaults, Commands.LISTWARP, "warps", "eternia.listwarp", null, " Veja todas as warps disponíveis", null);
        this.addDefault(defaults, Commands.RELOAD, "reload", "eternia.reload", " <modulo>", " Reinicia um módulo especifico ou todos.", null);
        this.addDefault(defaults, Commands.SETTINGS, "settings", "eternia.settings", " <setting>", "Edite alguma configuração do plugin", null);
        this.addDefault(defaults, Commands.INTERN, "eternia", "eternia.eternia", " <página>", " Receba ajuda para o sistema interno do EterniaServer plugin", null);
        this.addDefault(defaults, Commands.COMMAND, "command", "eternia.spawn", " <página>", " Receba ajuda para o sistema de confirmação de comandos", null);
        this.addDefault(defaults, Commands.COMMAND_ACCEPT, "accept", "eternia.spawn", null, " Confirme o uso de um comando", null);
        this.addDefault(defaults, Commands.COMMAND_DENY, "deny", "eternia.spawn", null, " Negue o uso de um comando", null);

        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(Constants.COMMANDS_LOCALE_FILE_PATH));

        for (Commands commandsEnum : Commands.values()) {
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

            if (commandLocale.aliases != null) {
                this.aliases[commandsEnum.ordinal()] = config.getString(commandsEnum.name() + ".aliases", commandLocale.aliases);
                config.set(commandsEnum.name() + ".aliases", this.aliases[commandsEnum.ordinal()]);
            }

        }

        if (new File(Constants.DATA_LOCALE_FOLDER_PATH).mkdir()) {
            ServerRelated.logError("Pasta de locales criada com sucesso", 1);
        }

        try {
            config.save(Constants.COMMANDS_LOCALE_FILE_PATH);
        } catch (IOException exception) {
            ServerRelated.logError("Impossível de criar arquivos em " + Constants.DATA_LOCALE_FOLDER_PATH, 3);
        }

        defaults.clear();

    }

    private void addDefault(Map<String, CommandLocale> defaults, Commands id, String name, String perm, String syntax, String description, String aliases) {
        CommandLocale commandLocale = new CommandLocale(id, name, syntax, description, perm, aliases);
        defaults.put(id.name(), commandLocale);
    }

    public String getName(Commands id) {
        return commands[id.ordinal()];
    }

    public String getSyntax(Commands id) {
        return syntax[id.ordinal()] != null ? syntax[id.ordinal()] : "";
    }

    public String getDescription(Commands id) {
        return descriptions[id.ordinal()];
    }

    public String getPerm(Commands id) {
        return perms[id.ordinal()];
    }

    public String getAliases(Commands id) {
        return aliases[id.ordinal()] != null ? aliases[id.ordinal()] : "";
    }

}
