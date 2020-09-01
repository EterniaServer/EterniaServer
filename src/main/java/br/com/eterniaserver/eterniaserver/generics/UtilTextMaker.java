package br.com.eterniaserver.eterniaserver.generics;

import java.util.ArrayList;

import br.com.eterniaserver.eterniaserver.objects.ChatObject;
import br.com.eterniaserver.eterniaserver.strings.Constants;
import br.com.eterniaserver.eterniaserver.EterniaServer;
import br.com.eterniaserver.eterniaserver.strings.MSG;
import br.com.eterniaserver.eterniaserver.objects.ChatMessage;
import br.com.eterniaserver.eterniaserver.objects.SubPlaceholder;

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

public class UtilTextMaker {

	protected TextComponent text;
	private final ChatMessage message;
	private final Player p;

	public UtilTextMaker(ChatMessage message, Player p) {
		this.p = p;
		this.message = message;
	}

	public void convertMessageToComponents() {
		BaseComponent[] baseComp = new BaseComponent[message.size()];
		for(int i = 0; i < message.size(); i++) {
			ChatObject chatObject = message.getChatObjects().get(i);
			String msg = chatObject.message;
			msg = UtilInternMethods.setPlaceholders(p, msg);
			if (msg.contains(Constants.MESSAGE)) msg = msg.replace(Constants.MESSAGE, message.getMessageSent());
			if (p.hasPermission("eternia.chat.mention") && msg.contains("@")) {
				int lenght = msg.length();
				for (int v = 0; v < lenght; v++) {
					String playerName;
					if (Character.toString(msg.charAt(v)).equals("@")) {
						if (lenght > v + 16) playerName = msg.substring(v, v + 16).split(" ")[0];
						else playerName = msg.substring(v, v + lenght - 1).split(" ")[0];
						msg = msg.replace(playerName, Vars.colors.get(3) + playerName + Vars.colors.get(15));
						if (Vars.playersName.containsKey(playerName)) {
							Player player = Bukkit.getPlayer(Vars.playersName.get(playerName));
							if (player != null && player.isOnline()) {
								player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(1, Note.Tone.F));
								player.sendTitle(MSG.getColor(p.getDisplayName()), MSG.getColor("&7mencionou você&8!"), 10, 40, 10);
							}
						}
						break;
					}
				}
			}
			ItemStack itemStack = p.getInventory().getItemInMainHand();
			if (p.hasPermission("eternia.chat.item") && msg.contains("[item]") && (itemStack != null && !itemStack.getType().equals(Material.AIR))) {
				baseComp[i] = sendItemInHand(msg, itemStack);
			} else {
				TextComponent textComp = new TextComponent(TextComponent.fromLegacyText(msg));
				if (chatObject.getHover() != null) {
					ArrayList<TextComponent> tcs = new ArrayList<>();
					tcs.add(new TextComponent(UtilInternMethods.setPlaceholders(p, MSG.getColor(chatObject.getHover()))));
					textComp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(tcs.toArray(new TextComponent[tcs.size() - 1]))));
				}
				if (chatObject.getColor() != null) {
					textComp.setColor(chatObject.getColor().asBungee());
				}
				if (chatObject.getSuggest() != null) {
					textComp.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, UtilInternMethods.setPlaceholders(p, MSG.getColor(chatObject.getSuggest()))));
				}
				if (chatObject.getRun() != null) {
					textComp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, UtilInternMethods.setPlaceholders(p, MSG.getColor(chatObject.getRun()))));
				}
				if (chatObject.isText()) {
					setTextAttr(textComp, p);
				}
				baseComp[i] = textComp;
			}
		}
		text = new TextComponent(baseComp);
	}

	private	TextComponent sendItemInHand(String string, ItemStack itemStack) {
		if (EventAsyncPlayerChat.version116) {
			HoverEvent event = new HoverEvent(HoverEvent.Action.SHOW_ITEM, Bukkit.getItemFactory().hoverContentOf(itemStack));
			TextComponent component = new TextComponent(string.replace("[item]", Vars.colors.get(3) + "x" + itemStack.getAmount() + " " + itemStack.getI18NDisplayName() + Vars.colors.get(15)));
			component.setHoverEvent(event);
			return component;
		}
		return new TextComponent("sem suporte");
	}

	private void setTextAttr(TextComponent text, Player p) {
		if (getConfigBoolean(p, "on_click.suggest_command")) {
			addClickSuggest(text, customPlaceholder(p, getConfigString(p, "on_click.suggested_command")));
		}
		if (getConfigBoolean(p, "on_click.run_command")) {
			addClickRun(text, customPlaceholder(p, getConfigString(p, "on_click.runned_command")));
		}
		if(getConfigBoolean(p, "on_hover.show_text")) {
			addHover(text, customPlaceholder(p, getConfigString(p, "on_hover.text_shown")));
		}
	}

	public void addHover(TextComponent text, String s) {
		if(s == null) return;
		ArrayList<TextComponent> tcs = new ArrayList<>();
		tcs.add(new TextComponent(MSG.getColor(s)));
		text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(tcs.toArray(new TextComponent[tcs.size() - 1]))));
	}

	public void addClickSuggest(TextComponent text, String s) {
		if(s == null) return;
		text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, s));
	}

	public void addClickRun(TextComponent text, String s) {
		if(s == null) return;
		text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, s));
	}

	public TextComponent getText() {
		return text;
	}

	public String getConfigString(Player p, String extra) {
		return EterniaServer.groupConfig.getString (Vars.uufi.get(p.getName()).getName() + "." + extra);
	}

	public boolean getConfigBoolean(Player p, String extra) {
		return EterniaServer.groupConfig.getBoolean(Vars.uufi.get(p.getName()).getName() + "." + extra);
	}

	public String customPlaceholder(Player p, String s2) {
		String stringMessage = s2;
		for(UtilCustomPlaceholder cp: Vars.customPlaceholders) {
			String id = cp.getId();
			if(!stringMessage.contains("{" + id + "}")) continue;
			SubPlaceholder bestPlaceholder = UtilInternMethods.getSubPlaceholder(p, cp);
			if(bestPlaceholder != null) {
				stringMessage = stringMessage.replace("{" + id + "}", bestPlaceholder.getValue());
			} else {
				stringMessage = stringMessage.replace("{" + id + "}", "");
			}
		}
		s2 = stringMessage;
		return UtilInternMethods.setPlaceholders(p, s2);
	}

}