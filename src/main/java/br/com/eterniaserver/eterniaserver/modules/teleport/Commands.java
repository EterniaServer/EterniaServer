package br.com.eterniaserver.eterniaserver.modules.teleport;

import br.com.eterniaserver.acf.BaseCommand;
import br.com.eterniaserver.acf.annotation.*;
import br.com.eterniaserver.acf.bukkit.contexts.OnlinePlayer;
import br.com.eterniaserver.eternialib.EterniaLib;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.ItemsKeys;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.modules.Constants;
import br.com.eterniaserver.eterniaserver.modules.teleport.Entities.HomeLocation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

final class Commands {

    private Commands() {
        throw new IllegalStateException(Constants.UTILITY_CLASS);
    }

    static class Home extends BaseCommand {

        private final EterniaServer plugin;
        private final Services.HomeService homeService;

        public Home(final EterniaServer plugin, Services.HomeService homeService) {
            this.plugin = plugin;
            this.homeService = homeService;
        }

        @CommandAlias("%DELHOME")
        @Syntax("%DELHOME_SYNTAX")
        @Description("%DELHOME_DESCRIPTION")
        @CommandPermission("%DELHOME_PERM")
        @CommandCompletion("@homes")
        public void onDelHome(Player player, String nome) {
            List<HomeLocation> homes = homeService.getHomes(player.getUniqueId());

            for (HomeLocation home : homes) {
                if (home.getName().equalsIgnoreCase(nome)) {
                    homeService.removeHome(home);
                    plugin.sendMiniMessages(player, Messages.HOME_DELETED, nome);
                    return;
                }
            }

            plugin.sendMiniMessages(player, Messages.HOME_NOT_FOUND, nome);
        }

        @CommandAlias("%HOME")
        @Syntax("%HOME_SYNTAX")
        @Description("%HOME_DESCRIPTION")
        @CommandPermission("%HOME_PERM")
        @CommandCompletion("@homes")
        public void onHome(Player player, String nome, @Optional OnlinePlayer onlineTarget) {
            if (onlineTarget == null) {
                List<HomeLocation> homes = homeService.getHomes(player.getUniqueId());
                if (teleportToHome(homes, nome, player, plugin)) {
                    return;
                }

                plugin.sendMiniMessages(player, Messages.HOME_NOT_FOUND, nome);
                return;
            }

            if (player.hasPermission(plugin.getString(Strings.PERM_HOME_OTHER))) {
                Player target = onlineTarget.getPlayer();
                List<HomeLocation> homes = homeService.getHomes(target.getUniqueId());
                if (teleportToHome(homes, nome, player, plugin)) {
                    return;
                }

                plugin.sendMiniMessages(player, Messages.HOME_NOT_FOUND, nome);
                return;
            }

            plugin.sendMiniMessages(player, Messages.SERVER_NO_PERM);
        }

        private boolean teleportToHome(List<HomeLocation> homes, String nome, Player player, EterniaServer plugin) {
            for (HomeLocation home : homes) {
                if (home.getName().equalsIgnoreCase(nome)) {
                    if (player.hasPermission(plugin.getString(Strings.PERM_TELEPORT_TIME_BYPASS))) {
                        player.teleportAsync(home.getLocation(plugin));
                    } else {
                        Utils.TeleportCommand teleportCommand = new Utils.TeleportCommand(
                                plugin,
                                nome,
                                player,
                                home.getLocation(plugin)
                        );
                        boolean result = EterniaLib.getAdvancedCmdManager().addTimedCommand(teleportCommand);
                        if (!result) {
                            plugin.sendMiniMessages(player, Messages.ALREADY_IN_TIMING, nome);
                        }
                    }
                    return true;
                }
            }

            return false;
        }

        @CommandAlias("%HOMES")
        @Syntax("%HOMES_SYNTAX")
        @Description("%HOMES_DESCRIPTION")
        @CommandPermission("%HOMES_PERM")
        public void onHomes(Player player, @Optional OnlinePlayer target) {
            if (target == null) {
                showHomes(player, player);
                return;
            }

            if (player.hasPermission(plugin.getString(Strings.PERM_HOME_OTHER))) {
                showHomes(player, target.getPlayer());
                return;
            }

            plugin.sendMiniMessages(player, Messages.SERVER_NO_PERM);
        }

        private void showHomes(Player player, Player target) {
            StringBuilder str = new StringBuilder();

            for (String actualHomeName : homeService.getHomeNames(target.getUniqueId())) {
                str.append(plugin.getString(Strings.JOIN_NAMES).replace("{0}", actualHomeName));
            }
            if (!str.isEmpty()) {
                str.setLength(str.length() - 2);
            }

            plugin.sendMiniMessages(player, Messages.HOME_LIST, str.toString());
        }

        @CommandAlias("%SETHOME")
        @Syntax("%SETHOME_SYNTAX")
        @Description("%SETHOME_DESCRIPTION")
        @CommandPermission("%SETHOME_PERM")
        public void onSetHome(Player player, String nome) {
            int i = 3;
            for (int v = 4; v <= 90; v++) {
                if (player.hasPermission(plugin.getString(Strings.PERM_SETHOME_LIMIT_PREFIX) + v)) {
                    i = v;
                }
            }

            nome = nome.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
            if (nome.length() > 16) {
                plugin.sendMiniMessages(player, Messages.HOME_STRING_LIMIT, String.valueOf(16));
                return;
            }

            List<HomeLocation> homes = homeService.getHomes(player.getUniqueId());
            Location location = player.getLocation();

            if (existHome(nome, homes)) {
                for (HomeLocation home : homes) {
                    if (home.getName().equalsIgnoreCase(nome)) {
                        home.setWorldName(location.getWorld().getName());
                        home.setCoordX(location.getX());
                        home.setCoordY(location.getY());
                        home.setCoordZ(location.getZ());
                        home.setCoordYaw((double) location.getYaw());
                        home.setCoordPitch((double) location.getPitch());

                        homeService.updateHome(home);
                        break;
                    }
                }
                plugin.sendMiniMessages(player, Messages.HOME_CREATED, nome);
                return;
            } else if (homes.size() < i) {
                HomeLocation home = new HomeLocation(
                        player.getUniqueId(),
                        nome,
                        location.getWorld().getName(),
                        location.getX(),
                        location.getY(),
                        location.getZ(),
                        (double) location.getYaw(),
                        (double) location.getPitch()
                );
                homeService.addHome(home);
                plugin.sendMiniMessages(player, Messages.HOME_CREATED, nome);
                return;
            }

            if (!player.hasPermission(plugin.getString(Strings.PERM_HOME_COMPASS))) {
                plugin.sendMiniMessages(player, Messages.HOME_NO_PERM_TO_COMPASS);
                return;
            }

            ItemStack item = new ItemStack(Material.COMPASS);
            ItemMeta meta = item.getItemMeta();

            meta.displayName(plugin.getMiniMessage(Messages.HOME_ITEM_NAME, false, nome));
            meta.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.TAG_FUNCTION), PersistentDataType.INTEGER, 1);
            meta.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.TAG_WORLD), PersistentDataType.STRING, location.getWorld().getName());
            meta.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.TAG_COORD_X), PersistentDataType.DOUBLE, location.getX());
            meta.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.TAG_COORD_Y), PersistentDataType.DOUBLE, location.getY());
            meta.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.TAG_COORD_Z), PersistentDataType.DOUBLE, location.getZ());
            meta.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.TAG_COORD_YAW), PersistentDataType.FLOAT, location.getYaw());
            meta.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.TAG_COORD_PITCH), PersistentDataType.FLOAT, location.getPitch());
            meta.getPersistentDataContainer().set(plugin.getKey(ItemsKeys.TAG_LOC_NAME), PersistentDataType.STRING, nome.toLowerCase());
            item.setItemMeta(meta);

            player.getInventory().addItem(item);
            plugin.sendMiniMessages(player, Messages.HOME_LIMIT_REACHED);
        }


        public boolean existHome(String home, List<HomeLocation> homes) {
            for (HomeLocation actualHome : homes) {
                if (actualHome.getName().equalsIgnoreCase(home)) {
                    return true;
                }
            }
            return false;
        }

    }

}
