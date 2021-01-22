package br.com.eterniaserver.eterniaserver.handlers;

import br.com.eterniaserver.eternialib.NBTItem;
import br.com.eterniaserver.eternialib.SQL;
import br.com.eterniaserver.eternialib.sql.queries.Update;
import br.com.eterniaserver.eterniaserver.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.PlayerRelated;
import br.com.eterniaserver.eterniaserver.api.ServerRelated;
import br.com.eterniaserver.eterniaserver.objects.User;
import br.com.eterniaserver.eterniaserver.enums.Booleans;
import br.com.eterniaserver.eterniaserver.enums.Integers;
import br.com.eterniaserver.eterniaserver.enums.Lists;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.objects.PlayerTeleport;
import br.com.eterniaserver.paperlib.PaperLib;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;

import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PlayerHandler implements Listener {

    private static final String WARP_SPAWN = "warp.spawn";

    private final EterniaServer plugin;
    private final List<Material> list = List.of(Material.RAIL, Material.POWERED_RAIL, Material.DETECTOR_RAIL, Material.ACTIVATOR_RAIL);

    public PlayerHandler(EterniaServer plugin) {
        this.plugin = plugin;
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!EterniaServer.getBoolean(Booleans.MODULE_HOMES) && !EterniaServer.getBoolean(Booleans.MODULE_EXPERIENCE) && !EterniaServer.getBoolean(Booleans.MODULE_SPAWNERS)) return;

        User user = new User(event.getPlayer());
        final Action action = event.getAction();

        if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
            final ItemStack itemStack = event.getItem();
            if (itemFunction(user, itemStack)) {
                event.setCancelled(true);
                return;
            }

            event.setCancelled(blockFunction(event.getClickedBlock(), itemStack, user.hasPermission(EterniaServer.getString(Strings.PERM_SPAWNERS_CHANGE))));
        }

        if (EterniaServer.getBoolean(Booleans.MODULE_SPAWNERS) && event.getClickedBlock() != null
                && action.equals(Action.RIGHT_CLICK_BLOCK) && event.getItem() != null
                && event.getClickedBlock().getType() == Material.SPAWNER
                && !user.hasPermission(EterniaServer.getString(Strings.PERM_SPAWNERS_CHANGE))) {
            event.setCancelled(true);
        }
    }

    private boolean blockFunction(final Block block, final ItemStack itemStack, boolean hasBreakPerm) {
        if (block == null) {
            return false;
        }


        if (list.contains(block.getType())) {
            final Location location = block.getLocation();
            for (Entity entity : location.getNearbyEntities(2, 2, 2)) {
                if (entity instanceof Minecart) {
                    return true;
                }
            }
        }

        return itemStack != null && block.getType() == Material.SPAWNER && !hasBreakPerm;
    }


    private boolean itemFunction(final User user, final ItemStack itemStack) {
        if (!EterniaServer.getBoolean(Booleans.ITEMS_FUNCTIONS) || itemStack == null || itemStack.getType().isAir()) {
            return false;
        }

        final NBTItem nbtItem = new NBTItem(itemStack);
        if (!nbtItem.hasKey(Constants.NBT_FUNCTION)) {
            return false;
        }

        final int function = nbtItem.getInteger(Constants.NBT_FUNCTION);
        switch (function) {
            case 0:
                return expFunction(user, nbtItem);
            case 1:
                return homesFunction(user, nbtItem);
            case 2:
                return customFunction(user, nbtItem);
            default:
                return false;
        }
    }

    private boolean customFunction(final User user, final NBTItem nbtItem) {
        final Player player = user.getPlayer();

        if (nbtItem.hasKey(Constants.NBT_RUN_COMMAND)) {
            for (final String cmd : nbtItem.getStringList(Constants.NBT_RUN_COMMAND)) {
                if (nbtItem.getBoolean(Constants.NBT_RUN_IN_CONSOLE)) {
                    plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), ServerRelated.setPlaceholders(player, cmd));
                } else {
                    player.performCommand(ServerRelated.setPlaceholders(player, cmd));
                }
            }

            final int itemUsages = nbtItem.getInteger(Constants.NBT_USAGES);
            if (itemUsages == 1) {
                player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            } else if (itemUsages > 1) {
                nbtItem.setInteger(Constants.NBT_USAGES, (itemUsages - 1));
                player.getInventory().setItemInMainHand(nbtItem.getItem());
            }
            return true;
        }
        return false;
    }

    private boolean expFunction(final User user, final NBTItem nbtItem) {
        if (!EterniaServer.getBoolean(Booleans.MODULE_EXPERIENCE)) {
            return false;
        }

        user.setItemInMainHand(new ItemStack(Material.AIR));
        user.giveExp(nbtItem.getInteger(Constants.NBT_INT_VALUE));
        return true;
    }

    private boolean homesFunction(final User user, final NBTItem nbtItem) {
        if (!EterniaServer.getBoolean(Booleans.MODULE_HOMES)) {
            return false;
        }

        if (user.isTeleporting()) {
            user.sendMessage(Messages.SERVER_IN_TELEPORT);
            return false;
        }

        user.putInTeleport(new PlayerTeleport(user.getPlayer(), new Location(
                Bukkit.getWorld(nbtItem.getString(Constants.NBT_WORLD)),
                nbtItem.getDouble(Constants.NBT_COORD_X),
                nbtItem.getDouble(Constants.NBT_COORD_Y),
                nbtItem.getDouble(Constants.NBT_COORD_Z),
                nbtItem.getFloat(Constants.NBT_COORD_YAW),
                nbtItem.getFloat(Constants.NBT_COORD_PITCH)
        ), EterniaServer.getMessage(Messages.HOME_GOING, true, nbtItem.getString(Constants.NBT_LOC_NAME))));
        return true;
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (EterniaServer.getBoolean(Booleans.MODULE_TELEPORTS)) {
            final Player player = event.getEntity();
            new User(player).putBackLocation(player.getLocation());
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (EterniaServer.getBoolean(Booleans.MODULE_TELEPORTS)) {
            new User(event.getPlayer()).putBackLocation(event.getFrom());
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (EterniaServer.getBoolean(Booleans.MODULE_TELEPORTS) && ServerRelated.hasLocation(WARP_SPAWN)) {
            event.setRespawnLocation(ServerRelated.getLocation(WARP_SPAWN));
        }
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onPlayerLeave(PlayerQuitEvent event) {
        User user = new User(event.getPlayer());
        user.clear();

        Update update = new Update(EterniaServer.getString(Strings.TABLE_PLAYER));
        update.set.set("hours", user.getAndUpdateTimePlayed());
        update.where.set("uuid", user.getUUID().toString());
        SQL.executeAsync(update);

        Bukkit.broadcastMessage(EterniaServer.getMessage(Messages.SERVER_LOGOUT, true, user.getName(), user.getDisplayName()));
        event.setQuitMessage(null);

    }

    @EventHandler (ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage().toLowerCase();
        if (message.equalsIgnoreCase("/tps")) {
            EterniaServer.sendMessage(event.getPlayer(), Messages.SERVER_TPS, String.format("%.2f", Bukkit.getTPS()[0]));
            event.setCancelled(true);
            return;
        }
        for (String line : EterniaServer.getStringList(Lists.BLACKLISTED_COMMANDS)) {
            if (message.startsWith(line)) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler (priority = EventPriority.LOW)
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        if (!EterniaServer.getBoolean(Booleans.MODULE_BED)) {
            return;
        }

        if (event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK) {
            User user = new User(event.getPlayer());
            if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - user.getBedCooldown()) > 6) {
                user.updateBedCooldown();
                Bukkit.broadcastMessage(EterniaServer.getMessage(Messages.NIGHT_PLAYER_SLEEPING, true, user.getName(), user.getDisplayName()));
            }
        }
    }

    @EventHandler (priority = EventPriority.LOW)
    public void onPlayerBedLeave(PlayerBedLeaveEvent event) {
        if (!EterniaServer.getBoolean(Booleans.MODULE_BED)) {
            return;
        }

        User user = new User(event.getPlayer());
        if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - user.getBedCooldown()) > 6) {
            user.updateBedCooldown();
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        final Player player = event.getPlayer();
        if (EterniaServer.getBoolean(Booleans.MODULE_ELEVATOR) && player.hasPermission(EterniaServer.getString(Strings.PERM_ELEVATOR)) && !player.isSneaking()) {
            Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
            Material material = block.getType();
            for (Material value : EterniaServer.getElevatorMaterials()) {
                if (value == material) {
                    block = block.getRelative(BlockFace.DOWN, EterniaServer.getInteger(Integers.ELEVATOR_MIN));
                    int i;
                    for (i = EterniaServer.getInteger(Integers.ELEVATOR_MAX); i > 0 && (block.getType() != material); block = block.getRelative(BlockFace.DOWN)) --i;
                    elevatorDown(player, i);
                    break;
                }
            }
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onPlayerJump(PlayerJumpEvent event) {
        final Player player = event.getPlayer();
        if (EterniaServer.getBoolean(Booleans.MODULE_ELEVATOR) && player.hasPermission(EterniaServer.getString(Strings.PERM_ELEVATOR))) {
            Block block = event.getTo().getBlock().getRelative(BlockFace.DOWN);
            Material material = block.getType();
            for (Material value : EterniaServer.getElevatorMaterials()) {
                if (value == material) {
                    block = block.getRelative(BlockFace.UP, EterniaServer.getInteger(Integers.ELEVATOR_MIN));
                    int i;
                    for (i = EterniaServer.getInteger(Integers.ELEVATOR_MAX); i > 0 && (block.getType() != material); block = block.getRelative(BlockFace.UP)) -- i;
                    elevatorUp(player, i);
                    break;
                }
            }
        }
    }

    @EventHandler (priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        User user = new User(event.getPlayer());

        event.setJoinMessage(null);

        Bukkit.broadcastMessage(EterniaServer.getMessage(Messages.SERVER_LOGIN, true, user.getName(), user.getDisplayName()));
        user.updateProfile();

        if (EterniaServer.getBoolean(Booleans.MODULE_CHAT)) {
            if (user.hasPermission(EterniaServer.getString(Strings.PERM_SPY))) {
                user.changeSpyState();
            }
            user.setDisplayName();
        }

        for (Player player : PlayerRelated.getVanishList()) {
            user.hidePlayer(plugin, player);
        }

        if (user.getChannel() == 0) {
            user.setChannel(EterniaServer.getString(Strings.DEFAULT_CHANNEL).hashCode());
        }

        user.updateAfkTime();
        user.createKits();
        user.disableFly();

        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, user::teleport, 10L);
    }

    private void elevatorUp(final Player player, final int i) {
        if (i > 0) {
            Location location = player.getLocation();
            location.setY((location.getY() + EterniaServer.getInteger(Integers.ELEVATOR_MAX) + 3.0D - (double) i) - 1);
            PaperLib.teleportAsync(player, location);
            player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.F));
        }
    }

    private void elevatorDown(final Player player, final int i) {
        if (i > 0) {
            Location location = player.getLocation();
            location.setY((location.getY() - EterniaServer.getInteger(Integers.ELEVATOR_MAX) - 3.0D + (double) i) + 1);
            PaperLib.teleportAsync(player, location);
            player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.D));
        }
    }

}
