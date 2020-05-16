package br.com.eterniaserver.eterniaserver.modules.genericmanager.commands.others;

import br.com.eterniaserver.eterniaserver.configs.Messages;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Blocks implements CommandExecutor {

    private final Messages messages;

    private final ItemStack coali = new ItemStack(Material.COAL);
    private final ItemStack lapizi = new ItemStack(Material.LAPIS_LAZULI);
    private final ItemStack redstonei = new ItemStack(Material.REDSTONE);
    private final ItemStack ironi = new ItemStack(Material.IRON_INGOT);
    private final ItemStack goldi = new ItemStack(Material.GOLD_INGOT);
    private final ItemStack diamondi = new ItemStack(Material.DIAMOND);
    private final ItemStack esmeraldai = new ItemStack(Material.EMERALD);

    public Blocks(Messages messages) {
        this.messages = messages;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            if (!sender.hasPermission("eternia.blocks")) {
                messages.sendMessage("server.no-perm", sender);
                return false;
            }

            Player player = (Player) sender;

            int coal = 0, lapiz = 0, redstone = 0, iron = 0, gold = 0, diamond = 0, esmeralda = 0;
            for (ItemStack i : player.getInventory().getContents()) {
                if (i != null) {
                    coal += checkItems(i, coali);
                    lapiz += checkItems(i, lapizi);
                    redstone += checkItems(i, redstonei);
                    iron += checkItems(i, ironi);
                    gold += checkItems(i, goldi);
                    diamond += checkItems(i, diamondi);
                    esmeralda += checkItems(i, esmeraldai);
                }
            }

            convertItems(coal, Material.COAL, Material.COAL_BLOCK, player);
            convertItems(lapiz, Material.LAPIS_LAZULI, Material.LAPIS_BLOCK, player);
            convertItems(redstone, Material.REDSTONE, Material.REDSTONE_BLOCK, player);
            convertItems(iron, Material.IRON_INGOT, Material.IRON_BLOCK, player);
            convertItems(gold, Material.GOLD_INGOT, Material.GOLD_BLOCK, player);
            convertItems(diamond, Material.DIAMOND, Material.DIAMOND_BLOCK, player);
            convertItems(esmeralda, Material.EMERALD, Material.EMERALD_BLOCK, player);

            messages.sendMessage("other.done", sender);
        } else {
            messages.sendMessage("server.only-player", sender);
        }
        return true;

    }

    private int checkItems(ItemStack item1, ItemStack item2) {
        if (item1.isSimilar(item2) && item1.getItemMeta().getDisplayName().equals(item2.getItemMeta().getDisplayName())) {
            return item1.getAmount();
        }
        return 0;
    }

    private void convertItems(int items, Material material, Material block, Player player) {
        int amount = items / 9;
        if (amount != 0) {
            player.getInventory().removeItem(new ItemStack(material, amount * 9));
            player.getInventory().addItem(new ItemStack(block, amount));
        }
    }

}
