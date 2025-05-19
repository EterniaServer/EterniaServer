package br.com.eterniaserver.eterniaserver.modules.spawner;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eternialib.chat.MessageOptions;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Entities;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import br.com.eterniaserver.eterniaserver.modules.core.Entities.PlayerProfile;
import br.com.eterniaserver.eterniaserver.modules.core.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class Commands {

    private Commands() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    @CommandAlias("%SPAWNER_GIVE")
    static class Give extends BaseCommand {

        private final EterniaServer plugin;
        private final Services.Spawner spawnerService;

        private static final List<String> entities = Stream.of(Entities.values()).map(Enum::name).collect(Collectors.toList());

        public Give(EterniaServer plugin, Services.Spawner spawnerService) {
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

            Player target = onlineTarget.getPlayer();
            Inventory inventory = target.getInventory();
            if (inventory.firstEmpty() == -1) {
                EterniaLib.getChatCommons().sendMessage(sender, Messages.SPAWNER_INV_FULL);
                return;
            }

            if (value <= 0) {
                value = 1;
            }

            String spawnerName = spawner.toUpperCase(Locale.ROOT);
            inventory.addItem(spawnerService.createSpawner(EntityType.valueOf(spawnerName), value));

            String[] senderNameDisplay = Utils.getNameAndDisplay(sender);
            String senderName = senderNameDisplay[0];
            String senderDisplay = senderNameDisplay[1];

            PlayerProfile targetProfile = EterniaLib.getDatabase().get(PlayerProfile.class, target.getUniqueId());
            String targetName = targetProfile.getPlayerName();
            String targetDisplay = targetProfile.getPlayerDisplay();

            MessageOptions playerOptions = new MessageOptions(spawnerName, targetName, targetDisplay, String.valueOf(value));
            MessageOptions targetOptions = new MessageOptions(spawnerName, senderName, senderDisplay, String.valueOf(value));

            EterniaLib.getChatCommons().sendMessage(sender, Messages.SPAWNER_SENT, playerOptions);
            EterniaLib.getChatCommons().sendMessage(target, Messages.SPAWNER_RECEIVED, targetOptions);
        }

        private void sendTypes(final CommandSender player) {
            MessageOptions options = new MessageOptions(
                    String.join(plugin.getString(Strings.MINI_MESSAGES_ENTITIES_DIVIDER), entities)
            );
            EterniaLib.getChatCommons().sendMessage(player, Messages.SPAWNER_SEND_TYPES, options);
        }

    }
    
}
