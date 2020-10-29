package br.com.eterniaserver.eterniaserver;

import br.com.eterniaserver.acf.ConditionFailedException;
import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.commands.*;
import br.com.eterniaserver.eterniaserver.core.PluginClearSchedule;
import br.com.eterniaserver.eterniaserver.core.PluginTick;
import br.com.eterniaserver.eterniaserver.core.PluginSchedule;
import br.com.eterniaserver.eterniaserver.core.Vars;
import br.com.eterniaserver.eterniaserver.core.CheckWorld;
import br.com.eterniaserver.eterniaserver.objects.CustomCommand;
import br.com.eterniaserver.eterniaserver.enums.Colors;
import br.com.eterniaserver.eterniaserver.enums.Commands;
import br.com.eterniaserver.eterniaserver.enums.Messages;

import org.bukkit.Bukkit;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Managers {

    private final EterniaServer plugin;

    public Managers(EterniaServer plugin) {

        this.plugin = plugin;

        loadCommandsLocale();
        loadConditions();
        loadCompletions();
        loadGenericManager();
        loadBedManager();
        loadBlockRewardsManager();
        loadCashManager();
        loadCommandsManager();
        loadChatManager();
        loadEconomyManager();
        loadElevatorManager();
        loadExperienceManager();
        loadHomesManager();
        loadPlayerChecks();
        loadClearManager();
        loadKitManager();
        loadRewardsManager();
        loadSpawnersManager();
        loadTeleportsManager();
        loadScheduleTasks();

    }

    private void loadCommandsLocale() {
        EterniaLib.getManager().getCommandReplacements().addReplacements(
                "cash", EterniaServer.cmdsLocale.getName(Commands.CASH),
                "cash_description", EterniaServer.cmdsLocale.getDescription(Commands.CASH),
                "cash_perm", EterniaServer.cmdsLocale.getPerm(Commands.CASH),
                "cash_help", EterniaServer.cmdsLocale.getName(Commands.CASH_HELP),
                "cash_help_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.CASH_HELP),
                "cash_help_description", EterniaServer.cmdsLocale.getDescription(Commands.CASH_HELP),
                "cash_help_perm", EterniaServer.cmdsLocale.getPerm(Commands.CASH_HELP),
                "cash_balance", EterniaServer.cmdsLocale.getName(Commands.CASH_BALANCE),
                "cash_balance_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.CASH_BALANCE),
                "cash_balance_description", EterniaServer.cmdsLocale.getDescription(Commands.CASH_BALANCE),
                "cash_balance_perm", EterniaServer.cmdsLocale.getPerm(Commands.CASH_BALANCE),
                "cash_accept", EterniaServer.cmdsLocale.getName(Commands.CASH_ACCEPT),
                "cash_accept_description", EterniaServer.cmdsLocale.getDescription(Commands.CASH_ACCEPT),
                "cash_accept_perm", EterniaServer.cmdsLocale.getPerm(Commands.CASH_ACCEPT),
                "cash_deny", EterniaServer.cmdsLocale.getName(Commands.CASH_DENY),
                "cash_deny_description", EterniaServer.cmdsLocale.getDescription(Commands.CASH_DENY),
                "cash_deny_perm", EterniaServer.cmdsLocale.getPerm(Commands.CASH_DENY),
                "cash_pay", EterniaServer.cmdsLocale.getName(Commands.CASH_PAY),
                "cash_pay_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.CASH_PAY),
                "cash_pay_description", EterniaServer.cmdsLocale.getDescription(Commands.CASH_PAY),
                "cash_pay_perm", EterniaServer.cmdsLocale.getPerm(Commands.CASH_PAY),
                "cash_remove", EterniaServer.cmdsLocale.getName(Commands.CASH_REMOVE),
                "cash_remove_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.CASH_REMOVE),
                "cash_remove_description", EterniaServer.cmdsLocale.getDescription(Commands.CASH_REMOVE),
                "cash_remove_perm", EterniaServer.cmdsLocale.getPerm(Commands.CASH_REMOVE),
                "cash_give", EterniaServer.cmdsLocale.getName(Commands.CASH_GIVE),
                "cash_give_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.CASH_GIVE),
                "cash_give_description", EterniaServer.cmdsLocale.getDescription(Commands.CASH_GIVE),
                "cash_give_perm", EterniaServer.cmdsLocale.getPerm(Commands.CASH_GIVE),
                "channel", EterniaServer.cmdsLocale.getName(Commands.CHANNEL),
                "channel_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.CHANNEL),
                "channel_description", EterniaServer.cmdsLocale.getDescription(Commands.CHANNEL),
                "channel_perm", EterniaServer.cmdsLocale.getPerm(Commands.CHANNEL),
                "channel_local", EterniaServer.cmdsLocale.getName(Commands.CHANNEL_LOCAL),
                "channel_local_aliases", EterniaServer.cmdsLocale.getAliases(Commands.CHANNEL_LOCAL),
                "channel_local_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.CHANNEL_LOCAL),
                "channel_local_description", EterniaServer.cmdsLocale.getDescription(Commands.CHANNEL_LOCAL),
                "channel_local_perm", EterniaServer.cmdsLocale.getPerm(Commands.CHANNEL_LOCAL),
                "channel_global", EterniaServer.cmdsLocale.getName(Commands.CHANNEL_GLOBAL),
                "channel_global_aliases", EterniaServer.cmdsLocale.getAliases(Commands.CHANNEL_GLOBAL),
                "channel_global_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.CHANNEL_GLOBAL),
                "channel_global_description", EterniaServer.cmdsLocale.getDescription(Commands.CHANNEL_GLOBAL),
                "channel_global_perm", EterniaServer.cmdsLocale.getPerm(Commands.CHANNEL_GLOBAL),
                "channel_staff", EterniaServer.cmdsLocale.getName(Commands.CHANNEL_STAFF),
                "channel_staff_aliases", EterniaServer.cmdsLocale.getAliases(Commands.CHANNEL_STAFF),
                "channel_staff_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.CHANNEL_STAFF),
                "channel_staff_description", EterniaServer.cmdsLocale.getDescription(Commands.CHANNEL_STAFF),
                "channel_staff_perm", EterniaServer.cmdsLocale.getPerm(Commands.CHANNEL_STAFF),
                "chat", EterniaServer.cmdsLocale.getName(Commands.CHAT),
                "chat_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.CHAT),
                "chat_description", EterniaServer.cmdsLocale.getDescription(Commands.CHAT),
                "chat_perm", EterniaServer.cmdsLocale.getPerm(Commands.CHAT),
                "chat_clear", EterniaServer.cmdsLocale.getName(Commands.CHAT_CLEAR),
                "chat_clear_aliases", EterniaServer.cmdsLocale.getAliases(Commands.CHAT_CLEAR),
                "chat_clear_description", EterniaServer.cmdsLocale.getDescription(Commands.CHAT_CLEAR),
                "chat_clear_perm", EterniaServer.cmdsLocale.getPerm(Commands.CHAT_CLEAR),
                "chat_broadcast", EterniaServer.cmdsLocale.getName(Commands.CHAT_BROADCAST),
                "chat_broadcast_aliases", EterniaServer.cmdsLocale.getAliases(Commands.CHAT_BROADCAST),
                "chat_broadcast_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.CHAT_BROADCAST),
                "chat_broadcast_description", EterniaServer.cmdsLocale.getDescription(Commands.CHAT_BROADCAST),
                "chat_broadcast_perm", EterniaServer.cmdsLocale.getPerm(Commands.CHAT_BROADCAST),
                "chat_vanish", EterniaServer.cmdsLocale.getName(Commands.CHAT_VANISH),
                "chat_vanish_aliases", EterniaServer.cmdsLocale.getAliases(Commands.CHAT_VANISH),
                "chat_vanish_description", EterniaServer.cmdsLocale.getDescription(Commands.CHAT_VANISH),
                "chat_vanish_perm", EterniaServer.cmdsLocale.getPerm(Commands.CHAT_VANISH),
                "chat_ignore", EterniaServer.cmdsLocale.getName(Commands.CHAT_IGNORE),
                "chat_ignore_aliases", EterniaServer.cmdsLocale.getAliases(Commands.CHAT_IGNORE),
                "chat_ignore_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.CHAT_IGNORE),
                "chat_ignore_description", EterniaServer.cmdsLocale.getDescription(Commands.CHAT_IGNORE),
                "chat_ignore_perm", EterniaServer.cmdsLocale.getPerm(Commands.CHAT_IGNORE),
                "chat_spy", EterniaServer.cmdsLocale.getName(Commands.CHAT_SPY),
                "chat_spy_aliases", EterniaServer.cmdsLocale.getAliases(Commands.CHAT_SPY),
                "chat_spy_description", EterniaServer.cmdsLocale.getDescription(Commands.CHAT_SPY),
                "chat_spy_perm", EterniaServer.cmdsLocale.getPerm(Commands.CHAT_SPY),
                "chat_reply", EterniaServer.cmdsLocale.getName(Commands.CHAT_REPLY),
                "chat_reply_aliases", EterniaServer.cmdsLocale.getAliases(Commands.CHAT_REPLY),
                "chat_reply_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.CHAT_REPLY),
                "chat_reply_description", EterniaServer.cmdsLocale.getDescription(Commands.CHAT_REPLY),
                "chat_reply_perm", EterniaServer.cmdsLocale.getPerm(Commands.CHAT_REPLY),
                "chat_tell", EterniaServer.cmdsLocale.getName(Commands.CHAT_TELL),
                "chat_tell_aliases", EterniaServer.cmdsLocale.getAliases(Commands.CHAT_TELL),
                "chat_tell_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.CHAT_TELL),
                "chat_tell_description", EterniaServer.cmdsLocale.getDescription(Commands.CHAT_TELL),
                "chat_tell_perm", EterniaServer.cmdsLocale.getPerm(Commands.CHAT_TELL),
                "eco", EterniaServer.cmdsLocale.getName(Commands.ECO),
                "eco_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.ECO),
                "eco_description", EterniaServer.cmdsLocale.getDescription(Commands.ECO),
                "eco_perm", EterniaServer.cmdsLocale.getPerm(Commands.ECO),
                "eco_set", EterniaServer.cmdsLocale.getName(Commands.ECO_SET),
                "eco_set_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.ECO_SET),
                "eco_set_description", EterniaServer.cmdsLocale.getDescription(Commands.ECO_SET),
                "eco_set_perm", EterniaServer.cmdsLocale.getPerm(Commands.ECO_SET),
                "eco_take", EterniaServer.cmdsLocale.getName(Commands.ECO_TAKE),
                "eco_take_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.ECO_TAKE),
                "eco_take_description", EterniaServer.cmdsLocale.getDescription(Commands.ECO_TAKE),
                "eco_take_perm", EterniaServer.cmdsLocale.getPerm(Commands.ECO_TAKE),
                "eco_give", EterniaServer.cmdsLocale.getName(Commands.ECO_GIVE),
                "eco_give_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.ECO_GIVE),
                "eco_give_description", EterniaServer.cmdsLocale.getDescription(Commands.ECO_GIVE),
                "eco_give_perm", EterniaServer.cmdsLocale.getPerm(Commands.ECO_GIVE),
                "eco_money", EterniaServer.cmdsLocale.getName(Commands.ECO_MONEY),
                "eco_money_aliases", EterniaServer.cmdsLocale.getAliases(Commands.ECO_MONEY),
                "eco_money_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.ECO_MONEY),
                "eco_money_description", EterniaServer.cmdsLocale.getDescription(Commands.ECO_MONEY),
                "eco_money_perm", EterniaServer.cmdsLocale.getPerm(Commands.ECO_MONEY),
                "eco_pay", EterniaServer.cmdsLocale.getName(Commands.ECO_PAY),
                "eco_pay_aliases", EterniaServer.cmdsLocale.getAliases(Commands.ECO_PAY),
                "eco_pay_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.ECO_PAY),
                "eco_pay_description", EterniaServer.cmdsLocale.getDescription(Commands.ECO_PAY),
                "eco_pay_perm", EterniaServer.cmdsLocale.getPerm(Commands.ECO_PAY),
                "eco_baltop", EterniaServer.cmdsLocale.getName(Commands.ECO_BALTOP),
                "eco_baltop_aliases", EterniaServer.cmdsLocale.getAliases(Commands.ECO_BALTOP),
                "eco_baltop_description", EterniaServer.cmdsLocale.getDescription(Commands.ECO_BALTOP),
                "eco_baltop_perm", EterniaServer.cmdsLocale.getPerm(Commands.ECO_BALTOP),
                "xp", EterniaServer.cmdsLocale.getName(Commands.XP),
                "xp_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.XP),
                "xp_description", EterniaServer.cmdsLocale.getDescription(Commands.XP),
                "xp_perm", EterniaServer.cmdsLocale.getPerm(Commands.XP),
                "xp_set", EterniaServer.cmdsLocale.getName(Commands.XP_SET),
                "xp_set_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.XP_SET),
                "xp_set_description", EterniaServer.cmdsLocale.getDescription(Commands.XP_SET),
                "xp_set_perm", EterniaServer.cmdsLocale.getPerm(Commands.XP_SET),
                "xp_take", EterniaServer.cmdsLocale.getName(Commands.XP_TAKE),
                "xp_take_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.XP_TAKE),
                "xp_take_description", EterniaServer.cmdsLocale.getDescription(Commands.XP_TAKE),
                "xp_take_perm", EterniaServer.cmdsLocale.getPerm(Commands.XP_TAKE),
                "xp_give", EterniaServer.cmdsLocale.getName(Commands.XP_GIVE),
                "xp_give_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.XP_GIVE),
                "xp_give_description", EterniaServer.cmdsLocale.getDescription(Commands.XP_GIVE),
                "xp_give_perm", EterniaServer.cmdsLocale.getPerm(Commands.XP_GIVE),
                "xp_check", EterniaServer.cmdsLocale.getName(Commands.XP_CHECK),
                "xp_check_description", EterniaServer.cmdsLocale.getDescription(Commands.XP_CHECK),
                "xp_check_perm", EterniaServer.cmdsLocale.getPerm(Commands.XP_CHECK),
                "xp_bottle", EterniaServer.cmdsLocale.getName(Commands.XP_BOTTLE),
                "xp_bottle_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.XP_BOTTLE),
                "xp_bottle_description", EterniaServer.cmdsLocale.getDescription(Commands.XP_BOTTLE),
                "xp_bottle_perm", EterniaServer.cmdsLocale.getPerm(Commands.XP_BOTTLE),
                "xp_withdraw", EterniaServer.cmdsLocale.getName(Commands.XP_WITHDRAW),
                "xp_withdraw_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.XP_WITHDRAW),
                "xp_withdraw_description", EterniaServer.cmdsLocale.getDescription(Commands.XP_WITHDRAW),
                "xp_withdraw_perm", EterniaServer.cmdsLocale.getPerm(Commands.XP_WITHDRAW),
                "xp_deposit", EterniaServer.cmdsLocale.getName(Commands.XP_DEPOSIT),
                "xp_deposit_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.XP_DEPOSIT),
                "xp_deposit_description", EterniaServer.cmdsLocale.getDescription(Commands.XP_DEPOSIT),
                "xp_deposit_perm", EterniaServer.cmdsLocale.getPerm(Commands.XP_DEPOSIT),
                "gamemode", EterniaServer.cmdsLocale.getName(Commands.GAMEMODE),
                "gamemode_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.GAMEMODE),
                "gamemode_description", EterniaServer.cmdsLocale.getDescription(Commands.GAMEMODE),
                "gamemode_perm", EterniaServer.cmdsLocale.getPerm(Commands.GAMEMODE),
                "gamemode_survival", EterniaServer.cmdsLocale.getName(Commands.GAMEMODE_SURVIVAL),
                "gamemode_survival_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.GAMEMODE_SURVIVAL),
                "gamemode_survival_description", EterniaServer.cmdsLocale.getDescription(Commands.GAMEMODE_SURVIVAL),
                "gamemode_survival_perm", EterniaServer.cmdsLocale.getPerm(Commands.GAMEMODE_SURVIVAL),
                "gamemode_creative", EterniaServer.cmdsLocale.getName(Commands.GAMEMODE_CREATIVE),
                "gamemode_creative_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.GAMEMODE_CREATIVE),
                "gamemode_creative_description", EterniaServer.cmdsLocale.getDescription(Commands.GAMEMODE_CREATIVE),
                "gamemode_creative_perm", EterniaServer.cmdsLocale.getPerm(Commands.GAMEMODE_CREATIVE),
                "gamemode_adventure", EterniaServer.cmdsLocale.getName(Commands.GAMEMODE_ADVENTURE),
                "gamemode_adventure_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.GAMEMODE_ADVENTURE),
                "gamemode_adventure_description", EterniaServer.cmdsLocale.getDescription(Commands.GAMEMODE_ADVENTURE),
                "gamemode_adventure_perm", EterniaServer.cmdsLocale.getPerm(Commands.GAMEMODE_ADVENTURE),
                "gamemode_spectator", EterniaServer.cmdsLocale.getName(Commands.GAMEMODE_SPECTATOR),
                "gamemode_spectator_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.GAMEMODE_SPECTATOR),
                "gamemode_spectator_description", EterniaServer.cmdsLocale.getDescription(Commands.GAMEMODE_SPECTATOR),
                "gamemode_spectator_perm", EterniaServer.cmdsLocale.getPerm(Commands.GAMEMODE_SPECTATOR),
                "speed", EterniaServer.cmdsLocale.getName(Commands.SPEED),
                "speed_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.SPEED),
                "speed_description", EterniaServer.cmdsLocale.getDescription(Commands.SPEED),
                "speed_perm", EterniaServer.cmdsLocale.getPerm(Commands.SPEED),
                "god", EterniaServer.cmdsLocale.getName(Commands.GOD),
                "god_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.GOD),
                "god_description", EterniaServer.cmdsLocale.getDescription(Commands.GOD),
                "god_perm", EterniaServer.cmdsLocale.getPerm(Commands.GOD),
                "profile", EterniaServer.cmdsLocale.getName(Commands.PROFILE),
                "profile_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.PROFILE),
                "profile_description", EterniaServer.cmdsLocale.getDescription(Commands.PROFILE),
                "profile_perm", EterniaServer.cmdsLocale.getPerm(Commands.PROFILE),
                "mem", EterniaServer.cmdsLocale.getName(Commands.MEM),
                "mem_description", EterniaServer.cmdsLocale.getDescription(Commands.MEM),
                "mem_perm", EterniaServer.cmdsLocale.getPerm(Commands.MEM),
                "mem_all", EterniaServer.cmdsLocale.getName(Commands.MEM_ALL),
                "mem_all_description", EterniaServer.cmdsLocale.getDescription(Commands.MEM_ALL),
                "mem_all_perm", EterniaServer.cmdsLocale.getPerm(Commands.MEM_ALL),
                "fly", EterniaServer.cmdsLocale.getName(Commands.FLY),
                "fly_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.FLY),
                "fly_description", EterniaServer.cmdsLocale.getDescription(Commands.FLY),
                "fly_perm", EterniaServer.cmdsLocale.getPerm(Commands.FLY),
                "fly_debug", EterniaServer.cmdsLocale.getName(Commands.FLY_DEBUG),
                "fly_debug_description", EterniaServer.cmdsLocale.getDescription(Commands.FLY_DEBUG),
                "fly_debug_perm", EterniaServer.cmdsLocale.getPerm(Commands.FLY_DEBUG),
                "feed", EterniaServer.cmdsLocale.getName(Commands.FEED),
                "feed_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.FEED),
                "feed_description", EterniaServer.cmdsLocale.getDescription(Commands.FEED),
                "feed_perm", EterniaServer.cmdsLocale.getPerm(Commands.FEED),
                "condenser", EterniaServer.cmdsLocale.getName(Commands.CONDENSER),
                "condenser_description", EterniaServer.cmdsLocale.getDescription(Commands.CONDENSER),
                "condenser_perm", EterniaServer.cmdsLocale.getPerm(Commands.CONDENSER),
                "thor", EterniaServer.cmdsLocale.getName(Commands.THOR),
                "thor_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.THOR),
                "thor_description", EterniaServer.cmdsLocale.getDescription(Commands.THOR),
                "thor_perm", EterniaServer.cmdsLocale.getPerm(Commands.THOR),
                "suicide", EterniaServer.cmdsLocale.getName(Commands.SUICIDE),
                "suicide_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.SUICIDE),
                "suicide_description", EterniaServer.cmdsLocale.getDescription(Commands.SUICIDE),
                "suicide_perm", EterniaServer.cmdsLocale.getPerm(Commands.SUICIDE),
                "afk", EterniaServer.cmdsLocale.getName(Commands.AFK),
                "afk_description", EterniaServer.cmdsLocale.getDescription(Commands.AFK),
                "afk_perm", EterniaServer.cmdsLocale.getPerm(Commands.AFK),
                "glow", EterniaServer.cmdsLocale.getName(Commands.GLOW),
                "glow_description", EterniaServer.cmdsLocale.getDescription(Commands.GLOW),
                "glow_perm", EterniaServer.cmdsLocale.getPerm(Commands.GLOW),
                "glow_color", EterniaServer.cmdsLocale.getName(Commands.GLOW_COLOR),
                "glow_color_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.GLOW_COLOR),
                "glow_color_description", EterniaServer.cmdsLocale.getDescription(Commands.GLOW_COLOR),
                "glow_color_perm", EterniaServer.cmdsLocale.getPerm(Commands.GLOW_COLOR),
                "home", EterniaServer.cmdsLocale.getName(Commands.HOME),
                "home_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.HOME),
                "home_description", EterniaServer.cmdsLocale.getDescription(Commands.HOME),
                "home_perm", EterniaServer.cmdsLocale.getPerm(Commands.HOME),
                "delhome", EterniaServer.cmdsLocale.getName(Commands.HOME_DELETE),
                "delhome_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.HOME_DELETE),
                "delhome_description", EterniaServer.cmdsLocale.getDescription(Commands.HOME_DELETE),
                "delhome_perm", EterniaServer.cmdsLocale.getPerm(Commands.HOME_DELETE),
                "homes", EterniaServer.cmdsLocale.getName(Commands.HOME_LIST),
                "homes_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.HOME_LIST),
                "homes_description", EterniaServer.cmdsLocale.getDescription(Commands.HOME_LIST),
                "homes_perm", EterniaServer.cmdsLocale.getPerm(Commands.HOME_LIST),
                "sethome", EterniaServer.cmdsLocale.getName(Commands.HOME_CREATE),
                "sethome_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.HOME_CREATE),
                "sethome_description", EterniaServer.cmdsLocale.getDescription(Commands.HOME_CREATE),
                "sethome_perm", EterniaServer.cmdsLocale.getPerm(Commands.HOME_CREATE),
                "hat", EterniaServer.cmdsLocale.getName(Commands.HAT),
                "hat_description", EterniaServer.cmdsLocale.getDescription(Commands.HAT),
                "hat_perm", EterniaServer.cmdsLocale.getPerm(Commands.HAT),
                "workbench", EterniaServer.cmdsLocale.getName(Commands.WORKBENCH),
                "workbench_description", EterniaServer.cmdsLocale.getDescription(Commands.WORKBENCH),
                "workbench_perm", EterniaServer.cmdsLocale.getPerm(Commands.WORKBENCH),
                "openinv", EterniaServer.cmdsLocale.getName(Commands.OPENINV),
                "openinv_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.OPENINV),
                "openinv_description", EterniaServer.cmdsLocale.getDescription(Commands.OPENINV),
                "openinv_perm", EterniaServer.cmdsLocale.getPerm(Commands.OPENINV),
                "enderchest", EterniaServer.cmdsLocale.getName(Commands.ENDERCHEST),
                "enderchest_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.ENDERCHEST),
                "enderchest_description", EterniaServer.cmdsLocale.getDescription(Commands.ENDERCHEST),
                "enderchest_perm", EterniaServer.cmdsLocale.getPerm(Commands.ENDERCHEST),
                "item", EterniaServer.cmdsLocale.getName(Commands.ITEM),
                "item_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.ITEM),
                "item_description", EterniaServer.cmdsLocale.getDescription(Commands.ITEM),
                "item_perm", EterniaServer.cmdsLocale.getPerm(Commands.ITEM),
                "item_nbt", EterniaServer.cmdsLocale.getName(Commands.ITEM_NBT),
                "item_nbt_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.ITEM_NBT),
                "item_nbt_description", EterniaServer.cmdsLocale.getDescription(Commands.ITEM_NBT),
                "item_nbt_perm", EterniaServer.cmdsLocale.getPerm(Commands.ITEM_NBT),
                "item_nbt_addstring", EterniaServer.cmdsLocale.getName(Commands.ITEM_NBT_ADDSTRING),
                "item_nbt_addstring_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.ITEM_NBT_ADDSTRING),
                "item_nbt_addstring_description", EterniaServer.cmdsLocale.getDescription(Commands.ITEM_NBT_ADDSTRING),
                "item_nbt_addstring_perm", EterniaServer.cmdsLocale.getPerm(Commands.ITEM_NBT_ADDSTRING),
                "item_nbt_addint", EterniaServer.cmdsLocale.getName(Commands.ITEM_NBT_ADDINT),
                "item_nbt_addint_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.ITEM_NBT_ADDINT),
                "item_nbt_addint_description", EterniaServer.cmdsLocale.getDescription(Commands.ITEM_NBT_ADDINT),
                "item_nbt_addint_perm", EterniaServer.cmdsLocale.getPerm(Commands.ITEM_NBT_ADDINT),
                "item_clear", EterniaServer.cmdsLocale.getName(Commands.ITEM_CLEAR),
                "item_clear_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.ITEM_CLEAR),
                "item_clear_description", EterniaServer.cmdsLocale.getDescription(Commands.ITEM_CLEAR),
                "item_clear_perm", EterniaServer.cmdsLocale.getPerm(Commands.ITEM_CLEAR),
                "item_clear_lore", EterniaServer.cmdsLocale.getName(Commands.ITEM_CLEAR_LORE),
                "item_clear_lore_description", EterniaServer.cmdsLocale.getDescription(Commands.ITEM_CLEAR_LORE),
                "item_clear_lore_perm", EterniaServer.cmdsLocale.getPerm(Commands.ITEM_CLEAR_LORE),
                "item_clear_name", EterniaServer.cmdsLocale.getName(Commands.ITEM_CLEAR_NAME),
                "item_clear_name_description", EterniaServer.cmdsLocale.getDescription(Commands.ITEM_CLEAR_NAME),
                "item_clear_name_perm", EterniaServer.cmdsLocale.getPerm(Commands.ITEM_CLEAR_NAME),
                "item_add_lore", EterniaServer.cmdsLocale.getName(Commands.ITEM_ADD_LORE),
                "item_add_lore_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.ITEM_ADD_LORE),
                "item_add_lore_description", EterniaServer.cmdsLocale.getDescription(Commands.ITEM_ADD_LORE),
                "item_add_lore_perm", EterniaServer.cmdsLocale.getPerm(Commands.ITEM_ADD_LORE),
                "item_set", EterniaServer.cmdsLocale.getName(Commands.ITEM_SET),
                "item_set_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.ITEM_SET),
                "item_set_description", EterniaServer.cmdsLocale.getDescription(Commands.ITEM_SET),
                "item_set_perm", EterniaServer.cmdsLocale.getPerm(Commands.ITEM_SET),
                "item_set_lore", EterniaServer.cmdsLocale.getName(Commands.ITEM_SET_LORE),
                "item_set_lore_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.ITEM_SET_LORE),
                "item_set_lore_description", EterniaServer.cmdsLocale.getDescription(Commands.ITEM_SET_LORE),
                "item_set_lore_perm", EterniaServer.cmdsLocale.getPerm(Commands.ITEM_SET_LORE),
                "item_set_name", EterniaServer.cmdsLocale.getName(Commands.ITEM_SET_NAME),
                "item_set_name_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.ITEM_SET_NAME),
                "item_set_name_description", EterniaServer.cmdsLocale.getDescription(Commands.ITEM_SET_NAME),
                "item_set_name_perm", EterniaServer.cmdsLocale.getPerm(Commands.ITEM_SET_NAME),
                "kit", EterniaServer.cmdsLocale.getName(Commands.KIT),
                "kit_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.KIT),
                "kit_description", EterniaServer.cmdsLocale.getDescription(Commands.KIT),
                "kit_perm", EterniaServer.cmdsLocale.getPerm(Commands.KIT),
                "kits", EterniaServer.cmdsLocale.getName(Commands.KIT_LIST),
                "kits_description", EterniaServer.cmdsLocale.getDescription(Commands.KIT_LIST),
                "kits_perm", EterniaServer.cmdsLocale.getPerm(Commands.KIT_LIST),
                "mute", EterniaServer.cmdsLocale.getName(Commands.MUTE),
                "mute_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.MUTE),
                "mute_description", EterniaServer.cmdsLocale.getDescription(Commands.MUTE),
                "mute_perm", EterniaServer.cmdsLocale.getPerm(Commands.MUTE),
                "mute_channels", EterniaServer.cmdsLocale.getName(Commands.MUTE_CHANNELS),
                "mute_channels_description", EterniaServer.cmdsLocale.getDescription(Commands.MUTE_CHANNELS),
                "mute_channels_perm", EterniaServer.cmdsLocale.getPerm(Commands.MUTE_CHANNELS),
                "mute_perma", EterniaServer.cmdsLocale.getName(Commands.MUTE_PERMA),
                "mute_perma_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.MUTE_PERMA),
                "mute_perma_description", EterniaServer.cmdsLocale.getDescription(Commands.MUTE_PERMA),
                "mute_perma_perm", EterniaServer.cmdsLocale.getPerm(Commands.MUTE_PERMA),
                "mute_undo", EterniaServer.cmdsLocale.getName(Commands.MUTE_UNDO),
                "mute_undo_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.MUTE_UNDO),
                "mute_undo_description", EterniaServer.cmdsLocale.getDescription(Commands.MUTE_UNDO),
                "mute_undo_perm", EterniaServer.cmdsLocale.getPerm(Commands.MUTE_UNDO),
                "mute_temp", EterniaServer.cmdsLocale.getName(Commands.MUTE_TEMP),
                "mute_temp_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.MUTE_TEMP),
                "mute_temp_description", EterniaServer.cmdsLocale.getDescription(Commands.MUTE_TEMP),
                "mute_temp_perm", EterniaServer.cmdsLocale.getPerm(Commands.MUTE_TEMP),
                "nick", EterniaServer.cmdsLocale.getName(Commands.NICKNAME),
                "nick_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.NICKNAME),
                "nick_description", EterniaServer.cmdsLocale.getDescription(Commands.NICKNAME),
                "nick_perm", EterniaServer.cmdsLocale.getPerm(Commands.NICKNAME),
                "nick_deny", EterniaServer.cmdsLocale.getName(Commands.NICKNAME_DENY),
                "nick_deny_description", EterniaServer.cmdsLocale.getDescription(Commands.NICKNAME_DENY),
                "nick_deny_perm", EterniaServer.cmdsLocale.getPerm(Commands.NICKNAME_DENY),
                "nick_accept", EterniaServer.cmdsLocale.getName(Commands.NICKNAME_ACCEPT),
                "nick_accept_description", EterniaServer.cmdsLocale.getDescription(Commands.NICKNAME_ACCEPT),
                "nick_accept_perm", EterniaServer.cmdsLocale.getPerm(Commands.NICKNAME_ACCEPT),
                "usekey", EterniaServer.cmdsLocale.getName(Commands.KEY_USE),
                "usekey_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.KEY_USE),
                "usekey_description", EterniaServer.cmdsLocale.getDescription(Commands.KEY_USE),
                "usekey_perm", EterniaServer.cmdsLocale.getPerm(Commands.KEY_USE),
                "genkey", EterniaServer.cmdsLocale.getName(Commands.KEY_GEN),
                "genkey_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.KEY_GEN),
                "genkey_description", EterniaServer.cmdsLocale.getDescription(Commands.KEY_GEN),
                "genkey_perm", EterniaServer.cmdsLocale.getPerm(Commands.KEY_GEN),
                "spawnergive", EterniaServer.cmdsLocale.getName(Commands.SPAWNERGIVE),
                "spawnergive_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.SPAWNERGIVE),
                "spawnergive_description", EterniaServer.cmdsLocale.getDescription(Commands.SPAWNERGIVE),
                "spawnergive_perm", EterniaServer.cmdsLocale.getPerm(Commands.SPAWNERGIVE),
                "tpall", EterniaServer.cmdsLocale.getName(Commands.TP_ALL),
                "tpall_description", EterniaServer.cmdsLocale.getDescription(Commands.TP_ALL),
                "tpall_perm", EterniaServer.cmdsLocale.getPerm(Commands.TP_ALL),
                "back", EterniaServer.cmdsLocale.getName(Commands.TP_BACK),
                "back_description", EterniaServer.cmdsLocale.getDescription(Commands.TP_BACK),
                "back_perm", EterniaServer.cmdsLocale.getPerm(Commands.TP_BACK),
                "tpa", EterniaServer.cmdsLocale.getName(Commands.TP_TPA),
                "tpa_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.TP_TPA),
                "tpa_description", EterniaServer.cmdsLocale.getDescription(Commands.TP_TPA),
                "tpa_perm", EterniaServer.cmdsLocale.getPerm(Commands.TP_TPA),
                "tpa_accept", EterniaServer.cmdsLocale.getName(Commands.TP_TPA_ACCEPT),
                "tpa_accept_description", EterniaServer.cmdsLocale.getDescription(Commands.TP_TPA_ACCEPT),
                "tpa_accept_perm", EterniaServer.cmdsLocale.getPerm(Commands.TP_TPA_ACCEPT),
                "tpa_deny", EterniaServer.cmdsLocale.getName(Commands.TP_TPA_DENY),
                "tpa_deny_description", EterniaServer.cmdsLocale.getDescription(Commands.TP_TPA_DENY),
                "tpa_deny_perm", EterniaServer.cmdsLocale.getPerm(Commands.TP_TPA_DENY),
                "spawn", EterniaServer.cmdsLocale.getName(Commands.SPAWN),
                "spawn_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.SPAWN),
                "spawn_description", EterniaServer.cmdsLocale.getDescription(Commands.SPAWN),
                "spawn_perm", EterniaServer.cmdsLocale.getPerm(Commands.SPAWN),
                "spawnset", EterniaServer.cmdsLocale.getName(Commands.SPAWN_SET),
                "spawnset_description", EterniaServer.cmdsLocale.getDescription(Commands.SPAWN_SET),
                "spawnset_perm", EterniaServer.cmdsLocale.getPerm(Commands.SPAWN_SET),
                "shop", EterniaServer.cmdsLocale.getName(Commands.SHOP),
                "shop_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.SHOP),
                "shop_description", EterniaServer.cmdsLocale.getDescription(Commands.SHOP),
                "shop_perm", EterniaServer.cmdsLocale.getPerm(Commands.SHOP),
                "setshop", EterniaServer.cmdsLocale.getName(Commands.SHOP_SET),
                "setshop_description", EterniaServer.cmdsLocale.getDescription(Commands.SHOP_SET),
                "setshop_perm", EterniaServer.cmdsLocale.getPerm(Commands.SHOP_SET),
                "delshop", EterniaServer.cmdsLocale.getName(Commands.SHOP_DELETE),
                "delshop_description", EterniaServer.cmdsLocale.getDescription(Commands.SHOP_DELETE),
                "delshop_perm", EterniaServer.cmdsLocale.getPerm(Commands.SHOP_DELETE),
                "warp", EterniaServer.cmdsLocale.getName(Commands.WARP),
                "warp_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.WARP),
                "warp_description", EterniaServer.cmdsLocale.getDescription(Commands.WARP),
                "warp_perm", EterniaServer.cmdsLocale.getPerm(Commands.WARP),
                "setwarp", EterniaServer.cmdsLocale.getName(Commands.WARP_SET),
                "setwarp_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.WARP_SET),
                "setwarp_description", EterniaServer.cmdsLocale.getDescription(Commands.WARP_SET),
                "setwarp_perm", EterniaServer.cmdsLocale.getPerm(Commands.WARP_SET),
                "delwarp", EterniaServer.cmdsLocale.getName(Commands.WARP_DELETE),
                "delwarp_syntax", EterniaServer.cmdsLocale.getSyntax(Commands.WARP_DELETE),
                "delwarp_description", EterniaServer.cmdsLocale.getDescription(Commands.WARP_DELETE),
                "delwarp_perm", EterniaServer.cmdsLocale.getPerm(Commands.WARP_DELETE),
                "listwarp", EterniaServer.cmdsLocale.getName(Commands.WARP_LIST),
                "listwarp_description", EterniaServer.cmdsLocale.getDescription(Commands.WARP_LIST),
                "listwarp_perm", EterniaServer.cmdsLocale.getPerm(Commands.WARP_LIST)
        );
    }

    private void loadConditions() {

        EterniaLib.getManager().getCommandConditions().addCondition(Integer.class, "limits", (c, exec, value) -> {
            if (value == null) {
                return;
            }
            if (c.getConfigValue("min", 0) > value) {
                throw new ConditionFailedException("O valor mínimo precisa ser &3" + c.getConfigValue("min", 0));
            }
            if (c.getConfigValue("max", 3) < value) {
                throw new ConditionFailedException("O valor máximo precisa ser &3 " + c.getConfigValue("max", 3));
            }
        });

        EterniaLib.getManager().getCommandConditions().addCondition(Double.class, "limits", (c, exec, value) -> {
            if (value == null) {
                return;
            }
            if (c.getConfigValue("min", 0) > value) {
                throw new ConditionFailedException("O valor mínimo precisa ser &3" + c.getConfigValue("min", 0));
            }
            if (c.getConfigValue("max", 3) < value) {
                throw new ConditionFailedException("O valor máximo precisa ser &3 " + c.getConfigValue("max", 3));
            }
        });

    }

    private void loadCompletions() {
        EterniaLib.getManager().getCommandCompletions().registerStaticCompletion("colors", Stream.of(Colors.values()).map(Enum::name).collect(Collectors.toList()));
        EterniaLib.getManager().getCommandCompletions().registerStaticCompletion("entidades", Vars.entityList);
    }

    private void loadBedManager() {
        if (sendModuleStatus(EterniaServer.configs.moduleBed, "Bed")) {
            plugin.getServer().getScheduler().runTaskTimer(plugin, new CheckWorld(plugin), 0L, (long) EterniaServer.configs.pluginTicks * 40);
        }
    }

    private void loadBlockRewardsManager() {
        sendModuleStatus(EterniaServer.configs.moduleBlock, "Block-Reward");
    }

    private void loadCommandsManager() {
        if (sendModuleStatus(EterniaServer.configs.moduleCommands, "Commands")) {
            EterniaServer.commands.customCommandMap.forEach((commandName, commandObject) -> new CustomCommand(plugin, commandName, commandObject.getDescription(), commandObject.getAliases(), commandObject.getText(), commandObject.getCommands(), commandObject.getConsole()));
        }
    }

    private void loadCashManager() {
        if (sendModuleStatus(EterniaServer.configs.moduleCash, "Cash")) {
            EterniaLib.getManager().registerCommand(new Cash());
        }
    }

    private void loadChatManager() {
        if (sendModuleStatus(EterniaServer.configs.moduleChat, "Chat")) {
            EterniaLib.getManager().registerCommand(new Channel());
            EterniaLib.getManager().registerCommand(new Mute());
            EterniaLib.getManager().registerCommand(new Chat(plugin));
            EterniaLib.getManager().registerCommand(new Nick());
        }
    }

    private void loadEconomyManager() {
        if (sendModuleStatus(EterniaServer.configs.moduleEconomy, "Economy")) {
            EterniaLib.getManager().registerCommand(new Economy());
        }
    }

    private void loadElevatorManager() {
        sendModuleStatus(EterniaServer.configs.moduleElevator, "Elevator");
    }

    private void loadExperienceManager() {
        if (sendModuleStatus(EterniaServer.configs.moduleExperience, "Experience"))
            EterniaLib.getManager().registerCommand(new Experience());
    }

    private void loadGenericManager() {
        sendModuleStatus(true, "Generic");
        EterniaLib.getManager().registerCommand(new Inventory());
        EterniaLib.getManager().registerCommand(new Generic(plugin));
        EterniaLib.getManager().registerCommand(new Gamemode());
        EterniaLib.getManager().registerCommand(new Glow());
        EterniaLib.getManager().registerCommand(new Item());
    }

    private void loadHomesManager() {
        if (sendModuleStatus(EterniaServer.configs.moduleHomes, "Homes")) {
            EterniaLib.getManager().registerCommand(new Home());
        }
    }

    private void loadKitManager() {
        if (sendModuleStatus(EterniaServer.configs.moduleKits, "Kits")) {
            EterniaLib.getManager().registerCommand(new Kit());
        }
    }

    private void loadPlayerChecks() {
        sendModuleStatus(true, "PlayerChecks");
        if (EterniaServer.configs.asyncCheck) {
            new PluginTick(plugin).runTaskTimerAsynchronously(plugin, 20L, (long) EterniaServer.configs.pluginTicks * 20);
            return;
        }
        new PluginTick(plugin).runTaskTimer(plugin, 20L, (long) EterniaServer.configs.pluginTicks * 20);
    }

    private void loadClearManager() {
        if (sendModuleStatus(EterniaServer.configs.moduleClear, "Mob Control")) {
            new PluginClearSchedule().runTaskTimer(plugin, 20L, 600L);
        }
    }

    private void loadRewardsManager() {
        if (sendModuleStatus(EterniaServer.configs.moduleRewards, "Rewards")) {
            EterniaLib.getManager().registerCommand(new Reward());
        }
    }

    private void loadSpawnersManager() {
        if (sendModuleStatus(EterniaServer.configs.moduleSpawners, "Spawners")) {
            EterniaLib.getManager().registerCommand(new Spawner());
        }
    }

    private void loadTeleportsManager() {
        if (sendModuleStatus(EterniaServer.configs.moduleTeleports, "Teleports")) {
            EterniaLib.getManager().registerCommand(new Warp());
            EterniaLib.getManager().registerCommand(new Teleport());
        }
    }

    private void loadScheduleTasks() {
        if (sendModuleStatus(EterniaServer.configs.moduleSchedule, "Schedule")) {
            long start = ChronoUnit.MILLIS.between(LocalTime.now(), LocalTime.of(
                    EterniaServer.schedule.scheduleHour,
                    EterniaServer.schedule.scheduleMinute,
                    EterniaServer.schedule.scheduleSecond));
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleWithFixedDelay(new PluginSchedule(plugin), start, TimeUnit.HOURS.toMillis(EterniaServer.schedule.scheduleDelay), TimeUnit.MILLISECONDS);
        }
    }

    private boolean sendModuleStatus(final boolean enable, final String module) {
        if (enable) {
            Bukkit.getConsoleSender().sendMessage(EterniaServer.msg.getMessage(Messages.SERVER_MODULE_ENABLED, true, module));
            return true;
        }
        Bukkit.getConsoleSender().sendMessage(EterniaServer.msg.getMessage(Messages.SERVER_MODULE_DISABLED, true, module));
        return false;
    }

}
