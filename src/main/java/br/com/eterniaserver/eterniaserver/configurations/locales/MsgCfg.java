package br.com.eterniaserver.eterniaserver.configurations.locales;

import br.com.eterniaserver.eternialib.core.enums.ConfigurationCategory;
import br.com.eterniaserver.eternialib.core.interfaces.ReloadableConfiguration;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.objects.CustomizableMessage;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MsgCfg implements ReloadableConfiguration {

    private final EterniaServer plugin;
    private final String[] messages;

    private final Map<String, CustomizableMessage> defaults;

    public MsgCfg(final EterniaServer plugin, final String[] messages) {
        this.plugin = plugin;
        this.messages = messages;
        this.defaults = new HashMap<>();

        this.addDefault(Messages.SERVER_NO_PERM, "Você não possui permissão para isso$8.", null);
        this.addDefault(Messages.SERVER_NO_PLAYER, "Esse jogador não existe$8.", null);
        this.addDefault(Messages.SERVER_IN_TELEPORT, "Você já está em teleporte$8.", null);
        this.addDefault(Messages.SERVER_MODULE_ENABLED, "Modulo de $3{0}$7 ativado e carregado$8.", "0: modulo");
        this.addDefault(Messages.SERVER_MODULE_DISABLED, "Module de $3{0}$7 desativado$8.", "0: modulo");
        this.addDefault(Messages.SERVER_DATA_LOADED, "Carregando $3{0} $7foram carregadas $3{1}$7 dados$8.", "0: modulo; 1: quantia de dados");
        this.addDefault(Messages.SERVER_NETHER_TRAP_TIMING, "Trap de portal identificada$8, $7você irá ser teleportado em $3{0}$8.", "0: tempo");
        this.addDefault(Messages.SERVER_REMOVED_ENTITIES, "Foram removidas $3{0}$7 entidades$8.", "0: quantia de entidades");
        this.addDefault(Messages.SERVER_LOGIN, "$3{1} $8[$a+$8]", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(Messages.SERVER_LOGOUT, "$3{1} $8[$c-$8]", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(Messages.SERVER_FIRST_LOGIN, "$3{1} $7acabou de chegar no servidor$8, $7OLÁ$8!", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(Messages.SERVER_TIMING, "Você não pode usar isso pelos próximos $3{0} $7segundos$8.", "0: tempo restante");
        this.addDefault(Messages.SERVER_TPS, "TPS$8:$3 {0}$8.", "0: tps");
        this.addDefault(Messages.SERVER_MOTD_1, "$l#333832◢◤◢◤◢◤ $l#a7d95b✙ $l#69de3bE$l#46e33dt$l#3ee66ee$l#46e89fr$l#50ebc4n$l#5bebedi$l#61b4f0a$l#677ff2S$l#7469f5e$l#825ff7r$l#ab5cfav$l#c962fce$l#f36affr $l#ff5cc9S$l#ff579a$l#ff4b6fu$l#ff454er$l#ff4b41v$l#ff583bi$l#ff8250v$l#ffa959a$l#ffcc4al $l#ffe77c✙ $l#333832◥◣◥◣◥◣", null);
        this.addDefault(Messages.SERVER_MOTD_2, "$r#71b32aVanilla #b5ac2ecom #b8813eextras#74bdbb!!!", null);
        this.addDefault(Messages.SERVER_CONVERTING_DATABASE, "Convertendo DataBase antiga$8, $7status$8: $3{0}%$8.", "0: porcentagem de chance");
        this.addDefault(Messages.ECO_PAY, "Você pagou $3{0} $7para $3{2}$8.", "0: quantia de money; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(Messages.ECO_PAY_RECEIVED, "Você recebeu $3{0} $7de $3{2}$8.", "0: quantia de money; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(Messages.ECO_NO_MONEY, "Você não possui todo esse dinheiro$8.", null);
        this.addDefault(Messages.ECO_AUTO_PAY, "Você não pode pagar a si mesmo$8.", null);
        this.addDefault(Messages.ECO_SET_FROM, "Você definiu para $3{0} $7o saldo de $3{2}$8.", "0: quantia de money; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(Messages.ECO_SETED, "O seu saldo foi definido para $3{0} $7por $3{2}$8.", "0: quantia de money; 1: nome de quem alterou; 2: apelido de quem alterou");
        this.addDefault(Messages.ECO_REMOVE_FROM, "Você removeu $3{0} $7do saldo de $3{2}$8.", "0: quantia de money; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(Messages.ECO_REMOVED, "Foi retirado $3{0} $7do seu saldo por $3{2}$8.", "0: quantia de money; 1: nome de quem removeu; 2: apelido de quem alterou");
        this.addDefault(Messages.ECO_GIVE_FROM, "Você deu $3{0} $7de saldo de $3{2}$8.", "0: quantia de money; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(Messages.ECO_GIVED, "Foi recebeu $3{0} $7de saldo por $3{2}$8.", "0: quantia de money; 1: nome de quem deu; 2: apelido de quem alterou");
        this.addDefault(Messages.ECO_BALANCE, "Você possui $3{0}$8.", "0: quantia de money");
        this.addDefault(Messages.ECO_BALANCE_OTHER,  "O $3{2} $7possui $3{0}$8.", "0: quantia de money; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(Messages.ECO_BALTOP_TITLE, "Top money$8, $7página $3{0}$8:", "0: número da página");
        this.addDefault(Messages.ECO_BALTOP_LIST, "$3{1} $8- $7Saldo$8: $3{2}", "0: nome do jogador; 1: apelido do jogador; 2: saldo do jogador");
        this.addDefault(Messages.ECO_BALTOP_PAGE, "$8---", "Left: Página anterior; Right: Próxima página");
        this.addDefault(Messages.ECO_BALTOP_PAGE_LIMIT, "$7Você passou do limite de páginas$8.", null);
        this.addDefault(Messages.ECO_XLSX_NOT_FOUND, "$7Arquivo csv não encontrado$8.", null);
        this.addDefault(Messages.CASH_BALANCE, "Você possui $3{0} $7de cash$8.", "0: quantia de cash");
        this.addDefault(Messages.CASH_BALANCE_OTHER, "$3{2} $7possui $3{0} $7de cash$8.", "0: quantia de cash; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(Messages.CASH_NOTHING_TO_BUY, "Você não possui nada para comprar$8.", null);
        this.addDefault(Messages.CASH_BOUGHT, "Compra confirmada com sucesso$8.", null);
        this.addDefault(Messages.CASH_CANCELED, "Compra cancelada com sucesso$8.", null);
        this.addDefault(Messages.CASH_RECEVEID, "Você recebeu $3{0}$7 de cash por $3{2}$8.", "0: quantia de cash; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(Messages.CASH_SENT, "Você enviou $3{0}$7 de cash para $3{2}$8.", "0: quantia de cash; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(Messages.CASH_LOST, "Você perdeu $3{0}$7 de cash por $3{2}$8.", "0: quantia de cash; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(Messages.CASH_REMOVED, "Você removeu $3{0}$7 de cash de $3{2}$8.", "0: quantia de cash; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(Messages.CASH_COST, "Isso irá custar $3{0}$7 de cash$8.", "0: quantia de cash");
        this.addDefault(Messages.CASH_CHOOSE, "Use $6/cash accept $7ou $6/cash deny$7 para aceitar ou negar a compra$8.", null);
        this.addDefault(Messages.CASH_NO_HAS, "Você não possui $3{0}$7 de cash$8.", "0: quantia de cash");
        this.addDefault(Messages.CASH_ALREADY_BUYING, "Você já possui uma compra em andamento$8.", null);
        this.addDefault(Messages.AFK_BROADCAST_KICK, "$3{1} $7estava AFK e foi kickado$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(Messages.AFK_KICKED, "Você foi kickado por estar AFK$8.", null);
        this.addDefault(Messages.AFK_AUTO_ENTER, "$3{1}$7 ficou ausente agora está AFK$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(Messages.AFK_LEAVE, "$3{1} $7saiu do modo AFK$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(Messages.AFK_ENTER, "$3{1} $7entrou no modo AFK$8.", null);
        this.addDefault(Messages.GLOW_ENABLED, "Glow ativado$8.", null);
        this.addDefault(Messages.GLOW_DISABLED, "Glow desativado$8.", null);
        this.addDefault(Messages.GLOW_COLOR_CHANGED, "Cor do glow alterada para $3{0}$8.", "0: cor do glow");
        this.addDefault(Messages.EXP_SET_FROM, "Você definiu para $3{0} $7o saldo de exp de $3{2}$8.", "0: quantia de exp; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(Messages.EXP_SETED, "O seu saldo de exp foi definido para $3{0} $7por $3{2}$8.", "0: quantia de exp; 1: nome de quem alterou; 2: apelido de quem alterou");
        this.addDefault(Messages.EXP_REMOVE_FROM, "Você removeu $3{0} $7do saldo de exp de $3{2}$8.", "0: quantia de exp; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(Messages.EXP_REMOVED, "Foi retirado $3{0} $7do seu saldo de exp por $3{2}$8.", "0: quantia de exp; 1: nome de quem removeu; 2: apelido de quem alterou");
        this.addDefault(Messages.EXP_GIVE_FROM, "Você deu $3{0} $7de saldo de exp de $3{2}$8.", "0: quantia de exp; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(Messages.EXP_GIVED, "Foi recebeu $3{0} $7de saldo de exp por $3{2}$8.", "0: quantia de exp; 1: nome de quem deu; 2: apelido de quem alterou");
        this.addDefault(Messages.EXP_BALANCE, "Você possui $3{0}$7 de exp$8.", "0: quantia de exp");
        this.addDefault(Messages.EXP_BOTTLED, "Tome sua garrafinha$8.", null);
        this.addDefault(Messages.EXP_INSUFFICIENT, "Você não possui tudo isso de exp$8.", null);
        this.addDefault(Messages.EXP_WITHDRAW, "Você sacou $3{0}$7 níveis$8.", "0: quantia de nível");
        this.addDefault(Messages.EXP_DEPOSIT, "Você depositou $3{0}$7 níveis$8.", "0: quantia de nível");
        this.addDefault(Messages.GAMEMODE_SETED, "Seu modo de jogo foi definido para {0}$8.", "0: modo de jogo");
        this.addDefault(Messages.GAMEMODE_SET_FROM, "O modo de jogo de $3{2}$7 foi definido para {0}$8.", "0: modo de jogo; 1: nome do jogador; 2: apelido do jogador");
        this.addDefault(Messages.WARP_TELEPORTED, "Você foi teleportado até o $3{0}$8.", "0: nome da warp");
        this.addDefault(Messages.WARP_CREATED, "A warp $3{0}$7 foi criada com sucesso$8.", "0: nome da warp");
        this.addDefault(Messages.WARP_DELETED, "A warp $3{0}$7 foi deletada$8.", "0: nome da warp");
        this.addDefault(Messages.WARP_NOT_FOUND, "A warp $3{0}$7 não existe$8.", "0: nome da warp");
        this.addDefault(Messages.WARP_LIST, "Warps$8: $3{0}$8.", "0: lista de warps");
        this.addDefault(Messages.WARP_SPAWN_TELEPORTED, "Você foi teleportado até o $3Spawn$8.", null);
        this.addDefault(Messages.WARP_SPAWN_TELEPORT_TARGET, "Você teleportou $3{1}$7 até o $3Spawn$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(Messages.WARP_SPAWN_TELEPORTED_BY, "Você foi teleportado até o $3Spawn$7 por $3{1}$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(Messages.WARP_SPAWN_CREATED, "$3Spawn $7definido com sucesso$8.", null);
        this.addDefault(Messages.WARP_SPAWN_NOT_FOUND, "O $3Spawn $7não foi definido ainda$8.", null);
        this.addDefault(Messages.WARP_SHOP_CREATED, "$3Loja $7definida com sucesso$8.", null);
        this.addDefault(Messages.WARP_SHOP_DELETED, "$3Loja $7deletada com sucesso$8.", null);
        this.addDefault(Messages.WARP_SHOP_TELEPORTED, "Você foi teleportado até a $3Loja$8.", null);
        this.addDefault(Messages.WARP_SHOP_PLAYER_TELEPORTED, "Você foi teleportado até a loja de$3{0}$8.", "0: nome da loja");
        this.addDefault(Messages.WARP_SHOP_NOT_FOUND, "$3{1}$7 não possui loja$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(Messages.WARP_SHOP_CENTRAL_NOT_FOUND, "$3Loja $7não encontrada$8.", null);
        this.addDefault(Messages.TELEPORT_ALL_PLAYERS, "$3{1}$7 teleportou todos jogadores até si$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(Messages.TELEPORT_NOT_REQUESTED, "Nenhum pedido de teleporte solicitado$8.", null);
        this.addDefault(Messages.TELEPORT_TARGET_ACCEPT, "$3{1}$7 aceitou o seu pedido de tpa$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(Messages.TELEPORT_GOING_TO_PLAYER, "Você foi teleportado até $3{1}$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(Messages.TELEPORT_ACCEPT, "Você aceitou o pedido de tpa de $3{1}$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(Messages.TELEPORT_TARGET_OFFLINE, "O usuário que fez o pedido de tpa estáo offline$8.", null);
        this.addDefault(Messages.TELEPORT_TARGET_DENIED, "$3{1}$7 negou o seu pedido de tpa$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(Messages.TELEPORT_DENIED, "Você negou o pedido de tpa de $3{1}$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(Messages.TELEPORT_CANT_YOURSELF, "Você não pode enviar um pedido para si mesmo$8.", null);
        this.addDefault(Messages.TELEPORT_ALREADY_REQUESTED, "$3{1}$7 já possui um pedido de tpa$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(Messages.TELEPORT_SENT, "Você enviou um pedido de tpa para $3{1}$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(Messages.TELEPORT_RECEIVED, "Você recebeu um pedido de tpa de $3{1}$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(Messages.TELEPORT_MOVED, "Você se moveu e por isso o teleporte foi cancelado$8.", null);
        this.addDefault(Messages.TELEPORT_TIMING, "Você irá ser teleportado em $3{0}$7 segundos$8.", "0: tempo para ser teleportado");
        this.addDefault(Messages.TELEPORT_BACK_NOT_TP, "Você ainda não se teleportou para usar esse comando$8.", null);
        this.addDefault(Messages.TELEPORT_BACK_WITH_COST, "Você foi teleportado até o seu local anterior por $3{0}$8.", "0: custo do back");
        this.addDefault(Messages.TELEPORT_BACK_WITHOUT_COST, "Você foi teleportado até o seu local anterior de graça$8.", null);
        this.addDefault(Messages.TELEPORT_BACK_WORLD_BLOCKED, "Você não pode utilizar o $6/back $7para esse mundo$8.", null);
        this.addDefault(Messages.REWARD_INVALID_KEY, "$3{0}$7 não é uma chave válida$8.", "0: chave");
        this.addDefault(Messages.REWARD_CREATED, "Reward criado com sucesso chave $3{0}$8.", "0: chave");
        this.addDefault(Messages.REWARD_NOT_FOUND, "Não foi encontrado nenhum reward com o nome de $3{0}$8.", "0: reward");
        this.addDefault(Messages.CHAT_NICK_CLEAR, "Seu apelido foi removido$8.", null);
        this.addDefault(Messages.CHAT_NICK_CHANGE, "Seu apelido foi alterado para $3{0}$8.", "0: novo apelido");
        this.addDefault(Messages.CHAT_NICK_CHANGE_TO, "Você mudou para $3{0}$7 o apelido de $3{2}$8.", "0: novo apelido; 1: nome de quem alterou; 2: apelido de quem alterou");
        this.addDefault(Messages.CHAT_BROADCAST, "$7{0}", "0: mensagem");
        this.addDefault(Messages.CHAT_SPY_ENABLED, "Chat spy ativado$8.", null);
        this.addDefault(Messages.CHAT_SPY_DISABLED, "Chat spy desativado$8.", null);
        this.addDefault(Messages.CHAT_NO_ONE_TO_RESP, "Você não possui ninguém para responder$8.", null);
        this.addDefault(Messages.CHAT_CHANNEL_CHANGED, "Você foi para o canal $3{0}$8.", "0: nome do canal");
        this.addDefault(Messages.CHAT_TELL_LOCKED, "Você travou a conversa privada com $3{1}$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(Messages.CHAT_TELL_UNLOCKED, "Você destravou a conversa privada com $3{1}$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(Messages.CHAT_ARE_MUTED, "Você está mutado pelos próximos $3{0}$8.", "0: tempo restante");
        this.addDefault(Messages.CHAT_NO_ONE_NEAR, "Não há ninguém para ti ouvir$8.", null);
        this.addDefault(Messages.CHAT_TELL_TO, "$8[$6P$8]$6 {2} -> {4} ➤ {0}", "0: mensagem; 1: nome do enviante; 2: apelido do enviante; 3: nome do recebinte; 4: apelido do recebinte");
        this.addDefault(Messages.CHAT_TELL_FROM, "$8[$6P$8]$6 {2} <- {4} ➤ {0}", "0: mensagem; 1: nome do recebinte; 2: apelido do recebinte; 3: nome do enviante; 4: apelido do enviante");
        this.addDefault(Messages.CHAT_CHANNELS_MUTED, "Todos os canais estão silenciados$8.", null);
        this.addDefault(Messages.CHAT_CHANNELS_ENABLED, "$3{1}$7 mutou todos os canais$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(Messages.CHAT_CHANNELS_DISABLED, "$3{1}$7 desmutou todos os canais$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(Messages.CHAT_BROADCAST_TEMP_MUTE, "$3{1}$7 foi mutado em $3{2}$7 por $3{4}$7 motivo$8:$3{5}", "0: nome do mutado; 1: apelido do mutado; 2: tempo mutado; 3: nome de quem mutou; 4: apelido de quem mutou; 5: motivo");
        this.addDefault(Messages.CHAT_BROADCAST_UNMUTE, "$3{1}$7 foi desmutado por $3{3}$8.", "0: nome do mutado; 1: apelido do mutado; 2: nome de quem mutou; 3: apelido de quem mutou");
        this.addDefault(Messages.CHAT_BROADCAST_MUTE, "$3{1}$7 foi mutado permanentemente por $3{3}$7 motivo$8:$3{4}", "0: nome do mutado; 1: apelido do mutado; 2: nome de quem mutou; 3: apelido de quem mutou; 4: motivo");
        this.addDefault(Messages.SPAWNER_INV_FULL, "O inventário do jogador está cheio$8.", null);
        this.addDefault(Messages.SPAWNER_RECEIVED, "Você recebeu $3{3}$7 spawner de $3{0}$7 por $3{2}$8.", "0: mob; 1: nome do jogador; 2: apelido do jogador; 3: quantia");
        this.addDefault(Messages.SPAWNER_SENT, "Você enviou $3{3} spawner de $3{0}$7 para $3{2}$8.", "0: mob; 1: nome do jogador; 2: apelido do jogador; 3: quantia");
        this.addDefault(Messages.SPAWNER_SEND_TYPES, "Os tipos de spawners válidos são$8: $3{0}$8.", "0: tipos");
        this.addDefault(Messages.SPAWNER_CANT_CHANGE_NAME, "Você não pode renomear spawners$8.", null);
        this.addDefault(Messages.SPAWNER_WORLD_BLOCKED, "Você não pode quebrar spawners nesse mundo$8.", null);
        this.addDefault(Messages.SPAWNER_DROP_FAILED, "Não foi dessa vez$8.", null);
        this.addDefault(Messages.SPAWNER_SILK_REQUESTED, "Você precisa de uma picareta com toque suave para isso$8.", null);
        this.addDefault(Messages.SPAWNER_WITHOUT_PERM, "Você precisa de permissão para isso$8.", null);
        this.addDefault(Messages.HOME_NOT_FOUND, "A home $3{0}$7 não existe$8.", "0: nome da home");
        this.addDefault(Messages.HOME_DELETED, "Você deletou a home $3{0}$8.", "0: nome da home");
        this.addDefault(Messages.HOME_LIMIT_REACHED, "Você alcançou o limite de homes$8, $7tome um bússola$8.", null);
        this.addDefault(Messages.HOME_CREATED, "Home definida com sucesso$8.", null);
        this.addDefault(Messages.HOME_STRING_LIMIT, "O nome $3{0}$7 pasou do limite de $310 $7caracteres permitidos$8.", "0: nome da home");
        this.addDefault(Messages.HOME_GOING, "Você foi até a sua home $3{0}$8.", "0: nome da home");
        this.addDefault(Messages.HOME_LIST, "Suas homes$8: $3{0}$8.", "0: lista de homes");
        this.addDefault(Messages.HOME_ITEM_NAME, "$8[$e{0}$8]", "0: nome da home");
        this.addDefault(Messages.HOME_NO_PERM_TO_COMPASS, "Você excedeu o limite de homes e não tem permissão para salvar em bussulas$8.", null);
        this.addDefault(Messages.ITEM_NOT_FOUND, "Nenhum item foi encontrado em sua mão$8.", null);
        this.addDefault(Messages.ITEM_NBT_ADDKEY, "Foi adicionado a chave $3{0}$7 com valor $3{1}$7 ao item$8.", "0: chave; 1: valor");
        this.addDefault(Messages.ITEM_NBT_DEFINE_COMMAND, "Você definiu o comando para o item executar$8, $7lembre de definir se deve ser executado pelo console ou não$8.", null);
        this.addDefault(Messages.ITEM_NBT_RUN_IN_CONSOLE, "Você se o comando deve ser executado no console como$8: $3{0}$8.", "0: valor");
        this.addDefault(Messages.ITEM_NBT_ADD_LINE, "Você adicionou mais um comando para ser executado no item$8.", null);
        this.addDefault(Messages.ITEM_NBT_SET_USAGES, "Você definiu a quantidade de usos do item como$8: $3{0}$8.", "0: quantidade de usos");
        this.addDefault(Messages.ITEM_NBT_GIVE, "Dado item especial para usuário$8.", null);
        this.addDefault(Messages.ITEM_NBT_CANT_GIVE, "Não foi possível dar esse item$8.", null);
        this.addDefault(Messages.ITEM_NBT_RECEIVED, "Aproveite seu item especial e não esqueça o que ele faz em$8.", null);
        this.addDefault(Messages.ITEM_CLEAR_LORE, "A lore do item foi removida$8.", null);
        this.addDefault(Messages.ITEM_CLEAR_NAME, "O nome do item foi removido$8.", null);
        this.addDefault(Messages.ITEM_ADD_LORE, "Foi adicionada a linha $3{0}$7 a lore$8.", "0: nova linha de lore");
        this.addDefault(Messages.ITEM_SET_LORE, "A lore foi limpa e definida como$3 {0}$8.", "0: lore");
        this.addDefault(Messages.ITEM_SET_NAME, "O nome do item foi definido como $3{0}$8.", "0: nome do item");
        this.addDefault(Messages.ITEM_HELMET, "Você equipou seu caçapete$8.", null);
        this.addDefault(Messages.ITEM_SET_CUSTOM, "Você atribuiu a CustomModelData $3{0} $7ao item$8.", "0: id da custom model data");
        this.addDefault(Messages.SPEED_SET, "Sua velocidade foi definida para $3{0}$8.", "0: nova velocidade");
        this.addDefault(Messages.SPEED_LIMIT, "Você precisa escolher um valor entre $31 $7e $310$8.", null);
        this.addDefault(Messages.PROFILE_TITLE, "$8[]====[$7Perfil$8]====[]", null);
        this.addDefault(Messages.PROFILE_REGISTER_DATA, "$7Registro$8: $3{0}", "0: data de registro");
        this.addDefault(Messages.PROFILE_LAST_LOGIN, "$7Ultimo login$8: $3{0}", "0: ultimo login");
        this.addDefault(Messages.PROFILE_ACCOUNT_HOURS, "$7Horas de jogo$8: $3{0}", "0: horas registradas");
        this.addDefault(Messages.GODMODE_ENABLED, "Você ativou o god mode$8.", null);
        this.addDefault(Messages.GODMODE_DISABLED, "Você desativou o god mode$8.", null);
        this.addDefault(Messages.SUICIDE_BROADCAST, "$3{1} disse$8: $3{2}$7 e logo após se matou$8.", "0: nome do jogador; 1: apelido do jogador; 2: mensagem");
        this.addDefault(Messages.LIGHTNING_CURSOR, "THOR$8! (pausa dramática) $7FILHO DE ODIM$8!", null);
        this.addDefault(Messages.LIGHTNING_TARGET, "Você castigou $3{1}$7 com as forças divinas$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(Messages.LIGHTNING_RECEIVED, "Você foi punido por $3{1}$7 os deuses não estão ao seu favor$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(Messages.ITEM_CONDENSER, "Compactando blocos$8.", null);
        this.addDefault(Messages.FEED_YOURSELF, "Você se saciou$8.", null);
        this.addDefault(Messages.FEED_TARGET, "Você saciou o $3{1}$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(Messages.FEED_RECEIVED, "Você foi saciado por $3{1}$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(Messages.STATS_MEM, "Memória$8: $3{0}MB$8/$3{1}MB$8.", "0: memória usada; 1: memória total");
        this.addDefault(Messages.STATS_HOURS, "Tempo online$8: $7Dias$8: $3{0} $7horas$8: $3{1} $7minutos$8:{2} $7segundos$8:$3{3}$8.", "0: dias; 1: horas; 2: minutos; 3: segundos");
        this.addDefault(Messages.FLY_ENABLED, "Modo voar ativado$8.", null);
        this.addDefault(Messages.FLY_DISABLED, "Modo voar desativado$8.", null);
        this.addDefault(Messages.FLY_ENABLED_FROM, "Modo voar ativado para $3{1}$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(Messages.FLY_ENABLED_BY, "Modo voar ativado por $3{1}$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(Messages.FLY_DISABLED_FROM, "Modo voar desativado para $3{1}$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(Messages.FLY_DISABLED_BY, "Modo voar desativado por $3{1}$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(Messages.FLY_ARE_PVP, "Você está em combate$8, $7aguarde $3{0}$7 fora de combate para poder voar$8.", "0: tempo em combate");
        this.addDefault(Messages.FLY_TARGET_ARE_PVP, "O alvo está em combate pelos próximos $3{0} $7segundos$8, $7logo não pode voar$8.", "0: tempo em combate");
        this.addDefault(Messages.FLY_ENTER_PVP, "Você entrou em combate e seu voar foi desativado$8.", null);
        this.addDefault(Messages.KIT_NOT_FOUND, "Nenhum kit com o nome de $3{0}$7 foi encontrado$8.", "0: nome do kit");
        this.addDefault(Messages.KIT_LIST, "Kits$8: $3{0}$8.", "0: lista de kits");
        this.addDefault(Messages.NIGHT_PLAYER_SLEEPING, "$3{1}$7 está dormindo$8, $7durma também para passar a noite mais rápido$8.", "0: nome do jogador; 1: apelido do jogador");
        this.addDefault(Messages.NIGHT_SKIPPING, "A noite está passando em $3{0}$8.", "0: nome do mundo");
        this.addDefault(Messages.NIGHT_SKIPPED, "A noite passou em $3{0}$8.", "0: nome do mundo");
        this.addDefault(Messages.SETTINGS_CHANGE, "Alterado estado de $3{0}$8.", "0: boolean");
        this.addDefault(Messages.SETTINGS_WRONG, "Nenhuma configuração com esse nome$8.", null);
        this.addDefault(Messages.RELOAD_ALL, "Reinicia todos os módulos do EterniaServer plugin$8.", null);
        this.addDefault(Messages.RELOAD_MODULE, "Reinicia o módulo $3{0}$7 do EterniaServer plugin$8.", "0: nome do módulo");
        this.addDefault(Messages.COMMAND_DENIED, "Comando cancelado$8.", null);
        this.addDefault(Messages.COMMAND_NOT, "Nenhum comando para ser aceito$8.", null);
        this.addDefault(Messages.COMMAND_COST, "Esse comando custa $3{0}$7 para aceitar use $6/command accept $7para negar use $6/command deny$8.", "0: custo do comando");
    }

    private void addDefault(Messages id, String text, String notes) {
        final CustomizableMessage message = new CustomizableMessage(id, text, notes);
        defaults.put(id.name(), message);
    }

    private String getPath(Messages messagesEnum) {
        switch (messagesEnum.name().split("_")[0].hashCode()) {
            case -1852497085:
                return "server.";
            case -577575125:
                return "teleport.";
            case 64710:
                return "afk.";
            case 68465:
                return "eco.";
            case 69117:
                return "exp.";
            case 2061107:
                return "cash.";
            case 2067288:
                return "chat.";
            case 2223327:
                return "home.";
            case 2257683:
                return "item.";
            case 2656904:
                return "warp.";
            default:
                return "generic.";
        }
    }

    @Override
    public ConfigurationCategory category() {
        return ConfigurationCategory.GENERIC;
    }

    @Override
    public void executeConfig() {
        final FileConfiguration config = YamlConfiguration.loadConfiguration(new File(Constants.MESSAGES_FILE_PATH));

        for (Messages messagesEnum : Messages.values()) {
            CustomizableMessage messageData = defaults.get(messagesEnum.name());
            final String path = getPath(messagesEnum);
            if (messageData == null) {
                messageData = new CustomizableMessage(messagesEnum, "Mensagem faltando para $3" + messagesEnum.name() + "$8.", null);
                plugin.logError("Entrada para a mensagem " + messagesEnum.name(), 2);
            }

            messages[messagesEnum.ordinal()] = config.getString(path + messagesEnum.name() + ".text", messageData.text);
            config.set(path + messagesEnum.name() + ".text", messages[messagesEnum.ordinal()]);
            messages[messagesEnum.ordinal()] = messages[messagesEnum.ordinal()].replace('$', (char) 0x00A7);
            if (messageData.getNotes() != null) {
                messageData.setNotes(config.getString(path + messagesEnum.name() + ".notes", messageData.getNotes()));
                config.set(path + messagesEnum.name() + ".notes", messageData.getNotes());
            }
        }

        if (new File(Constants.DATA_LOCALE_FOLDER_PATH).mkdir()) {
            plugin.logError("Pasta de locales criada com sucesso", 1);
        }

        try {
            config.save(Constants.MESSAGES_FILE_PATH);
        } catch (IOException exception) {
            plugin.logError("Impossível de criar arquivos em " + Constants.DATA_LOCALE_FOLDER_PATH, 3);
        }
    }

    @Override
    public void executeCritical() {

    }
}
