package com.game.cdcs.bot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import com.game.cdcs.bot.entity.Item;
import com.game.cdcs.bot.entity.ItemEffect;
import com.game.cdcs.bot.entity.PlayerProfile;
import com.game.cdcs.bot.handleupdate.CallbackCommand;
import com.game.cdcs.bot.handleupdate.SendResult;
import com.game.cdcs.bot.helper.TelegramHelper;
import com.game.cdcs.bot.repository.PlayerProfileRepository;

@Component
public class ShowPlayerItems {
	@Autowired
	public TelegramHelper telegramHelper;

	@Autowired
	public PlayerProfileRepository playerProfileRepository;

	public SendResult buildSendResult(Long chatId) {
		Optional<PlayerProfile> profileOpt = playerProfileRepository.get(chatId);
		if (profileOpt.isEmpty()) {
			return new SendResult(telegramHelper.buildSendTextMessage(chatId, "Profilo non trovato."));
		}

		PlayerProfile profile = profileOpt.get();
		var items = new ArrayList<>(profile.getItems());
		if (items.isEmpty()) {
			return new SendResult(
					telegramHelper.buildSendTextMessage(chatId, "Nessun oggetto trovato per il profilo."));
		}

		StringBuilder inventory = new StringBuilder("Oggetti di " + profile.getName() + ":\n");

		InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

		for (int indexItem = 0; indexItem < items.size(); indexItem++) {
			Item item = items.get(indexItem);
			ItemEffect itemEffect = item.getItemEffect();

			inventory//
					.append(indexItem + 1)//
					.append(": ")//
					.append(itemEffect.getName())//
					.append(" <" + itemEffect.getDescription() + "> ")//
					.append("\n");

			InlineKeyboardButton itemButton = telegramHelper.createButton(
					"" + (indexItem + 1) + " - " + itemEffect.getName(),
					CallbackCommand.PLAYER_ITEM_DETAILS_.name() + item.getId());

			keyboard.add(List.of(itemButton));
		}
		keyboardMarkup.setKeyboard(keyboard);

		var sendMessage = telegramHelper.buildSendTextMessage(chatId, inventory.toString());
		sendMessage.setReplyMarkup(keyboardMarkup);

		return new SendResult(sendMessage);
	}
}
