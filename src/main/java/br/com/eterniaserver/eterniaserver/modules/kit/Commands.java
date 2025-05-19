package br.com.eterniaserver.eterniaserver.modules.kit;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.chat.MessageOptions;
import br.com.eterniaserver.eternialib.database.dtos.SearchField;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.modules.Constants;

import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

final class Commands {

    private Commands() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    static class Kit extends BaseCommand {

        private final EterniaServer plugin;
        private final Services.KitService kitService;

        public Kit(final EterniaServer plugin, Services.KitService kitService) {
            this.plugin = plugin;
            this.kitService = kitService;
        }

        @CommandAlias("%KITS")
        @Description("%KITS_DESCRIPTION")
        @CommandPermission("%KITS_PERM")
        public void onKits(Player player) {
            StringBuilder str = new StringBuilder();
            for (String kitNames : kitService.kitNames()) {
                if (player.hasPermission(plugin.getString(Strings.PERM_KIT_PREFIX) + kitNames)) {
                    str.append(plugin.getString(Strings.JOIN_NAMES).replace("{0}", kitNames));
                }
            }
            str.setLength(str.length() - 2);

            MessageOptions options = new MessageOptions(str.toString());
            EterniaLib.getChatCommons().sendMessage(player, Messages.KIT_LIST, options);
        }

        @CommandAlias("%KIT")
        @Syntax("%KIT_SYNTAX")
        @Description("%KIT_DESCRIPTION")
        @CommandPermission("%KIT_PERM")
        @CommandCompletion("@kits")
        public void onKit(Player player, String kit) {
            if (!kitService.kitNames().contains(kit)) {
                MessageOptions options = new MessageOptions(kit);
                EterniaLib.getChatCommons().sendMessage(player, Messages.KIT_NOT_FOUND, options);
                return;
            }

            if (player.hasPermission(plugin.getString(Strings.PERM_KIT_PREFIX) + kit)) {
                giveKit(player, kit);
            } else {
                EterniaLib.getChatCommons().sendMessage(player, Messages.SERVER_NO_PERM);
            }
        }

        private void giveKit(Player player, String kit) {
            Utils.CustomKit kitObject = kitService.kitList().get(kit);

            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                SearchField uuidSearch = new SearchField("uuid", player.getUniqueId());
                SearchField nameSearch = new SearchField("kitName", kit);

                Entities.KitTime kitTime = EterniaLib.getDatabase().findBy(
                        Entities.KitTime.class,
                        uuidSearch,
                        nameSearch
                );

                Timestamp actualTime = new Timestamp(System.currentTimeMillis());

                if (kitTime == null || kitTime.getUuid() == null) {
                    giveKit(player, kitObject);
                    kitTime = new Entities.KitTime(player.getUniqueId(), kit, actualTime);
                    EterniaLib.getDatabase().insert(Entities.KitTime.class, kitTime);
                } else {
                    Timestamp lastUse = kitTime.getLastUseTime();
                    long time = TimeUnit.MILLISECONDS.toSeconds(actualTime.getTime() - lastUse.getTime());

                    if (time < kitObject.getDelay()) {
                        MessageOptions options = new MessageOptions(String.valueOf(kitObject.getDelay() - time));
                        EterniaLib.getChatCommons().sendMessage(player, Messages.KIT_IN_RECHARGE, options);
                        return;
                    }

                    giveKit(player, kitObject);
                    kitTime.setLastUseTime(actualTime);
                    EterniaLib.getDatabase().update(Entities.KitTime.class, kitTime);
                }

            });
        }

        private void giveKit(Player player, Utils.CustomKit customKit) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                for (String command : customKit.getCommands()) {
                    plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), plugin.setPlaceholders(player, command));
                }
                for (String text : customKit.getMessages()) {
                    player.sendMessage(EterniaLib.getChatCommons().parseColor(plugin.setPlaceholders(player, text)));
                }
            }, 1L);
        }

    }

}
