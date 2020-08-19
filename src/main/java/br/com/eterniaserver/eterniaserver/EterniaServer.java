package br.com.eterniaserver.eterniaserver;

import br.com.eterniaserver.eterniaserver.generics.*;
import br.com.eterniaserver.eterniaserver.dependencies.eternialib.Files;
import br.com.eterniaserver.eterniaserver.dependencies.vault.VaultHook;

import com.google.common.collect.ImmutableList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.DecimalFormat;
import java.util.List;

public class EterniaServer extends JavaPlugin {

    public static final Location error = new Location(Bukkit.getWorld("world"), 666, 666, 666, 666, 666);
    public static final DecimalFormat df2 = new DecimalFormat(".##");

    public static final List<String> arrData = ImmutableList.of("tblack", "tdarkblue", "tdarkgreen", "tdarkaqua", "tdarkred",
            "tdarkpurple", "tgold", "tlightgray", "tdarkgray", "tblue", "tgreen", "taqua", "tred", "tpurple", "tyellow",
            "twhite");

    public static final List<String> entityList = ImmutableList.of("BEE", "BLAZE", "CAT", "CAVE_SPIDER", "CHICKEN", "COD",
            "COW", "CREEPER", "DOLPHIN", "DONKEY", "DROWNED", "ELDER_GUARDIAN", "ENDER_DRAGON", "ENDERMAN", "ENDERMITE",
            "EVOKER", "FOX", "GHAST", "GIANT", "GUARDIAN", "HOGLIN", "HORSE", "HUSK", "ILLUSIONER", "IRON_GOLEM",
            "MAGMA_CUBE", "MULE", "PANDA", "PARROT", "PHANTOM", "PIG", "PIGLIN", "PILLAGER", "POLAR_BEAR", "PUFFERFISH",
            "RABBIT", "RAVAGER", "SALMON", "SHEEP", "SILVERFISH", "SKELETON", "SKELETON_HORSE", "SLIME", "SNOW_GOLEM",
            "SPIDER", "SQUID", "STRAY", "STRIDER", "TURTLE", "VEX", "VILLAGER", "VINDICATOR", "WITCH", "WITHER",
            "WITHER_SKELETON", "WOLF", "ZOGLIN", "ZOMBIE", "ZOMBIE_HORSE", "ZOMBIFIED_PIGLIN", "ZOMBIE_VILLAGER");

    public static final List<ChatColor> colors = ImmutableList.of(ChatColor.BLACK, ChatColor.DARK_BLUE, ChatColor.DARK_GREEN,
            ChatColor.DARK_AQUA, ChatColor.DARK_RED, ChatColor.DARK_PURPLE, ChatColor.GOLD, ChatColor.GRAY,
            ChatColor.DARK_GRAY, ChatColor.BLUE, ChatColor.GREEN, ChatColor.AQUA, ChatColor.RED, ChatColor.LIGHT_PURPLE,
            ChatColor.YELLOW, ChatColor.WHITE);

    public static final FileConfiguration serverConfig = new YamlConfiguration();
    public static final FileConfiguration msgConfig = new YamlConfiguration();
    public static final FileConfiguration cmdConfig = new YamlConfiguration();
    public static final FileConfiguration rewardsConfig = new YamlConfiguration();
    public static final FileConfiguration blockConfig = new YamlConfiguration();
    public static final FileConfiguration kitConfig = new YamlConfiguration();
    public static final FileConfiguration chatConfig = new YamlConfiguration();
    public static final FileConfiguration placeholderConfig = new YamlConfiguration();
    public static final FileConfiguration groupConfig = new YamlConfiguration();
    public static final FileConfiguration cashConfig = new YamlConfiguration();

    public final Files files = new Files(this);

    @Override
    public void onEnable() {

        files.loadConfigs();
        files.loadMessages();
        files.loadDatabase();
        
        loadManagers();
        vaultHook();

        new PlaceHolders().register();

        this.getServer().getPluginManager().registerEvents(new OnPlayerJump(), this);
        this.getServer().getPluginManager().registerEvents(new OnEntityDamage(), this);
        this.getServer().getPluginManager().registerEvents(new OnEntityDamageByEntity(), this);
        this.getServer().getPluginManager().registerEvents(new OnEntityInventoryClick(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerBlockBreak(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerBlockPlace(), this);
        this.getServer().getPluginManager().registerEvents(new OnAsyncPlayerChat(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerCommandPreProcess(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerDeath(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerInteract(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerLeave(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerJoin(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerMove(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerRespawn(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerSignChange(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerTeleport(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerToggleSneak(), this);
        this.getServer().getPluginManager().registerEvents(new OnServerListPing(), this);

    }

    private void vaultHook() {
        new VaultHook(this);
    }

    private void loadManagers() {
        new Managers(this);
    }

    public Files getFiles() {
        return files;
    }

}
