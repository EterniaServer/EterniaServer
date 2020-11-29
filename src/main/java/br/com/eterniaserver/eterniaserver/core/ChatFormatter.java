package br.com.eterniaserver.eterniaserver.core;

import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.objects.ChannelObject;
import br.com.eterniaserver.eterniaserver.objects.CustomPlaceholder;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ChatFormatter {

	public void filter(User user, String message, ChannelObject channelObject, Set<Player> players) {
		if (channelObject == null) {
			channelObject = EterniaServer.getChannelsMap().get(EterniaServer.getString(Strings.DEFAULT_CHANNEL).hashCode());
		}

		if (!user.hasPermission(channelObject.getPerm())) {
			user.sendMessage(Messages.SERVER_NO_PERM);
			return;
		}

		BaseComponent[] baseComponents = customPlaceholder(user.getPlayer(), channelObject.getFormat(), channelObject.getChannelColor(), message);

		if (!channelObject.isHasRange()) {
			for (Player player : players) {
				sendMessage(player, baseComponents, channelObject.getPerm());
			}
			players.clear();
			return;
		}

		int pes = 0;
		for (Player p : players) {
			if ((user.getPlayer().getWorld() == p.getWorld() && p.getLocation().distanceSquared(user.getPlayer().getLocation()) <= Math.pow(channelObject.getRange(), 2)) || channelObject.getRange() <= 0) {
				pes += 1;
				p.spigot().sendMessage(baseComponents);
			} else if (p.hasPermission(EterniaServer.getString(Strings.PERM_SPY)) && Vars.spy.get(UUIDFetcher.getUUIDOf(p.getName()))) {
				p.sendMessage(APIServer.getColor(EterniaServer.getString(Strings.CONS_SPY_LOCAL)
						.replace("{0}", user.getName())
						.replace("{1}", user.getDisplayName())
						.replace("{2}", message)));
			}
		}
		if (pes <= 1) {
			user.sendMessage(Messages.CHAT_NO_ONE_NEAR);
		}
		players.clear();
	}

	private void sendMessage(Player player, BaseComponent[] baseComponents, String perm) {
		if (player.hasPermission(perm)) {
			player.spigot().sendMessage(baseComponents);
		}
	}

	private BaseComponent[] customPlaceholder(Player player, String format, String channelColor, String message) {
		if (player.hasPermission(EterniaServer.getString(Strings.PERM_CHAT_COLOR))) {
			message = message.replace('&', (char) 0x00A7);
		}
		Map<Integer, TextComponent> textComponentMap = new TreeMap<>();
		EterniaServer.getCustomPlaceholders().forEach((placeholder, object) -> {
			if (format.contains("{" + placeholder + "}") && player.hasPermission(object.getPermission())) {
				textComponentMap.put(object.getPriority(), getText(player, object));
			}
		});

		String[] messageSplited = message.split(" ");
		messageSplited[0] = channelColor + messageSplited[0];

		for (int i = 0; i < messageSplited.length; i++) {
			if (i > 0) {
				messageSplited[i] = ChatColor.getLastColors(messageSplited[i - 1]) + messageSplited[i];
			}
		}

		BaseComponent[] baseComponents = new BaseComponent[textComponentMap.size() + messageSplited.length];

		AtomicInteger integer = new AtomicInteger(0);
		textComponentMap.forEach((id, component) -> baseComponents[integer.getAndIncrement()] = component);

		for (String actualMsg : messageSplited) {
			baseComponents[integer.getAndIncrement()] = getComponent(actualMsg, channelColor, player);
		}

		return baseComponents;
	}

	private TextComponent getComponent(String actualMsg, String channelColor, Player player) {
		String msg = ChatColor.stripColor(actualMsg);

		if (player.hasPermission(EterniaServer.getString(Strings.PERM_CHAT_MENTION)) && msg.contains(EterniaServer.getString(Strings.MENTION_PLACEHOLDER)) && Vars.playersName.containsKey(msg)) {
			Player target = Bukkit.getPlayer(Vars.playersName.get(msg));
			msg = "§3" + msg + channelColor;
			if (target != null && target.isOnline()) {
				target.playNote(target.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.F));
				target.sendTitle(EterniaServer.getString(Strings.CONS_MENTION_TITLE).replace("{0}", player.getName()).replace("{1}", player.getDisplayName()),
						EterniaServer.getString(Strings.CONS_MENTION_SUBTITLE).replace("{0}", player.getName()).replace("{1}", player.getDisplayName()), 10, 40, 10);
			}
			return new TextComponent(msg + " ");
		}

		if (player.hasPermission(EterniaServer.getString(Strings.PERM_CHAT_ITEM)) && msg.equals(EterniaServer.getString(Strings.SHOW_ITEM_PLACEHOLDER))) {
			ItemStack itemStack = player.getInventory().getItemInMainHand();
			if (!itemStack.getType().equals(Material.AIR)) {
				return sendItemInHand(msg + " ", itemStack);
			}
		}

		if (player.hasPermission(EterniaServer.getString(Strings.PERM_CHAT_COLOR))) {
			return new TextComponent(channelColor + actualMsg + " ");
		}

		return new TextComponent(channelColor + msg + " ");
	}

	private	TextComponent sendItemInHand(String string, ItemStack itemStack) {
		if (APIServer.getVersion() >= 116) {
			HoverEvent event = new HoverEvent(HoverEvent.Action.SHOW_ITEM, Bukkit.getItemFactory().hoverContentOf(itemStack));
			TextComponent component = new TextComponent(string.replace(EterniaServer.getString(Strings.SHOW_ITEM_PLACEHOLDER),
					EterniaServer.getString(Strings.CONS_SHOW_ITEM).replace("{0}", String.valueOf(itemStack.getAmount())).replace("{1}", itemStack.getI18NDisplayName())));
			component.setHoverEvent(event);
			return component;
		}
		return new TextComponent(EterniaServer.getString(Strings.NOT_SUPPORTED));
	}

	private TextComponent getText(Player player, CustomPlaceholder objects) {
		TextComponent textComponent = new TextComponent(APIServer.getColor(APIServer.setPlaceholders(player, objects.getValue())));
		if (!objects.getHoverText().equals("")) {
			List<TextComponent> textComponentList = new ArrayList<>();
			textComponentList.add(new TextComponent(APIServer.getColor(APIServer.setPlaceholders(player, objects.getHoverText()))));
			textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(textComponentList.toArray(new TextComponent[textComponentList.size() - 1]))));
		}

		if (!objects.getSuggestCmd().equals("")) {
			textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, APIServer.getColor(APIServer.setPlaceholders(player, objects.getSuggestCmd()))));
		}
		return textComponent;
	}

}
