package com.game.cdcs.bot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import com.game.cdcs.bot.entity.PlayerProfile;
import com.game.cdcs.bot.handleupdate.CallbackCommand;
import com.game.cdcs.bot.handleupdate.SendResult;
import com.game.cdcs.bot.helper.TelegramHelper;
import com.game.cdcs.bot.repository.PlayerProfileRepository;

@Component
public class ShowPublicPlayerProfile {

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
		String profileInfo = buildPublicPlayerProfileText(profile);

		var sendMessage = telegramHelper.buildSendTextMessage(chatId, profileInfo);

		if (profile.hasItems()) {
			buildInventoryButton(sendMessage);
		}

		return new SendResult(sendMessage);
	}

	private String buildPublicPlayerProfileText(PlayerProfile profile) {
		return String.format("Profilo Giocatore:\n" //
				+ "Nome: %s\n" //
				+ "Posizione: %s\n" //
				+ "Oro: %d\n" //
				+ "Trofei: %d\n" //
				+ "Oggetti: %d\n", //
				profile.getName(), profile.getCurrentCity(), profile.getGold(), profile.getTrophies(),
				profile.getItems().size());
	}

	private void buildInventoryButton(SendMessage sendMessage) {
		InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

		InlineKeyboardButton detailMissionButton = telegramHelper.createButton("Inventario",
				CallbackCommand.INVENTORY.name());
		keyboard.add(List.of(detailMissionButton));

		keyboardMarkup.setKeyboard(keyboard);
		sendMessage.setReplyMarkup(keyboardMarkup);
	}
}
