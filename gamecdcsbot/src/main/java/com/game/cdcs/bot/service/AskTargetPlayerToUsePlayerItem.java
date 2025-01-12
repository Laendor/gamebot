package com.game.cdcs.bot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import com.game.cdcs.bot.entity.Item;
import com.game.cdcs.bot.entity.PlayerProfile;
import com.game.cdcs.bot.handleupdate.CallbackCommand;
import com.game.cdcs.bot.handleupdate.SendResult;
import com.game.cdcs.bot.helper.TelegramHelper;
import com.game.cdcs.bot.repository.PlayerProfileRepository;

@Component
public class AskTargetPlayerToUsePlayerItem {
	@Autowired
	public TelegramHelper telegramHelper;

	@Autowired
	public PlayerProfileRepository playerProfileRepository;

	public SendResult buildSendResult(Long chatId, String playerItemIdString) {
		Optional<PlayerProfile> profileOpt = playerProfileRepository.get(chatId);
		if (profileOpt.isEmpty()) {
			return new SendResult(telegramHelper.buildSendTextMessage(chatId, "Profilo non trovato."));
		}
		PlayerProfile profile = profileOpt.get();

		Long playerItemId;
		try {
			playerItemId = Long.parseLong(playerItemIdString);
		} catch (Exception e) {
			return new SendResult(telegramHelper.buildSendTextMessage(chatId, "Identificativo oggetto non valido."));
		}

		Optional<Item> itemOpt = profile.getItem(playerItemId);
		if (itemOpt.isEmpty()) {
			return new SendResult(telegramHelper.buildSendTextMessage(chatId, "Oggetto non trovato."));
		}
		Item item = itemOpt.get();

		var availablePlayerTargetable = playerProfileRepository.getAllProfilesExcept(playerItemId);
		if (availablePlayerTargetable.isEmpty()) {
			return new SendResult(
					telegramHelper.buildSendTextMessage(chatId, "Non ci sono validi bersagli per l'oggetto"));
		}

		var sendMessage = telegramHelper.buildSendTextMessage(chatId,
				"Seleziona il bersaglio di  " + item.getItemEffect().getName() + "\n");

		InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

		for (PlayerProfile otherProfilePlayer : availablePlayerTargetable) {
			InlineKeyboardButton useItemButton = telegramHelper.createButton(otherProfilePlayer.getName(),
					CallbackCommand.USE_ITEM_ON_ANY_OTHER_PLAYER.name() + item.getId() + ":"
							+ otherProfilePlayer.getChatId());
			keyboard.add(List.of(useItemButton));
		}

		keyboardMarkup.setKeyboard(keyboard);
		sendMessage.setReplyMarkup(keyboardMarkup);

		return new SendResult(sendMessage);
	}
}
