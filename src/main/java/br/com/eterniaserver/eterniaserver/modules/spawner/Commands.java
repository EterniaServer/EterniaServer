package br.com.eterniaserver.eterniaserver.modules.spawner;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.CommandAlias;
import br.com.eterniaserver.acf.annotation.CommandCompletion;
import br.com.eterniaserver.acf.annotation.CommandPermission;
import br.com.eterniaserver.acf.annotation.Default;
import br.com.eterniaserver.acf.annotation.Description;
import br.com.eterniaserver.acf.annotation.Syntax;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Entities;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class Commands {

    @CommandAlias("%SPAWNER_GIVE")
    static class Give extends BaseCommand {

        private final EterniaServer plugin;
        private final static List<String> entities = Stream.of(Entities.values()).map(Enum::name).collect(Collectors.toList());
        private final Services.Spawner spawnerService;

        public Give(final EterniaServer plugin, Services.Spawner spawnerService) {
            this.plugin = plugin;
            this.spawnerService = spawnerService;
        }

        @Default
        @CommandCompletion("@valid_entities 1 @players")
        @Syntax("%SPAWNER_GIVE_SYNTAX")
        @Description("%SPAWNER_GIVE_DESCRIPTION")
        @CommandPermission("%SPAWNER_GIVE_PERM")
        public void onSpawnerGive(CommandSender sender, String spawner, Integer value, OnlinePlayer onlineTarget) {
            if (!entities.contains(spawner)) {
                sendTypes(sender);
                return;
            }

            final Player target = onlineTarget.getPlayer();
            final Inventory inventory = target.getInventory();

            if (inventory.firstEmpty() == -1) {
                plugin.sendMiniMessages(sender, Messages.SPAWNER_INV_FULL);
                return;
            }

            if (value <= 0) value = 1;

            final String spawnerName = spawner.toUpperCase(Locale.ROOT);
            final String senderDisplay = sender instanceof Player player ? player.getDisplayName() : sender.getName();

            inventory.addItem(getSpawner(EntityType.valueOf(spawnerName), value));

            plugin.sendMiniMessages(target, Messages.SPAWNER_RECEIVED, spawnerName, sender.getName(), senderDisplay, String.valueOf(value));
            plugin.sendMiniMessages(sender, Messages.SPAWNER_SENT, spawnerName, target.getName(), target.getDisplayName(), String.valueOf(value));
        }

        private ItemStack getSpawner(final EntityType entityType, final int value) {
            ItemStack item = new ItemStack(Material.SPAWNER);
            ItemMeta meta = item.getItemMeta();

            item.setAmount(value);
            meta.displayName(spawnerService.getSpawnerName(entityType));
            item.setItemMeta(meta);
            return item;
        }

        private void sendTypes(final CommandSender player) {
            plugin.sendMiniMessages(
                    player,
                    Messages.SPAWNER_SEND_TYPES,
                    String.join(
                            plugin.getString(Strings.MINI_MESSAGES_ENTITIES_DIVIDER), entities
                    )
            );
        }

    }
    
}
