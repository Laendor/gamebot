package com.game.cdcs.bot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import com.game.cdcs.bot.entity.MapGraph;
import com.game.cdcs.bot.entity.PlayerProfile;
import com.game.cdcs.bot.handleupdate.CallbackCommand;
import com.game.cdcs.bot.handleupdate.SendResult;
import com.game.cdcs.bot.helper.TelegramHelper;
import com.game.cdcs.bot.repository.CityRepository;
import com.game.cdcs.bot.repository.PlayerProfileRepository;

@Component
public class ShowCitiesToTravel {

	@Autowired
	public TelegramHelper telegramHelper;

	@Autowired
	public PlayerProfileRepository playerProfileRepository;

	@Autowired
	public CityRepository cityRepository;

	@Autowired
	public MapGraph mapGraph;

	public SendResult buildSendResult(Long chatId) {
		Optional<PlayerProfile> profileOpt = playerProfileRepository.get(chatId);
		if (profileOpt.isEmpty()) {
			return new SendResult(telegramHelper.buildSendTextMessage(chatId, "Profilo non trovato."));
		}
		PlayerProfile profile = profileOpt.get();
		Map<String, Integer> neighbors = mapGraph.getNeighbors(profile.getCurrentCity());
		if (neighbors.isEmpty()) {
			return new SendResult(telegramHelper.buildSendTextMessage(chatId,
					"Non ci sono città vicine disponibili per il viaggio."));
		}

		SendMessage message = telegramHelper.buildSendTextMessage(chatId, "Città disponibili per il viaggio:");
		InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

		for (Map.Entry<String, Integer> entry : neighbors.entrySet()) {
			String city = entry.getKey();
			int cost = entry.getValue();
			InlineKeyboardButton button = telegramHelper.createButton(city + " (Costo: " + cost + " oro)",
					CallbackCommand.MOVE_TO_.name() + city);
			keyboard.add(List.of(button));
		}

		keyboardMarkup.setKeyboard(keyboard);
		message.setReplyMarkup(keyboardMarkup);

		return new SendResult(message);
	}
}
