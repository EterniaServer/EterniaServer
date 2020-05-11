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
            Player player = (Player) sender;
            if (player.hasPermission("eternia.blocks")) {
                int coal = 0, lapiz = 0, redstone = 0, iron = 0, gold = 0, diamond = 0, esmeralda = 0;
                for (ItemStack i : player.getInventory().getContents()) {
                    if (i != null) {
                        if (i.isSimilar(coali) && i.getItemMeta().getDisplayName().equals(coali.getItemMeta().getDisplayName())) {
                            coal += i.getAmount();
                        } else if (i.isSimilar(lapizi) && i.getItemMeta().getDisplayName().equals(lapizi.getItemMeta().getDisplayName())) {
                            lapiz += i.getAmount();
                        } else if (i.isSimilar(redstonei) && i.getItemMeta().getDisplayName().equals(redstonei.getItemMeta().getDisplayName())) {
                            redstone += i.getAmount();
                        } else if (i.isSimilar(ironi) && i.getItemMeta().getDisplayName().equals(ironi.getItemMeta().getDisplayName())) {
                            iron += i.getAmount();
                        } else if (i.isSimilar(goldi) && i.getItemMeta().getDisplayName().equals(goldi.getItemMeta().getDisplayName())) {
                            gold += i.getAmount();
                        } else if (i.isSimilar(diamondi) && i.getItemMeta().getDisplayName().equals(diamondi.getItemMeta().getDisplayName())) {
                            diamond += i.getAmount();
                        } else if (i.isSimilar(esmeraldai) && i.getItemMeta().getDisplayName().equals(esmeraldai.getItemMeta().getDisplayName())) {
                            esmeralda += i.getAmount();
                        }
                    }
                }
                int itens = coal / 9;
                if (itens != 0) {
                    player.getInventory().removeItem(new ItemStack(Material.COAL, itens * 9));
                    player.getInventory().addItem(new ItemStack(Material.COAL_BLOCK, itens));
                }
                itens = lapiz / 9;
                if (itens != 0) {
                    player.getInventory().removeItem(new ItemStack(Material.LAPIS_LAZULI, itens * 9));
                    player.getInventory().addItem(new ItemStack(Material.LAPIS_BLOCK, itens));
                }
                itens = redstone / 9;
                if (itens != 0) {
                    player.getInventory().removeItem(new ItemStack(Material.REDSTONE, itens * 9));
                    player.getInventory().addItem(new ItemStack(Material.REDSTONE_BLOCK, itens));
                }
                itens = iron / 9;
                if (itens != 0) {
                    player.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, itens * 9));
                    player.getInventory().addItem(new ItemStack(Material.IRON_BLOCK, itens));
                }
                itens = gold / 9;
                if (itens != 0) {
                    player.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, itens * 9));
                    player.getInventory().addItem(new ItemStack(Material.GOLD_BLOCK, itens));
                }
                itens = diamond / 9;
                if (itens != 0) {
                    player.getInventory().removeItem(new ItemStack(Material.DIAMOND, itens * 9));
                    player.getInventory().addItem(new ItemStack(Material.DIAMOND_BLOCK, itens));
                }
                itens = esmeralda / 9;
                if (itens != 0) {
                    player.getInventory().removeItem(new ItemStack(Material.EMERALD, itens * 9));
                    player.getInventory().addItem(new ItemStack(Material.EMERALD_BLOCK, itens));
                }
                messages.PlayerMessage("other.done", player);
            } else {
                messages.PlayerMessage("server.no-perm", player);
            }
        } else {
            messages.ConsoleMessage("server.only-player");
        }
        return true;
    }

}
