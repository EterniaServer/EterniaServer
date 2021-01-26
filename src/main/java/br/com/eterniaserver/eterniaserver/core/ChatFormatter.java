package br.com.eterniaserver.eterniaserver.core;

import br.com.eterniaserver.eternialib.UUIDFetcher;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.api.PlayerRelated;
import br.com.eterniaserver.eterniaserver.api.ServerRelated;
import br.com.eterniaserver.eterniaserver.enums.Strings;
import br.com.eterniaserver.eterniaserver.enums.Messages;
import br.com.eterniaserver.eterniaserver.objects.ChannelObject;
import br.com.eterniaserver.eterniaserver.objects.CustomPlaceholder;
import br.com.eterniaserver.eterniaserver.objects.User;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

import org.bukkit.Bukkit;
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

public class ChatFormatter {

	//todo adicionar suporte a hex color

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
				if (player.hasPermission(channelObject.getPerm())) {
					player.spigot().sendMessage(user.getUUID(), baseComponents);
				}
			}
			players.clear();
			return;
		}

		int pes = 0;
		for (Player p : players) {
			if ((user.getPlayer().getWorld() == p.getWorld() && p.getLocation().distanceSquared(user.getPlayer().getLocation()) <= Math.pow(channelObject.getRange(), 2)) || channelObject.getRange() <= 0) {
				pes += 1;
				p.spigot().sendMessage(user.getUUID(), baseComponents);
			} else if (p.hasPermission(EterniaServer.getString(Strings.PERM_SPY)) && PlayerRelated.isSpying(UUIDFetcher.getUUIDOf(p.getName()))) {
				p.sendMessage(user.getUUID(), ServerRelated.getColor(EterniaServer.getString(Strings.CONS_SPY_LOCAL)
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

	private BaseComponent[] customPlaceholder(Player player, String format, String channelColor, String message) {

		Map<Integer, TextComponent> textComponentMap = new TreeMap<>();
		EterniaServer.getCustomPlaceholders().forEach((placeholder, object) -> {
			if (format.contains("{" + placeholder + "}") && player.hasPermission(object.getPermission())) {
				textComponentMap.put(object.getPriority(), getText(player, object));
			}
		});

		String[] messageSplited = message.split(" ");

		StringBuilder stringBuilder = new StringBuilder(channelColor);
	 	final List<TextComponent> textComponentList = new ArrayList<>();

		for (String actualMsg : messageSplited) {
			final TextComponent textComponent = getComponent(actualMsg, player);
			if (textComponent != null) {
				textComponentList.add(getText(stringBuilder.toString(), player));
				stringBuilder = new StringBuilder(channelColor);
				textComponentList.add(getComponent(actualMsg, player));
			} else {
				stringBuilder.append(actualMsg).append(" ");
			}

		}

		if (!stringBuilder.isEmpty()) {
			textComponentList.add(getText(stringBuilder.toString(), player));
		}

		BaseComponent[] baseComponents = new BaseComponent[textComponentMap.size() + textComponentList.size()];
		int i = 0;
		for (TextComponent textComponent : textComponentMap.values()) {
			baseComponents[i++] = textComponent;
		}

		for (TextComponent textComponent : textComponentList) {
			baseComponents[i++] = textComponent;
		}

		return baseComponents;
	}

	private TextComponent getComponent(String actualMsg, Player player) {
		String msg = ChatColor.stripColor(actualMsg);

		if (player.hasPermission(EterniaServer.getString(Strings.PERM_CHAT_MENTION)) && msg.contains(EterniaServer.getString(Strings.MENTION_PLACEHOLDER)) && PlayerRelated.hasNameOnline(msg)) {
			Player target = Bukkit.getPlayer(PlayerRelated.getUUIDFromMention(msg));
			msg = "§3" + msg;
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

		return null;
	}

	private TextComponent getText(String message, Player player) {
		if (player.hasPermission(EterniaServer.getString(Strings.PERM_CHAT_COLOR))) {
			message = ServerRelated.translateHex(message);
		}

		return new TextComponent(message);
	}

	private	TextComponent sendItemInHand(String string, ItemStack itemStack) {
		if (ServerRelated.getVersion() >= 116) {
			final HoverEvent event = new HoverEvent(HoverEvent.Action.SHOW_ITEM, Bukkit.getItemFactory().hoverContentOf(itemStack));
			final String itemName =  itemStack.getI18NDisplayName() == null ? itemStack.getType().toString() : itemStack.getI18NDisplayName();
			TextComponent component = new TextComponent(string.replace(EterniaServer.getString(Strings.SHOW_ITEM_PLACEHOLDER),
					EterniaServer.getString(Strings.CONS_SHOW_ITEM).replace("{0}", String.valueOf(itemStack.getAmount())).replace("{1}", itemName)));
			component.setHoverEvent(event);
			return component;
		}
		return new TextComponent(EterniaServer.getString(Strings.NOT_SUPPORTED));
	}

	private TextComponent getText(Player player, CustomPlaceholder objects) {
		TextComponent textComponent = new TextComponent(ServerRelated.getColor(ServerRelated.setPlaceholders(player, objects.getValue())));
		if (!objects.getHoverText().equals("")) {
			List<TextComponent> textComponentList = new ArrayList<>();
			textComponentList.add(new TextComponent(ServerRelated.getColor(ServerRelated.setPlaceholders(player, objects.getHoverText()))));
			textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(textComponentList.toArray(new TextComponent[textComponentList.size() - 1]))));
		}

		if (!objects.getSuggestCmd().equals("")) {
			textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, ServerRelated.getColor(ServerRelated.setPlaceholders(player, objects.getSuggestCmd()))));
		}
		return textComponent;
	}

}
