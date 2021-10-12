package br.com.eterniaserver.eterniaserver.configurations.locales;

import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Commands;
import br.com.eterniaserver.eterniaserver.objects.CommandLocale;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CommandsLocaleCfg {

    private final Map<String, CommandLocale> defaults;
    private final String[] commands;
    private final String[] syntax;
    private final String[] descriptions;
    private final String[] perms;
    private final String[] aliases;

    public CommandsLocaleCfg(final EterniaServer plugin) {
        this.defaults = new HashMap<>();
        this.commands = new String[Commands.values().length];
        this.syntax = new String[Commands.values().length];
        this.descriptions = new String[Commands.values().length];
        this.perms = new String[Commands.values().length];
        this.aliases = new String[Commands.values().length];

        this.addDefault(Commands.SENDMESSAGE, "sendmessage", "eternia.sendmessage", " <jogador> <mensagem>", " Envia uma mensagem a um joagdor", null);
        this.addDefault(Commands.CASH, "cash", "eternia.cash", null, "  Abre a GUI da loja de Cash", null);
        this.addDefault(Commands.CASH_PAY, "pay", "eternia.cash", " <jogador> <quantia>", " Paga uma quantia de cash a um jogador", null);
        this.addDefault(Commands.CASH_ACCEPT, "accept", "eternia.cash", null, " Aceita uma compra da loja de cash", null);
        this.addDefault(Commands.CASH_DENY, "deny", "eternia.cash", null, " Recusa uma compra da loja de cash", null);
        this.addDefault(Commands.CASH_BALANCE, "balance", "eternia.cash", " <jogador>", " Mostra o saldo atual de cash de um jogador", null);
        this.addDefault(Commands.CASH_GIVE, "give", "eternia.cash.admin", " <jogador> <quantia>", " Dá uma quantia de cash a um jogador", null);
        this.addDefault(Commands.CASH_REMOVE, "remove", "eternia.cash.admin", " <jogador> <quantia>", " Remove uma quantia de cash de um jogador", null);
        this.addDefault(Commands.CASH_HELP, "help", "eternia.cash.admin", " <página>", " Ajuda para o sistema de Cash", null);
        this.addDefault(Commands.CHANNEL, "ch|channel", "eternia.chat.channel", " <página>", " Ajuda para o sistema de Canais", null);
        this.addDefault(Commands.CHAT, "chat", "eternia.chat.help", " <página>", " Ajuda para o sistema de Chat", null);
        this.addDefault(Commands.CHAT_CLEAR, "clear", "eternia.chat.clear", null, " Limpa todo o chat", "clearchat|limparchat");
        this.addDefault(Commands.CHAT_BROADCAST, "broadcast", "eternia.broadcast", " <msg>", " Envie uma mensagem para todo o servidor", "broadcast|advice|aviso");
        this.addDefault(Commands.CHAT_VANISH, "vanish", "eternia.vanish", null, " Se esconda dos jogadores", "chupadadimensional|vanish");
        this.addDefault(Commands.CHAT_SPY, "spy", "eternia.spy", null, " Ative o spy de todos os chats", "spy");
        this.addDefault(Commands.CHAT_REPLY, "reply", "eternia.tell", " <msg>", " Responde um jogador", "r|w|reply");
        this.addDefault(Commands.CHAT_TELL, "tell", "eternia.tell", " <jogador> <msg>", " Envia uma mensagem privada a um jogador", "tell|msg|whisper");
        this.addDefault(Commands.ECO, "eco", "eternia.money.user", " <página>", " Ajuda para o sistema de Economia", null);
        this.addDefault(Commands.ECO_SET, "set", "eternia.money.admin", " <jogador> <quantia>", " Define o saldo de um jogador", null);
        this.addDefault(Commands.ECO_TAKE, "take", "eternia.money.admin", " <jogador> <quantia>", " Retira uma quantia de saldo de um jogador", null);
        this.addDefault(Commands.ECO_GIVE, "give", "eternia.money.admin", " <jogador> <quantia>", " Dar uma quantia de saldo a um jogador", null);
        this.addDefault(Commands.ECO_MONEY, "money", "eternia.money.user", " <jogador>", " Verifica o saldo de um jogador", "money|balance");
        this.addDefault(Commands.ECO_PAY, "pay", "eternia.money.user", " <jogador> <quantia>", " Paga uma quantia a um jogador", "pay");
        this.addDefault(Commands.ECO_BALTOP, "baltop", "eternia.money.user", null, " Verifica a lista de mais ricos", "baltop");
        this.addDefault(Commands.ECO_GEN_PRICES, "genprice", "eternia.money.admin", null, " Gere a economia se baseando em um arquivo xlsx", null);
        this.addDefault(Commands.XP_DEPOSIT, "deposit", "eternia.xp.user", " <quantia>", " Guarda uma quantia de nível", null);
        this.addDefault(Commands.SPEED, "speed", "eternia.speed", " <valor>", " Define a velocidade do jogador", null);
        this.addDefault(Commands.GOD, "god", "eternia.god", " <jogador>", " Ativa o modo deus para você ou para um joador", null);
        this.addDefault(Commands.PROFILE, "profile", "eternia.profile", " <jogador>", " Veja o seu perfil ou o de outro jogador", null);
        this.addDefault(Commands.MEM, "mem", "eternia.mem", null, " Veja informações do servidor", null);
        this.addDefault(Commands.MEM_ALL, "memall", "eternia.mem.all", null, " Mostre globalmente as informações do servidor", null);
        this.addDefault(Commands.FLY, "fly", "eternia.fly", " <jogador>", " Ative o seu ou o fly de outro jogador", null);
        this.addDefault(Commands.FLY_DEBUG, "flydebug", "eternia.admin", null, " Desativa o fly de todos jogadores", null);
        this.addDefault(Commands.FEED, "feed", "eternia.feed", " <jogador>", " Se sacie ou sacie um jogador", null);
        this.addDefault(Commands.CONDENSER, "blocks|condenser", "eternia.blocks", null, " Comprima seus minérios em blocos para liberar espaço", null);
        this.addDefault(Commands.THOR, "thor", "eternia.thor", " <jogador>", " Solte um raio ou puna um jogador", null);
        this.addDefault(Commands.SUICIDE, "suicide", "eternia.suicide", " <mensagem>", " Se mate e envie uma mensagem após isso", null);
        this.addDefault(Commands.AFK, "afk", "eternia.afk", null, " Fique ausente", null);
        this.addDefault(Commands.GLOW, "glow", "eternia.glow", null, " Brilhe como você merece", null);
        this.addDefault(Commands.GLOW_COLOR, "color", "eternia.glow", " <cor>", " Escolha a cor que você deseja brilhar", null);
        this.addDefault(Commands.HOME, "home", "eternia.home", " <home> <jogador>", " Vá até sua home ou a de outro jogador", null);
        this.addDefault(Commands.DELHOME, "delhome", "eternia.delhome", " <home>", " Delete uma home sua", null);
        this.addDefault(Commands.HOMES, "homes", "eternia.homes", " <jogador>", " Veja a sua lista de homes ou a de outro jogador", null);
        this.addDefault(Commands.SETHOME, "sethome", "eternia.sethome", " <home>", " Defina uma nova home ou uma bússula de teleporte", null);
        this.addDefault(Commands.HAT, "hat", "eternia.hat", null, " Coloque seu caçapete", null);
        this.addDefault(Commands.WORKBENCH, "workbench", "eternia.workbench", null, " Abra uma bancada de trabalho virtual", null);
        this.addDefault(Commands.OPENINV, "openinv", "eternia.openinv", " <jogador>", " Abra o inventário de um jogador", null);
        this.addDefault(Commands.ENDERCHEST, "enderchest", "eternia.enderchest", " <jogador>", " Abra o seu enderchest ou o de um jogador", null);
        this.addDefault(Commands.ITEM, "item", "eternia.item", " <página>", " Ajuda para o sistema de Items", null);
        this.addDefault(Commands.ITEM_NBT, "nbt", "eternia.item.nbt", " <página>", " Ajuda para o sistema de NBT de Items", null);
        this.addDefault(Commands.ITEM_NBT_ADDSTRING, "addstring", "eternia.item.nbt", " <chave> <valor>", " Adiciona uma chave e um valor NBT ao item", null);
        this.addDefault(Commands.ITEM_NBT_ADDINT, "addint", "eternia.item.nbt", " <chave> <valor>", " Adiciona uma chave e um valor NBT ao item", null);
        this.addDefault(Commands.ITEM_NBT_SETCOMMANDLIST, "setcommand", "eternia.item.nbt", " <comando>", " Define o comando a ser rodado ao usar o item", null);
        this.addDefault(Commands.ITEM_NBT_DEFINECONSOLE, "defineconsole", "eternia.item.nbt", " <1 ou 0>", " Define se os comandos serão rodados pelo console ou pelo jogador", null);
        this.addDefault(Commands.ITEM_NBT_ADDCOMMAND, "addcommand", "eternia.item.nbt", " <comando>", " Adiciona um comando a mais para ser executado", null);
        this.addDefault(Commands.ITEM_NBT_SETUSAGES, "setusages", "eternia.item.nbt", " <valor>", " Define a quantia de vezes que o item pode ser usado", null);
        this.addDefault(Commands.ITEM_SEND_CUSTON, "sendcustom", "eternia.item.nbt", " <jogador> <usos> <console> <tipo do item> <comandos...> (separado por ;)", "Envia um item custom para um jogador", null);
        this.addDefault(Commands.ITEM_CLEAR, "clear", "eternia.item", " <página>", " Ajuda para o sistema de Clear de Items", null);
        this.addDefault(Commands.ITEM_CLEAR_LORE, "lore", "eternia.item", null, " Remove a lore de um item", null);
        this.addDefault(Commands.ITEM_CLEAR_NAME, "name", "eternia.item", null, " Remove o nome de um item", null);
        this.addDefault(Commands.ITEM_ADD_LORE, "add lore", "eternia.item", " <lore>", " Adiciona uma nova linha de lore ao item", null);
        this.addDefault(Commands.ITEM_SET, "set", "eternia.item", " <página>", " Ajuda para o sistema de Set de Items", null);
        this.addDefault(Commands.ITEM_SET_LORE, "lore", "eternia.item", " <lore>", " Define a lore do item", null);
        this.addDefault(Commands.ITEM_SET_NAME, "name", "eternia.item", " <nome>", " Define o nome do item", null);
        this.addDefault(Commands.ITEM_CUSTOM_MODEL, "setcustommodeldata", "eternia.item", " <id>", " Define uma custom model data para um item", null);
        this.addDefault(Commands.KIT, "kit", "eternia.kit", " <kit>", " Escolha um kit para pegar", null);
        this.addDefault(Commands.KITS, "kits", "eternia.kits", null, " Veja a lista de kits disponíveis", null);
        this.addDefault(Commands.MUTE, "mute", "eternia.mute", " <página>", " Receba ajuda para o sistema de Mute", null);
        this.addDefault(Commands.MUTE_CHANNELS, "channels|muteall", "eternia.mute", null, " Muta todos os canais", null);
        this.addDefault(Commands.MUTE_PERMA, "perma", "eternia.mute.perma", " <jogador> <mensagem>", " Muta um jogador permanentemente", null);
        this.addDefault(Commands.MUTE_UNDO, "unmute", "eternia.mute", " <jogador>", " Desmuta um jogador", null);
        this.addDefault(Commands.MUTE_TEMP, "temp", "eternia.mute", " <jogador> <tempo> <mensagem>", " Muta um jogador temporariamente", null);
        this.addDefault(Commands.NICK, "nick", "eternia.nickname", " <novo nome> ou <novo nome> <jogador>", " Altera o seu apelido ou de um jogador", null);
        this.addDefault(Commands.TPALL, "tpall", "eternia.tpall", null, " Teleporte todos jogadores até você", null);
        this.addDefault(Commands.BACK, "back", "eternia.back", null, " Volte ao seu ultimo local", null);
        this.addDefault(Commands.TPA, "tpa", "eternia.tpa", " <jogador>", " Envie um pedido de tpa a um jogador", null);
        this.addDefault(Commands.TPA_ACCEPT, "accept", "eternia.tpa", null, " Aceite um pedido de tpa", null);
        this.addDefault(Commands.TPA_DENY, "deny", "eternia.tpa", null, " Negue um pedido de tpa", null);
        this.addDefault(Commands.SPAWN, "spawn", "eternia.spawn", " <jogador>", " Vá ou leve alguém até o spawner", null);
        this.addDefault(Commands.SPAWNSET, "setspawn", "eternia.setspawn", null, " Defina a localização do spawn", null);
        this.addDefault(Commands.SHOP, "shop|loja", "eternia.shop.player", " <jogador>", " Vá até a loja geral ou a de alguém", null);
        this.addDefault(Commands.SETSHOP, "setshop|setloja", "eternia.setshop", null, " Defina a sua loja", null);
        this.addDefault(Commands.DELSHOP, "delshop|delloja", "eternia.setshop", null, " Delete a sua loja", null);
        this.addDefault(Commands.WARP, "warp", "eternia.warp", " <warp>", " Vá até uma warp", null);
        this.addDefault(Commands.SETWARP, "setwarp", "eternia.setwarp", " <warp>", " Define uma nova warp", null);
        this.addDefault(Commands.DELWARP, "delwarp", "eternia.delwarp", " <warp>", " Delete uma warp", null);
        this.addDefault(Commands.LISTWARP, "warps", "eternia.listwarp", null, " Veja todas as warps disponíveis", null);
        this.addDefault(Commands.TITLE, "title", "eternia.title", " <página>", " Ajuda para o sistema de titulos", null);

        final FileConfiguration config = YamlConfiguration.loadConfiguration(new File(Constants.COMMANDS_LOCALE_FILE_PATH));

        for (Commands commandsEnum : Commands.values()) {
            CommandLocale commandLocale = defaults.get(commandsEnum.name());
            if (commandLocale == null) {
                continue;
            }

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
            plugin.logError("Pasta de locales criada com sucesso", 1);
        }

        try {
            config.save(Constants.COMMANDS_LOCALE_FILE_PATH);
        } catch (IOException exception) {
            plugin.logError("Impossível de criar arquivos em " + Constants.DATA_LOCALE_FOLDER_PATH, 3);
        }

    }

    private void addDefault(Commands id, String name, String perm, String syntax, String description, String aliases) {
        final CommandLocale commandLocale = new CommandLocale(id, name, syntax, description, perm, aliases);
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
