package com.game.cdcs.bot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import com.game.cdcs.bot.entity.City;
import com.game.cdcs.bot.entity.PlayerProfile;
import com.game.cdcs.bot.entity.Route;
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

	public SendResult buildSendResult(Long chatId) {
		Optional<PlayerProfile> profileOpt = playerProfileRepository.get(chatId);
		if (profileOpt.isEmpty()) {
			return new SendResult(telegramHelper.buildSendTextMessage(chatId, "Profilo non trovato."));
		}
		PlayerProfile profile = profileOpt.get();
		City currentCity = profile.getCurrentCity();
		List<Route> routes = currentCity.getRoutes();
		if (routes.isEmpty()) {
			return new SendResult(telegramHelper.buildSendTextMessage(chatId,
					"Non ci sono città vicine disponibili per il viaggio."));
		}

		SendMessage message = telegramHelper.buildSendTextMessage(chatId, "Città disponibili per il viaggio:");
		InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

		for (Route route : routes) {
			City cityTo = route.getTo();
			int cost = route.getCost();
			InlineKeyboardButton button = telegramHelper.createButton(cityTo.getName() + " (Costo: " + cost + " oro)",
					CallbackCommand.MOVE_TO_.name() + cityTo.getName());
			keyboard.add(List.of(button));
		}

		keyboardMarkup.setKeyboard(keyboard);
		message.setReplyMarkup(keyboardMarkup);

		return new SendResult(message);
	}
}
