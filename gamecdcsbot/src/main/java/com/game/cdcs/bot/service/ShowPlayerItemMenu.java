package com.game.cdcs.bot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import com.game.cdcs.bot.entity.Item;
import com.game.cdcs.bot.entity.ItemEffectUsableOnAnyOtherPlayer;
import com.game.cdcs.bot.entity.PlayerProfile;
import com.game.cdcs.bot.handleupdate.CallbackCommand;
import com.game.cdcs.bot.handleupdate.SendResult;
import com.game.cdcs.bot.helper.TelegramHelper;
import com.game.cdcs.bot.repository.PlayerProfileRepository;

@Component
public class ShowPlayerItemMenu {
	@Autowired
	public TelegramHelper telegramHelper;

	@Autowired
	public PlayerProfileRepository playerProfileRepository;

	public SendResult buildSendResult(Long chatId, Long playerItemId) {
		Optional<PlayerProfile> profileOpt = playerProfileRepository.get(chatId);
		if (profileOpt.isEmpty()) {
			return new SendResult(telegramHelper.buildSendTextMessage(chatId, "Profilo non trovato."));
		}
		PlayerProfile profile = profileOpt.get();

		Optional<Item> itemOpt = profile.getItem(playerItemId);
		if (itemOpt.isEmpty()) {
			return new SendResult(telegramHelper.buildSendTextMessage(chatId, "Oggetto non trovato."));
		}
		Item item = itemOpt.get();

		StringBuilder itemDetails = new StringBuilder();
		itemDetails//
				.append("Oggetto " + item.getItemEffect().getName() + "\n")//
				.append("Descrizione " + item.getItemEffect().getDescription() + "\n")//
		;

		SendMessage sendMessage = telegramHelper.buildSendTextMessage(chatId, itemDetails.toString());

		InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

		if (item instanceof ItemEffectUsableOnAnyOtherPlayer) {
			InlineKeyboardButton useItemButton = telegramHelper.createButton("Usa",
					CallbackCommand.ASK_TARGET_PLAYER_FOR_ITEM_.name() + item.getId());
			keyboard.add(List.of(useItemButton));
		}

		keyboardMarkup.setKeyboard(keyboard);
		sendMessage.setReplyMarkup(keyboardMarkup);

		return new SendResult(sendMessage);
	}
}
